package project.partb;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WorkoutLoggerController {

    private MainApplication mainApp;

    @FXML
    private TextField exerciseTypeField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField caloriesBurnedField;

    @FXML
    private JFXButton saveButton;

    @FXML
    private JFXButton retrieveButton;

    @FXML
    private TextArea logsArea;

    @FXML
    private MenuItem menuHome, menuExercise, menuNutrition, menuGoal, menuProgress, menuProfile;

    private static final String TEXT_FILE = "workout_logs.txt";
    private static final String BINARY_FILE = "workout_logs.dat";


    @FXML
    private void initialize() {
        saveButton.setOnAction(e -> saveLog());
        retrieveButton.setOnAction(e -> retrieveLogs());


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
    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    private void saveLog() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        String exerciseType = exerciseTypeField.getText();
        String durationStr = durationField.getText();
        String caloriesStr = caloriesBurnedField.getText();

        if (exerciseType.isEmpty() || durationStr.isEmpty() || caloriesStr.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        durationStr = cleanNumber(durationStr);
        caloriesStr = cleanNumber(caloriesStr);

        if (!isNumeric(durationStr)) {
            showAlert("Error", "Duration must be a number!");
            System.out.println("Duration is not numeric: " + durationStr);
            return;
        }

        if (!isNumeric(caloriesStr)) {
            showAlert("Error", "Calories Burned must be a number!");
            System.out.println("Calories is not numeric: " + caloriesStr);
            return;
        }

        int duration = Integer.parseInt(durationStr);
        int caloriesBurned = Integer.parseInt(caloriesStr);

        WorkoutLog log = new WorkoutLog(username, exerciseType, duration, caloriesBurned);

        try {
            saveToTextFile(log);
            saveToBinaryFile(log);

            showAlert("Success", "Log saved successfully!");
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving the log.");
        }
    }

    private String cleanNumber(String str) {
        return str == null ? "" : str.trim().replaceAll("[^0-9]", "");
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    private void saveToTextFile(WorkoutLog log) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_FILE, true))) {
            writer.write(log.toTextFormat());
            writer.newLine();
        }
    }

    private void saveToBinaryFile(WorkoutLog log) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARY_FILE, true))) {
            oos.writeObject(log);
        }
    }

    private void retrieveLogs() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();

        try {
            List<WorkoutLog> logs = loadFromTextFile(username);

            if (logs.isEmpty()) {
                logsArea.setText("No logs found.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (WorkoutLog log : logs) {
                    sb.append(log.toString()).append("\n");
                }
                logsArea.setText(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<WorkoutLog> loadFromTextFile(String username) throws IOException {
        List<WorkoutLog> logs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(username)) {
                    String duration = cleanNumber(parts[2]);
                    String calories = cleanNumber(parts[3]);
                    if (isNumeric(duration) && isNumeric(calories)) {
                        logs.add(new WorkoutLog(parts[0], parts[1], Integer.parseInt(duration), Integer.parseInt(calories)));
                    }
                }
            }
        }
        return logs;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        exerciseTypeField.clear();
        durationField.clear();
        caloriesBurnedField.clear();
    }
}
