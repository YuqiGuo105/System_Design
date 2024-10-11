package StockTrading;

import lombok.Data;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.*;

@Data
public class OrderBook {
    private final String stockId;
    private final long dailyVolumeLimit;
    private final AtomicLong dailyVolume;
    private final PriorityBlockingQueue<Order> buyOrders;
    private final PriorityBlockingQueue<Order> sellOrders;
    private final ReentrantLock matchLock;

    public OrderBook(String stockId, long dailyVolumeLimit) {
        this.stockId = stockId;
        this.dailyVolumeLimit = dailyVolumeLimit;
        this.dailyVolume = new AtomicLong(0);
        this.buyOrders = new PriorityBlockingQueue<>(11, new BuyOrderComparator());
        this.sellOrders = new PriorityBlockingQueue<>(11, new SellOrderComparator());
        this.matchLock = new ReentrantLock();
    }

    public boolean addOrder(Order order) {
        // Reach daily limit
        if (dailyVolume.get() >= dailyVolumeLimit) return false;

        if (order.getOrderType() == OrderType.BUY) buyOrders.offer(order);
        else sellOrders.offer(order);

        attemptMatch();
        return true;
    }

    private void attemptMatch() {
        matchLock.lock();
        try {
            while (!buyOrders.isEmpty() && !sellOrders.isEmpty()) {
                Order highestBuy = buyOrders.peek();
                Order lowestSell = sellOrders.peek();

                if (highestBuy.getPrice() < lowestSell.getPrice()) {
                    break; // 无法匹配
                }

                long tradableQuantity = Math.min(highestBuy.getQuantity(), lowestSell.getQuantity());

                // 检查是否会超过每日交易量限制
                if (dailyVolume.get() + tradableQuantity > dailyVolumeLimit) {
                    tradableQuantity = dailyVolumeLimit - dailyVolume.get();
                    if (tradableQuantity <= 0) {
                        break; // 达到每日交易量上限
                    }
                }

                // 执行交易
                executeTrade(highestBuy, lowestSell, tradableQuantity);

                // 更新每日交易量
                dailyVolume.addAndGet(tradableQuantity);

                // 更新订单
                updateOrders(highestBuy, lowestSell, tradableQuantity);
            }
        } finally {
            matchLock.unlock();
        }
    }

    /**
     * Executes a trade between a buy order and a sell order.
     *
     * @param buyOrder The buy order.
     * @param sellOrder The sell order.
     * @param quantity The quantity to trade.
     */
    private void executeTrade(Order buyOrder, Order sellOrder, long quantity) {
        double tradePrice = sellOrder.getPrice();

        User buyer = StockTradingSystemHolder.getUser(buyOrder.getUserId());
        User seller = StockTradingSystemHolder.getUser(sellOrder.getUserId());

        if (buyer != null && seller != null) {
            buyer.updateProfit((long) (-tradePrice * quantity));
            seller.updateProfit((long) (tradePrice * quantity));
        }

        Trade trade = new Trade(
                UUID.randomUUID().toString(),
                buyOrder.getOrderId(),
                sellOrder.getOrderId(),
                stockId,
                quantity,
                tradePrice,
                System.currentTimeMillis()
        );

        StockTradingSystemHolder.recordTrade(stockId, trade);

        buyOrder.setTrade(trade);
        sellOrder.setTrade(trade);

        buyOrder.setStatus(OrderStatus.COMPLETED);
        sellOrder.setStatus(OrderStatus.COMPLETED);
    }

    /**
     * Updates or removes orders based on the traded quantity.
     *
     * @param buyOrder The buy order.
     * @param sellOrder The sell order.
     * @param quantity The quantity traded.
     */
    private void updateOrders(Order buyOrder, Order sellOrder, long quantity) {
        buyOrders.poll();
        sellOrders.poll();
    }

    /**
     * Resets the daily trading volume.
     */
    public void resetDailyVolume() {
        dailyVolume.set(0);
    }

    // Comparator for buy orders: higher price first, then earlier timestamp
    private static class BuyOrderComparator implements Comparator<Order> {
        @Override
        public int compare(Order o1, Order o2) {
            int priceCompare = Double.compare(o2.getPrice(), o1.getPrice());
            if (priceCompare != 0) return priceCompare;
            return Long.compare(o1.getTimestamp(), o2.getTimestamp());
        }
    }

    // Comparator for sale orders: lower price first, then earlier timestamp
    private static class SellOrderComparator implements Comparator<Order> {
        @Override
        public int compare(Order o1, Order o2) {
            int priceCompare = Double.compare(o1.getPrice(), o2.getPrice());
            if (priceCompare != 0) return priceCompare;
            return Long.compare(o1.getTimestamp(), o2.getTimestamp());
        }
    }
}
