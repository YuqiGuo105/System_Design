package StockTrading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StockTradingTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建交易系统实例
        Q06StockTrading tradingSystem = new StockTradingSystem();

        // 初始化每日交易量限制（例如，每只股票每天最多交易1000股）
        tradingSystem.init(1000);

        // 定义股票列表和用户列表
        List<String> stockIds = Arrays.asList("AAPL", "GOOG", "AMZN", "MSFT");
        List<String> userIds = Arrays.asList("user1", "user2", "user3", "user4", "user5");

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 随机数生成器
        Random random = new Random();

        // 定义任务列表
        List<Callable<Void>> tasks = new ArrayList<>();

        // 为每个用户创建买卖订单任务
        for (String userId : userIds) {
            Callable<Void> task = () -> {
                // 每个用户随机提交10个订单
                for (int i = 0; i < 10; i++) {
                    // 随机选择股票
                    String stockId = stockIds.get(random.nextInt(stockIds.size()));

                    // 随机决定是买单还是卖单
                    boolean isBuyOrder = random.nextBoolean();

                    // 随机数量和价格
                    long quantity = random.nextInt(50) + 1; // 1 到 50 股
                    double price = random.nextDouble() * 100 + 100; // 100.0 到 200.0

                    // 提交订单
                    if (isBuyOrder) {
                        int status = tradingSystem.buy(stockId, userId, quantity, price);
                        if (status != 201) {
                            System.out.println("Buy order failed for user " + userId + " on stock " + stockId);
                        }
                    } else {
                        int status = tradingSystem.sell(stockId, userId, quantity, price);
                        if (status != 201) {
                            System.out.println("Sell order failed for user " + userId + " on stock " + stockId);
                        }
                    }

                    // 随机等待一段时间，模拟现实中的交易延迟
                    Thread.sleep(random.nextInt(100));
                }
                return null;
            };
            tasks.add(task);
        }

        // 提交所有任务到线程池
        executorService.invokeAll(tasks);

        // 关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // 输出每个用户的利润和订单历史
        for (String userId : userIds) {
            double profit = tradingSystem.userProfit(userId);
            List<Order> orders = tradingSystem.userHistory(userId);
            System.out.println("User: " + userId);
            System.out.println("Profit: " + profit);
            System.out.println("Order History:");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getOrderId() +
                        ", Stock: " + order.getStockId() +
                        ", Type: " + order.getOrderType() +
                        ", Quantity: " + order.getQuantity() +
                        ", Price: " + order.getPrice() +
                        ", Status: " + order.getStatus());
            }
            System.out.println("----------------------------");
        }

        // 输出每只股票的交易记录
        for (String stockId : stockIds) {
            List<Trade> trades = tradingSystem.stockTradeHistory(stockId);
            System.out.println("Stock: " + stockId);
            System.out.println("Trade History:");
            for (Trade trade : trades) {
                System.out.println("Trade ID: " + trade.getTradeId() +
                        ", Buy Order ID: " + trade.getBuyOrderId() +
                        ", Sell Order ID: " + trade.getSellOrderId() +
                        ", Quantity: " + trade.getQuantity() +
                        ", Price: " + trade.getPrice());
            }
            System.out.println("============================");
        }
    }
}
