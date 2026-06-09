package project.partb;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    // Scenes
    private Scene loginScene;
    private Scene signupScene;
    private Scene homeScene;
    private Scene progressScene;
    private Scene workoutScene;
    private Scene nutritionScene;
    private Scene goalScene;
    private Scene viewGoalScene;
    private Scene profileScene;


    //Stage
    private Stage primaryStage;

    // Controllers
    private LoginController loginController;
    private SignupController signupController;
    private HomeController homeController;
    private ProgressController progressController;
    private WorkoutLoggerController workoutLoggerController;
    private NutritionController nutritionController;
    private FitnessGoalController fitnessGoalController;
    private UserProfileController userProfileController;
    private ViewGoalController viewGoalController;

    // The start of the application
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // Set the application title and centre

        primaryStage.setTitle("SparkFit. — Spark Your Journey To Fitness");

        // Load the FXML files for the scenes
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginPage = loginLoader.load();
        loginScene = new Scene(loginPage);

        FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("signup.fxml"));
        Parent signupPage = signupLoader.load();
        signupScene = new Scene(signupPage);

        // Get controller instances and set up references for scene switching
        loginController = loginLoader.getController();
        loginController.setMainApp(this);

        signupController = signupLoader.getController();
        signupController.setMainApp(this);


        // Set the first scene of the app and show
        primaryStage.setScene(loginScene); // Start with login scene
        primaryStage.show();
    }

    // Methods to switch scenes
    public void switchToSignup() {
        signupController.resetToggleButton();
        primaryStage.setScene(signupScene);
        primaryStage.centerOnScreen();
    }

    public void switchToLogin() {
        loginController.resetToggleButton();
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
    }

    public void switchToHome() throws IOException {

        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("Home.fxml"));
        Parent homePage = homeLoader.load();
        homeScene = new Scene(homePage);

        homeController = homeLoader.getController();
        homeController.setMainApp(this);

        primaryStage.setScene(homeScene);
        primaryStage.centerOnScreen();
    }

    public void switchToProgress() throws IOException {

        FXMLLoader progressLoader = new FXMLLoader(getClass().getResource("progressVisualisation.fxml"));
        Parent progressPage = progressLoader.load();
        progressScene = new Scene(progressPage);

        progressController = progressLoader.getController();
        progressController.setMainApp(this);

        primaryStage.setScene(progressScene);
        primaryStage.centerOnScreen();
    }

    public void switchToExerciseLog() throws IOException {

        FXMLLoader workoutLoader = new FXMLLoader(getClass().getResource("WorkoutLogger.fxml"));
        Parent workoutPage = workoutLoader.load();
        workoutScene = new Scene(workoutPage);

        workoutLoggerController = workoutLoader.getController();
        workoutLoggerController.setMainApp(this);

        primaryStage.setScene(workoutScene);
        primaryStage.centerOnScreen();
    }

    public void switchToNutrition() throws IOException {

        FXMLLoader nutritionLoader = new FXMLLoader(getClass().getResource("nutrition.fxml"));
        Parent nutritionPage = nutritionLoader.load();
        nutritionScene = new Scene(nutritionPage);

        nutritionController = nutritionLoader.getController();
        nutritionController.setMainApp(this);

        primaryStage.setScene(nutritionScene);
        primaryStage.centerOnScreen();
    }

    public void switchToGoal() throws IOException {

        FXMLLoader goalLoader = new FXMLLoader(getClass().getResource("fitnessGoal.fxml"));
        Parent goalPage = goalLoader.load();
        goalScene = new Scene(goalPage);

        fitnessGoalController = goalLoader.getController();
        fitnessGoalController.setMainApp(this);

        primaryStage.setScene(goalScene);
        primaryStage.centerOnScreen();
    }

    public void switchToViewGoal() throws IOException {

        FXMLLoader viewGoalLoader = new FXMLLoader(getClass().getResource("viewGoal.fxml"));
        Parent viewGoalPage = viewGoalLoader.load();
        viewGoalScene = new Scene(viewGoalPage);

        viewGoalController = viewGoalLoader.getController();
        viewGoalController.setMainApp(this);

        primaryStage.setScene(viewGoalScene);
        primaryStage.centerOnScreen();
    }

    public void switchToProfile() throws IOException {

        FXMLLoader profileLoader = new FXMLLoader(getClass().getResource("userprofile.fxml"));
        Parent profilePage = profileLoader.load();
        profileScene = new Scene(profilePage);

        userProfileController = profileLoader.getController();
        userProfileController.setMainApp(this);

        primaryStage.setScene(profileScene);
        primaryStage.centerOnScreen();
    }

    // Main Application Run
    public static void main(String[] args) {
        launch();
    }
}