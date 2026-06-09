package project.partb;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.Integer.parseInt;

public class UserProfileController {

    private MainApplication mainApp;

    @FXML
    private TextField nameField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField heightField;

    @FXML
    private TextField countryField;

    @FXML
    private DatePicker dobField;

    @FXML
    private Label summaryName;

    @FXML
    private Label summaryEmail;

    @FXML
    private Label summaryDOB;

    @FXML
    private Label summaryWeight;

    @FXML
    private Label summaryHeight;

    @FXML
    private Label summaryCountry;

    @FXML
    private Label summaryUsername;

    @FXML
    private Label goalCounter;

    @FXML
    private MenuItem menuHome, menuExercise, menuNutrition, menuGoal, menuProgress, menuProfile;

    private static final String PROFILE_FILE = "user_profile.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        loadUserProfile();
        loadGoalCounter();

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

    @FXML
    private void handleSaveChanges() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        String email = session.getEmail();
        String name = nameField.getText();
        String weight = weightField.getText();
        String height = heightField.getText();
        String country = countryField.getText();
        LocalDate dob = dobField.getValue();
        int goalCount = loadGoalCounter();

        if (name.isEmpty()|| weight.isEmpty() || height.isEmpty() || country.isEmpty() || dob == null) {
            showAlert("Error", "All fields are required!");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROFILE_FILE, true))) {
            writer.write(username + "," + name + "," + email + "," + weight + "," + height + "," + country + "," + dob.format(DATE_FORMATTER) + "," + goalCount );
            writer.newLine();
            showAlert("Success", "Profile updated successfully!");
            updateSummary(username, name, email, weight, height, country, dob);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while saving the profile.");
        }
    }

    private void loadUserProfile() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        String primaryEmail = session.getEmail();
        summaryUsername.setText("@" + username);
        summaryEmail.setText(primaryEmail);

        try (BufferedReader reader = new BufferedReader(new FileReader(PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[2].equals(primaryEmail)) {
                    String name = parts[1];
                    String weight = parts[3];
                    String height = parts[4];
                    String country = parts[5];
                    LocalDate dob = LocalDate.parse(parts[6], DATE_FORMATTER);

                    nameField.setText(name);
                    weightField.setText(weight);
                    heightField.setText(height);
                    countryField.setText(country);
                    dobField.setValue(dob);

                    updateSummary(username, name, primaryEmail, weight, height, country, dob);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSummary(String username, String name, String email, String weight, String height, String country, LocalDate dob) {
        summaryName.setText(name);
        summaryEmail.setText(email);
        summaryWeight.setText(weight + " kg");
        summaryHeight.setText(height + " cm");
        summaryCountry.setText(country);
        summaryDOB.setText(dob.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int loadGoalCounter() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return 0;
        }

        String username = session.getUsername();

        try (BufferedReader reader = new BufferedReader(new FileReader(PROFILE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8 && parts[0].equals(username)) {
                    String goalCount = parts[7];

                    goalCounter.setText(goalCount);

                    return parseInt(goalCount);
                } else {
                    goalCounter.setText("0");
                    return 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}