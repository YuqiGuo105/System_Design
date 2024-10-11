package StockTrading;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class User {
    public final String userId;
    private final AtomicLong profit;

    public User(String userId) {
        this.userId = userId;
        this.profit = new AtomicLong(0);
    }

    public void updateProfit(long amount) {
        profit.addAndGet(amount);
    }
}
