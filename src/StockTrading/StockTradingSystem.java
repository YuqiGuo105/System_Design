package StockTrading;

import java.util.concurrent.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class StockTradingSystem implements Q06StockTrading{
    private long dailyVolumeLimit;
    private final ConcurrentHashMap<String, OrderBook> orderBooks; // stockId -> OrderBook
    private final ConcurrentHashMap<String, User> users; // userId -> User
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Order>> userOrders; // userId -> List<Order>
    private final ReentrantLock lock = new ReentrantLock();
    private final ExecutorService executorService;

    public StockTradingSystem() {
        this.orderBooks = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.userOrders = new ConcurrentHashMap<>();
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void init(long dailyVolumeLimit) {
        this.dailyVolumeLimit = dailyVolumeLimit;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::resetDailyVolumes, computeInitialDelay(), 24, TimeUnit.HOURS);
    }

    @Override
    public int buy(String stockId, String userId, long quantity, double price) {
        if (stockId == null || userId == null || quantity <= 0 || price <= 0) return 400;

        users.putIfAbsent(userId, new User(userId));

        Order order = new Order(UUID.randomUUID().toString(), stockId, userId, quantity, price, OrderType.BUY, System.currentTimeMillis());
        userOrders.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(order);

        // 异步处理订单
        executorService.submit(() -> processOrder(order));

        return 201; // 创建成功
    }

    @Override
    public int sell(String stockId, String userId, long quantity, double price) {
        if (stockId == null || userId == null || quantity <= 0 || price <= 0) return 400;

        // 确保用户存在
        users.putIfAbsent(userId, new User(userId));
        StockTradingSystemHolder.addUser(users.get(userId));

        // 创建订单
        Order order = new Order(UUID.randomUUID().toString(), stockId, userId, quantity, price, OrderType.SELL, System.currentTimeMillis());
        userOrders.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(order);

        // 异步处理订单
        executorService.submit(() -> processOrder(order));

        return 201;
    }

    @Override
    public long userProfit(String userId) {
        User user = users.get(userId);
        return user != null ? user.getProfit().longValue() : 0;
    }

    @Override
    public List<Order> userHistory(String userId) {
        return userOrders.getOrDefault(userId, new CopyOnWriteArrayList<>());
    }

    @Override
    public List<Trade> stockTradeHistory(String stockId) {
        return StockTradingSystemHolder.getStockTrades(stockId);
    }

    private void processOrder(Order order) {
        OrderBook orderBook = orderBooks.computeIfAbsent(order.getOrderId(), k -> new OrderBook(order.getStockId(), dailyVolumeLimit));
        boolean accepted = orderBook.addOrder(order);
        if (!accepted) {
            order.setStatus(OrderStatus.CANCELLED);
        }
    }

    /**
     * Resets the daily trading volumes for all order books.
     */
    private void resetDailyVolumes() {
        for (OrderBook orderBook : orderBooks.values()) {
            orderBook.resetDailyVolume();
        }
    }

    /**
     * Computes the initial delay until the next midnight for resetting daily volumes.
     *
     * @return The delay in seconds.
     */
    private long computeInitialDelay() {
        Calendar nextMidnight = Calendar.getInstance();
        nextMidnight.add(Calendar.DAY_OF_YEAR, 1);
        nextMidnight.set(Calendar.HOUR_OF_DAY, 0);
        nextMidnight.set(Calendar.MINUTE, 0);
        nextMidnight.set(Calendar.SECOND, 0);
        nextMidnight.set(Calendar.MILLISECOND, 0);
        long currentTime = System.currentTimeMillis();
        long midnightTime = nextMidnight.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toSeconds(midnightTime - currentTime);
    }
}
