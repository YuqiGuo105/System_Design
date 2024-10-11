package StockTrading;

import lombok.Data;

public class Order {
    private final String orderId;
    private final String stockId;
    private final String userId;
    private final long quantity;
    private final double price;

    public String getOrderId() {
        return orderId;
    }

    public String getStockId() {
        return stockId;
    }

    public String getUserId() {
        return userId;
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    private final OrderType orderType; // BUY or SELL
    private final long timestamp;
    private OrderStatus status; // PENDING, COMPLETED, CANCELLED
    private Trade trade;

    public Order(String orderId, String stockId, String userId, long quantity, double price, OrderType orderType, long timestamp) {
        this.orderId = orderId;
        this.stockId = stockId;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.orderType = orderType;
        this.timestamp = timestamp;
        this.status = OrderStatus.PENDING;
    }
}

enum OrderStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}

enum OrderType {
    BUY,
    SELL
}
