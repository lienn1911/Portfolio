package project.partb;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WorkoutLog implements Serializable {
    private String username;
    private String exerciseType;
    private int duration;
    private int caloriesBurned;
    private String timestamp;

    // retrieve the workout log information

    public WorkoutLog(String username, String exerciseType, int duration, int caloriesBurned) {
        this.username = username;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



    public WorkoutLog(String username, String exerciseType, int duration, int caloriesBurned, String timestamp) {
        this.username = username;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.timestamp = timestamp;
    }

    // getter methods

    public String getUsername() {
        return username;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public int getDuration() {
        return duration;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // format of how workout log is displayed

    public String toTextFormat() {
        return username + "," + exerciseType + "," + duration + "," + caloriesBurned + "," + timestamp;
    }

    @Override
    public String toString() {
        return "Exercise: " + exerciseType + ", Duration: " + duration + " mins, Calories: " + caloriesBurned + ", Date: " + timestamp + "\n——————\n";
    }
}