package project.partb;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class FitnessGoalController {

    private MainApplication mainApp;


    @FXML
    private Label goalCalories;

    @FXML
    private Label goalWeight;

    @FXML
    private Label goalSteps;

    @FXML
    private Label goalSleep;

    @FXML
    private DatePicker goalDate;

    @FXML
    private Button setGoals;

    @FXML
    private Button increaseCalories;

    @FXML
    private Button decreaseCalories;

    @FXML
    private Button increaseWeight;

    @FXML
    private Button decreaseWeight;

    @FXML
    private Button increaseSteps;

    @FXML
    private Button decreaseSteps;

    @FXML
    private Button increaseSleep;

    @FXML
    private Button decreaseSleep;

    @FXML
    private Button viewGoals;

    @FXML
    private MenuItem menuHome, menuExercise, menuNutrition, menuGoal, menuProgress, menuProfile;

    private static final String GOALS_FILE = "user_goals.txt";

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {

        //increase or decrease values
        increaseCalories.setOnAction(e -> adjustGoal(goalCalories, 100));
        decreaseCalories.setOnAction(e -> adjustGoal(goalCalories, -100));
        increaseWeight.setOnAction(e -> adjustGoal(goalWeight, 1));
        decreaseWeight.setOnAction(e -> adjustGoal(goalWeight, -1));
        increaseSteps.setOnAction(e -> adjustGoal(goalSteps, 100));
        decreaseSteps.setOnAction(e -> adjustGoal(goalSteps, -100));
        increaseSleep.setOnAction(e -> adjustGoal(goalSleep, 1));
        decreaseSleep.setOnAction(e -> adjustGoal(goalSleep, -1));
        setGoals.setOnAction(e -> saveGoals());

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
                System.out.println("An error occurred");
                ex.printStackTrace();
                // what is goin on lol
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

        // view goals after setting them
        viewGoals.setOnAction(e -> {
            try {
                mainApp.switchToViewGoal();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    private void adjustGoal(Label goalLabel, int adjustment) {
        int currentGoal = Integer.parseInt(goalLabel.getText());
        int newGoal = currentGoal + adjustment;
        if (newGoal >= 0) {
            goalLabel.setText(String.valueOf(newGoal));
        }
    }

    private void saveGoals() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        String calories = goalCalories.getText();
        String weight = goalWeight.getText();
        String steps = goalSteps.getText();
        String sleep = goalSleep.getText();
        LocalDate deadline = goalDate.getValue();

        if (calories.isEmpty() || weight.isEmpty() || steps.isEmpty() || sleep.isEmpty() || deadline == null) {
            showAlert("Error", "All fields are required!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GOALS_FILE, true))) {
            writer.write(username + "," + calories + "," + weight + "," + steps + "," + sleep + "," + deadline.toString());
            writer.newLine();
            showAlert("Success", "Goals saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving the goals.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
