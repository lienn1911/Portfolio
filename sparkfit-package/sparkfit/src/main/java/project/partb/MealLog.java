package project.partb;

import java.io.Serializable;
import java.time.LocalDate;

public class MealLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private String username;
    private String mealTime;
    private String foodItem;
    private int portionSize;
    private double calories;
    private double proteins;
    private double carbohydrates;
    private double fats;

    public MealLog(LocalDate date, String username, String mealTime, String foodItem, int portionSize,
                   double calories, double proteins, double carbohydrates, double fats) {
        this.date = date;
        this.username = username;
        this.mealTime = mealTime;
        this.foodItem = foodItem;
        this.portionSize = portionSize;
        this.calories = calories;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
        this.fats = fats;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return String.format("Date: %s, Meal: %s, Food: %s, Portion: %d, Calories: %.2f, Proteins: %.2f, Carbohydrates: %.2f, Fats: %.2f",
                date, mealTime, foodItem, portionSize, calories, proteins, carbohydrates, fats);
    }
}
