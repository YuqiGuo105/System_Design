package Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Solution implements Q05RestaurantRatingInterface{
    private Helper05 helper;
    private ConcurrentHashMap<String, Order> orders;
    private ConcurrentHashMap<String, Restaurant> restaurants;

    public Solution(){}

    public void init(Helper05 helper){
        this.helper=helper;
        this.orders = new ConcurrentHashMap<>();
        this.restaurants = new ConcurrentHashMap<>();
        helper.println("restaurant rating module initialized");
    }

    public void orderFood(String orderId, String restaurantId, String foodItemId) {
        Order order = new Order(orderId, restaurantId, foodItemId);
        orders.put(orderId, order);
        helper.println("Order created: " + orderId + ", restaurant: " + restaurantId + ", food: " + foodItemId);

        restaurants.computeIfAbsent(restaurantId, k -> new Restaurant(restaurantId));
    }

    /**
     * when you(customer) are rating an order e.g giving 4 stars to an orders
     * then it means you are assigning 4 stars to both the food item
     * in that restaurant as well as 4 stars to the overall restaurant rating.
     * - rating ranges from 1 to 5, 5 is best, 1 is worst
     */
    public void rateOrder(String orderId, int rating) {
        Order order = orders.get(orderId);
        if (order == null) {
            helper.println("Rate failed: order doesn't exist - " + orderId);
            return;
        }

        String restaurantId = order.restaurantId;
        String foodItemId = order.foodItemId;

        Restaurant restaurant = restaurants.get(restaurantId);
        if (restaurant == null) {
            helper.println("Rate failed: restaurant doesn't exist - " + orderId);
            return;
        }

        restaurant.addOverallRating(rating);
        restaurant.addFoodItemRating(foodItemId, rating);

        helper.println("Order rating: " + orderId + ", restaurant: " + restaurantId + ", foodItemId: " + foodItemId + ", rating: " + rating);
    }

    /**
     * - Fetches a list of top 20 restaurants
     * - unrated restaurants will be at the bottom of list.
     * - restaurants are sorted in descending order on average ratings
     * of the food item and then based on restaurant id lexicographically
     * - ratings are rounded down to 1 decimal point,
     *  i.e. 4.05, 4.08, 4.11, 4.12, 4.14 all become 4.1,
     *    4.15, 4.19, 4.22, 4.24 all become 4.2
     * - e.g. 'food-item-1':  veg burger is rated 4.3 in restaurant-4
     * and 4.6 in restaurant-6 then we will return ['restaurant-6', 'restaurant-4']
     */
    public List<String> getTopRestaurantsByFood(String foodItemId) {
        PriorityQueue<Restaurant> minHeap = new PriorityQueue<>((a, b) -> {
            double ratingA = a.getFoodItemAverageRating(foodItemId);
            double ratingB = b.getFoodItemAverageRating(foodItemId);
            if (ratingA != ratingB) return Double.compare(ratingA, ratingB);
            return a.restaurantId.compareTo(b.restaurantId);
        });

        for (Restaurant restaurant: restaurants.values()) {
            double avgRating = restaurant.getFoodItemAverageRating(foodItemId);
            if (avgRating > 0.0) minHeap.offer(restaurant);
        }

        List<String> topRestaurants = new ArrayList<>();
        int count = 0;
        while (!minHeap.isEmpty() && count < 20) {
            Restaurant top = minHeap.poll();
            topRestaurants.add(top.restaurantId);
            count++;
        }

        List<String> unratedRestaurants = new ArrayList<>();
        for (Restaurant restaurant : restaurants.values()) {
            if (restaurant.getFoodItemAverageRating(foodItemId) == 0.0) {
                unratedRestaurants.add(restaurant.restaurantId);
            }
        }

        Collections.sort(unratedRestaurants);
        for (String restaurantId : unratedRestaurants) {
            if (topRestaurants.size() >= 20) break;
            topRestaurants.add(restaurantId);
        }

        helper.println("获取食物项 " + foodItemId + " 的前20名餐厅: " + topRestaurants);
        return topRestaurants;
    }

    /**
     * - Here we are talking about restaurant's overall rating and NOT food item's rating.
     */
    public List<String> getTopRatedRestaurants() {
        PriorityQueue<Restaurant> heap = new PriorityQueue<>((a, b) -> {
            double ratingA = a.getOverallAverageRating();
            double ratingB = b.getOverallAverageRating();
            if (ratingA != ratingB) {
                return Double.compare(ratingB, ratingA); // 降序
            }
            return a.restaurantId.compareTo(b.restaurantId); // 字典序
        });

        for (Restaurant restaurant : restaurants.values()) {
            double avgRating = restaurant.getOverallAverageRating();
            if (avgRating > 0.0) { // 只考虑有评分的餐厅
                heap.offer(restaurant);
            }
        }

        List<String> topRestaurants = new ArrayList<>();
        int count = 0;
        while (!heap.isEmpty() && count < 20) {
            Restaurant top = heap.poll();
            topRestaurants.add(top.restaurantId);
            count++;
        }

        // 将未评分的餐厅按餐厅ID字典序排序并添加到列表末尾
        List<String> unratedRestaurants = new ArrayList<>();
        for (Restaurant restaurant : restaurants.values()) {
            if (restaurant.getOverallAverageRating() == 0.0) {
                unratedRestaurants.add(restaurant.restaurantId);
            }
        }
        Collections.sort(unratedRestaurants);
        for (String restaurantId : unratedRestaurants) {
            if (topRestaurants.size() >= 20) break;
            topRestaurants.add(restaurantId);
        }

        helper.println("获取整体评分前20名餐厅: " + topRestaurants);
        return topRestaurants;
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        Helper05 helper = new Helper05();
        solution.init(helper);

        // 模拟订单和评分
        solution.orderFood("order-0", "restaurant-0", "food-1");
        solution.rateOrder("order-0", 3);

        solution.orderFood("order-1", "restaurant-2", "food-0");
        solution.rateOrder("order-1", 1);

        solution.orderFood("order-2", "restaurant-1", "food-0");
        solution.rateOrder("order-2", 3);

        solution.orderFood("order-3", "restaurant-2", "food-0");
        solution.rateOrder("order-3", 5);

        solution.orderFood("order-4", "restaurant-0", "food-0");
        solution.rateOrder("order-4", 3);

        solution.orderFood("order-5", "restaurant-1", "food-0");
        solution.rateOrder("order-5", 4);

        solution.orderFood("order-6", "restaurant-0", "food-1");
        solution.rateOrder("order-6", 2);

        solution.orderFood("order-7", "restaurant-0", "food-1");
        solution.rateOrder("order-7", 2);

        solution.orderFood("order-8", "restaurant-1", "food-0");
        solution.rateOrder("order-8", 2);

        solution.orderFood("order-9", "restaurant-1", "food-0");
        solution.rateOrder("order-9", 4);

        // 获取Top Restaurants by Food
        List<String> topFood0 = solution.getTopRestaurantsByFood("food-0");
        helper.println("Top Restaurants for food-0: " + topFood0);

        List<String> topFood1 = solution.getTopRestaurantsByFood("food-1");
        helper.println("Top Restaurants for food-1: " + topFood1);

        // 获取Top Rated Restaurants
        List<String> topRated = solution.getTopRatedRestaurants();
        helper.println("Top Rated Restaurants: " + topRated);
    }
}
