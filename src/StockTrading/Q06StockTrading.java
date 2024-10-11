package StockTrading;

import java.util.List;

public interface Q06StockTrading {
    /**
     * Initializes the stock trading system with a daily trading volume limit per stock.
     *
     * @param dailyVolumeLimit The maximum number of shares that can be traded per stock each day.
     */
    void init(long dailyVolumeLimit);

    /**
     * Places a buy order for a specified stock on behalf of a user.
     *
     * @param stockId The identifier of the stock to buy.
     * @param userId The identifier of the user placing the buy order.
     * @param quantity The number of shares to buy.
     * @param price The maximum price per share the user is willing to pay.
     * @return Status code: 201 for successful order placement, 400 for invalid inputs, 404 if daily limit is reached.
     */
    int buy(String stockId, String userId, long quantity, double price);

    /**
     * Places a sell order for a specified stock on behalf of a user.
     *
     * @param stockId The identifier of the stock to sell.
     * @param userId The identifier of the user placing the sell order.
     * @param quantity The number of shares to sell.
     * @param price The minimum price per share the user is willing to accept.
     * @return Status code: 201 for successful order placement, 400 for invalid inputs, 404 if daily limit is reached.
     */
    int sell(String stockId, String userId, long quantity, double price);

    /**
     * Retrieves the total profit or loss for a specified user.
     *
     * @param userId The identifier of the user.
     * @return The net profit (positive value) or loss (negative value) of the user.
     */
    long userProfit(String userId);

    /**
     * Retrieves the trading history (list of orders) for a specified user.
     *
     * @param userId The identifier of the user.
     * @return A list of Order objects representing the user's trading history.
     */
    List<Order> userHistory(String userId);

    /**
     * Fetch transaction history for specific stock
     *
     * @param stockId stockId
     * @return List of Trade
     */
    List<Trade> stockTradeHistory(String stockId);
}
