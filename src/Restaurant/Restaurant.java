package Restaurant;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Restaurant {
    String restaurantId;
    AtomicLong overallRatingSum;
    AtomicInteger overallRatingCount;
    ConcurrentHashMap<String, FoodItem> foodItems;

    public Restaurant(String restaurantId) {
        this.restaurantId = restaurantId;
        this.overallRatingSum = new AtomicLong(0);
        this.overallRatingCount = new AtomicInteger(0);
        this.foodItems = new ConcurrentHashMap<>();
    }

    /**
     * 计算餐厅的整体平均评分，保留一位小数
     */
    public double getOverallAverageRating() {
        if (overallRatingCount.get() == 0) return 0.0;
        double average = (double) overallRatingSum.get() / overallRatingCount.get();
        return Math.floor((average + 0.05) * 10) / 10.0;
    }

    /**
     * Overall rating score
     */
    public void addOverallRating(int rating) {
        overallRatingSum.addAndGet(rating);
        overallRatingCount.incrementAndGet();
    }

    /**
     * Add score for the food
     */
    public void addFoodItemRating(String foodItemId, int rating) {
        FoodItem foodItem = foodItems.computeIfAbsent(foodItemId, k -> new FoodItem(foodItemId));
        foodItem.addRating(rating);
    }

    /**
     * 获取指定食物项的平均评分
     */
    public double getFoodItemAverageRating(String foodItemId) {
        FoodItem foodItem = foodItems.get(foodItemId);
        if (foodItem == null) return 0.0;
        return foodItem.getAverageRating();
    }
}
