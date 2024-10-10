package Restaurant;

public class Order {
    String orderId;
    String restaurantId;
    String foodItemId;

    public Order(String orderId, String restaurantId, String foodItemId) {
        this.orderId = orderId;
        this.restaurantId = restaurantId;
        this.foodItemId = foodItemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(String foodItemId) {
        this.foodItemId = foodItemId;
    }

}
