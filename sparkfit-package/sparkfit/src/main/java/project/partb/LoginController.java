package project.partb;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoginController {

    private MainApplication mainApp;

    @FXML
    private JFXToggleButton loginToggle;

    @FXML
    private JFXButton loginButton;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private TextField loginUsername;

    @FXML
    private Label loginMessage;

    @FXML
    private JFXButton loginExit;


    @FXML
    public void initialize() {
        // Initialize additional components or listeners
        loginToggle.setOnAction(e -> mainApp.switchToSignup());
        loginButton.setOnAction(e -> {
            try {
                handleLogin();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        loginExit.setOnAction(e-> Platform.exit());
    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    public void resetToggleButton() {
        loginToggle.setSelected(false); // Set to default selected state
    }

    private void handleLogin() throws IOException {
        String username = loginUsername.getText();
        String password = loginPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            loginMessage.setText("Please enter both username and password.");
            return;
        }

        List<User> userList = loadUserData();
        boolean loginSuccess = false;
        String email = null;

        for (User user : userList) {
            if ((user.getUsername().equals(username) || user.getEmail().equals(username))
                    && user.getPassword().equals(password)) {
                loginSuccess = true;
                email = user.getEmail();
                break;
            }
        }

        if (loginSuccess) {
            UserSession.getInstance(username, email); // Create a session
            loginMessage.setText("Login Successful");
            mainApp.switchToHome();
        } else {
            loginMessage.setText("Invalid username or password.");
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


}
