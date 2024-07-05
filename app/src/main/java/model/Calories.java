package model;

public class Calories {
    private String id;
    private String foodName;
    private int calories;
    private int totalCalories;

    public Calories() {
        // Default constructor required for calls to DataSnapshot.getValue(Calories.class)
    }

    public Calories(String id, String foodName, int calories, int totalCalories) {
        this.id = id;
        this.foodName = foodName;
        this.calories = calories;
        this.totalCalories = totalCalories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }
}
