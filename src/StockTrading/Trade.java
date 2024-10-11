package StockTrading;

import lombok.Data;

@Data
public class Trade {
    private final String tradeId;
    private final String buyOrderId;
    private final String sellOrderId;
    private final String stockId;
    private final long quantity;
    private final double price;
    private final long timestamp;

    public Trade(String tradeId, String buyOrderId, String sellOrderId, String stockId, long quantity, double price, long timestamp) {
        this.tradeId = tradeId;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }
}
