package Restaurant;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FoodItem {
    String foodItemId;
    AtomicLong ratingSum;
    AtomicInteger ratingCount;

    public FoodItem(String foodItemId) {
        this.foodItemId = foodItemId;
        this.ratingSum = new AtomicLong(0);
        this.ratingCount = new AtomicInteger(0);
    }

    /**
     * Calculate the average rating
     * **/
    public double getAverageRating() {
        if (ratingCount.get() == 0) return 0.0;
        double average = (double) ratingSum.get() / ratingCount.get();
        return Math.floor((average + 0.05) * 10) / 10.0;
    }

    /**
     * Add rating
     * */
    public void addRating(int rating) {
        ratingSum.addAndGet(rating);
        ratingCount.incrementAndGet();
    }
}
