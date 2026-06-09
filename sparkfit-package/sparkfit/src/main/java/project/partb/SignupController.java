package project.partb;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class SignupController {
    @FXML
    private BorderPane loginPane;

    @FXML
    private JFXToggleButton signUpToggle;

    @FXML
    private JFXButton signupButton;

    @FXML
    private PasswordField signupConfirm;

    @FXML
    private PasswordField signupPassword;

    @FXML
    private TextField signupUsername;

    @FXML
    private Label signupMessage;

    @FXML
    private TextField signupEmail;

    @FXML
    private JFXButton signupExit;

    private MainApplication mainApp;

    @FXML
    public void initialize() {
        // Initialize additional components or listeners
        signupButton.setOnAction(e -> handleSignup());
        signUpToggle.setOnAction(e -> mainApp.switchToLogin());
        signupExit.setOnAction(e-> Platform.exit());
    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    public void resetToggleButton() {
        signUpToggle.setSelected(true); // Set to default selected state
    }

    private void handleSignup() {
        String username = signupUsername.getText();
        String email = signupEmail.getText();
        String password = signupPassword.getText();
        String confirmPassword = signupConfirm.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            signupMessage.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            signupMessage.setText("Passwords do not match.");
            return;
        }

        if (!isUnique(username, email)) {
            signupMessage.setText("Username or email already exists.");
            return;
        }

        // Create a new User object
        User newUser = new User(username, email, password);

        // Save the User object to file
        try {
            List<User> userList = loadUserData(); // Load existing users
            userList.add(newUser); // Add the new user

            // Write updated user list back to file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_data.dat"))) {
                oos.writeObject(userList);
            }

            signupMessage.setText("Signup successful!");
            mainApp.switchToLogin();
        } catch (IOException e) {
            e.printStackTrace();
            signupMessage.setText("An error occurred. Please try again.");
        }
    }

    // Load user data from file
    @SuppressWarnings("unchecked")
    private List<User> loadUserData() {
        List<User> userList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_data.dat"))) {
            userList = (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // File might not exist initially, or be empty
        }
        return userList;
    }

    private boolean isUnique(String username, String email) {
        List<User> userList = loadUserData();
        for (User user : userList) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }





}