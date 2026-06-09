package project.partb;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewGoalController {

    // to get methods from the main app

    private MainApplication mainApp;

    @FXML
    private Label calories;

    @FXML
    private Label weight;

    @FXML
    private Label steps;

    @FXML
    private Label sleep;

    @FXML
    private Label deadline;

    @FXML
    private Label countdown;

    @FXML
    private JFXButton goalAchieved, resetGoal;

    @FXML
    private MenuItem menuHome, menuExercise, menuNutrition, menuGoal, menuProgress, menuProfile;

    // relevant files

    private static final String GOALS_FILE = "user_goals.txt";
    private static final String PROFILE_FILE = "user_profile.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {

        // loading the current goals set when screen is switched to view goals
        loadGoals();

        // menu items
        menuHome.setOnAction(e -> {
            try{
                mainApp.switchToHome();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuProgress.setOnAction(e -> {
            try {
                mainApp.switchToProgress();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuExercise.setOnAction(e -> {
            try {
                mainApp.switchToExerciseLog();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuNutrition.setOnAction(e -> {
            try {
                mainApp.switchToNutrition();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuGoal.setOnAction(e -> {
            try {
                mainApp.switchToGoal();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        menuProfile.setOnAction(e -> {
            try {
                mainApp.switchToProfile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // button functions
        goalAchieved.setOnAction(e -> goalAchieved());
        resetGoal.setOnAction(e -> resetGoal());
    }

    private void loadGoals() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "User Sync Error Occured. Cannot Load Goals");
            return;
        }

        String username = session.getUsername();

        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6 && parts[0].equals(username)) {
                    Map<String, String> goalsData = new HashMap<>();
                    goalsData.put("calories", parts[1]);
                    goalsData.put("weight", parts[2]);
                    goalsData.put("steps", parts[3]);
                    goalsData.put("sleep", parts[4]);
                    goalsData.put("deadline", parts[5]);

                    displayGoals(goalsData);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert("Info", "No goals found for the user.");
    }

    private void displayGoals(Map<String, String> goalsData) {
        calories.setText(goalsData.get("calories"));
        weight.setText(goalsData.get("weight"));
        steps.setText(goalsData.get("steps"));
        sleep.setText(goalsData.get("sleep"));

        LocalDate deadlineDate = LocalDate.parse(goalsData.get("deadline"), DATE_FORMATTER);
        deadline.setText(deadlineDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), deadlineDate);
        countdown.setText(String.valueOf(daysLeft));
    }

    private void goalAchieved() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        removeGoal(username);
        incrementGoalCounter(username);
    }

    private void resetGoal() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        removeGoal(username);
    }

    private void removeGoal(String username) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (!parts[0].equals(username)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GOALS_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert("Success", "Goal removed successfully!");
        clearGoalDisplay();
    }

    private void incrementGoalCounter(String username) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    int goalCounter = Integer.parseInt(parts[7]) + 1;
                    lines.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + parts[5] + "," + parts[6] + "," + goalCounter);
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROFILE_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert("Success", "Goal achieved and counter updated!");
    }

    private void clearGoalDisplay() {
        calories.setText("0");
        weight.setText("0");
        steps.setText("0");
        sleep.setText("0");
        deadline.setText("DD-MM-YYYY");
        countdown.setText("-");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
