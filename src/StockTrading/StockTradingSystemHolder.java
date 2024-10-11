package StockTrading;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class StockTradingSystemHolder {
    private static final ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, CopyOnWriteArrayList<Trade>> stockTrades = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.putIfAbsent(user.userId, user);
    }

    public static User getUser(String userId) {
        return users.get(userId);
    }

    public static void recordTrade(String stockId, Trade trade) {
        stockTrades.computeIfAbsent(stockId, k -> new CopyOnWriteArrayList<>()).add(trade);
    }

    public static List<Trade> getStockTrades(String stockId) {
        return stockTrades.getOrDefault(stockId, new CopyOnWriteArrayList<>());
    }
}
