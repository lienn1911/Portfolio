package project.partb;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.DatePicker;

import project.partb.handleData.DataReader;
import project.partb.handleData.DataFilter;
import project.partb.handleData.DataParser;

public class HomeController {

    private MainApplication mainApp;

    @FXML
    private Button btnOK;

    @FXML
    private Label date;

    @FXML
    private JFXButton exerciseLog, mealLog;

    @FXML
    private DatePicker fromDate;

    @FXML
    private Label label;

    @FXML
    private Label labelCalories;

    @FXML
    private Label labelFromDate;

    @FXML
    private Label labelSleep;

    @FXML
    private Label labelSteps;

    @FXML
    private Label labelToDate;

    @FXML
    private Label labelWeight;

    @FXML
    private LineChart<String, Number> lineChart1;
    @FXML
    private LineChart<String, Number> lineChart2;
    @FXML
    private LineChart<String, Number> lineChart3;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML private VBox summary;

    private final List<NutritionController.MealLog> mealLogs = new ArrayList<>();

    @FXML
    private MenuItem menuLogout, menuHome, menuProgress, menuExercise, menuNutrition, menuGoal, menuProfile;

    @FXML
    private Label time;

    @FXML
    private DatePicker toDate;

    @FXML
    private Text username;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxisSleep;

    @FXML
    private NumberAxis yAxisSteps;

    @FXML
    private NumberAxis yAxisWeight;

    @FXML
    private NumberAxis yAxyAxisisCalories;

    @FXML
    private TextArea logsArea;

    private static final String TEXT_FILE = "workout_logs.txt";
    private DataReader dataReader = new DataReader();
    private DataFilter dataFilter = new DataFilter();
    private DataParser dataParser = new DataParser();

    LocalDate selectedDate = LocalDate.now();

    UserSession session = UserSession.getInstance();
    private static final String DAILY_PROGRESS = "daily_progress.txt";
    String loggedInUser = session.getUsername();

    @FXML
    public void initialize() {
        timeNow();
        currentUser();
        btnOK.setOnAction(e -> updateCharts());

        menuLogout.setOnAction(e -> {
                    handleLogout();
                    mainApp.switchToLogin();
        });
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
        displayTodayLogs();

        //button to log exercise screen
        exerciseLog.setOnAction(e -> {
            try {
                mainApp.switchToExerciseLog();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        //button to nutrition screen
        mealLog.setOnAction(e -> updateDailySummary());


    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    private void currentUser(){
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }
        String currentUser = session.getUsername();
        username.setText(currentUser);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void timeNow() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                final String timenow = timeFormat.format(new Date());
                final String datenow = dateFormat.format(new Date());
                Platform.runLater(() -> {
                    time.setText(timenow);
                    date.setText(datenow);
                });
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void handleLogout() {
        UserSession session = UserSession.getInstance();
        if (session != null) {
            session.clearSession();
        }
        mainApp.switchToLogin();
    }

    private void displayTodayLogs() {
        List<WorkoutLog> todayLogs = getTodayLogs();
        if (todayLogs.isEmpty()) {
            logsArea.setText("No Workouts Today!");
        } else {
            StringBuilder sb = new StringBuilder();
            for (WorkoutLog log : todayLogs) {
                sb.append(log.toString()).append("\n");
            }
            logsArea.setText(sb.toString());
        }
    }

    private List<WorkoutLog> getTodayLogs() {
        List<WorkoutLog> todayLogs = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    LocalDateTime logDateTime = LocalDateTime.parse(parts[4], formatter);
                    if (logDateTime.toLocalDate().equals(today)) {
                        todayLogs.add(new WorkoutLog(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), parts[4]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayLogs;
    }


    // showing progress charts
    @FXML
    private void updateCharts() {
        String loggedInUser = session.getUsername();

        List<String[]> data = dataReader.readData(DAILY_PROGRESS);
        List<String[]> filteredData = dataFilter.filterDataByUser(data, loggedInUser);

        LocalDate startDate = fromDate.getValue();
        LocalDate endDate = toDate.getValue();

        if (startDate != null && endDate != null) {
            filteredData = filteredData.stream()
                    .filter(record -> {
                        LocalDate date;
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            date = LocalDate.parse(record[1], formatter);
                        } catch (DateTimeParseException e) {
                            e.printStackTrace(); // Log the error for debugging
                            return false; // Skip invalid dates
                        }
                        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                                (date.isEqual(endDate) || date.isBefore(endDate));
                    })
                    .collect(Collectors.toList());
        }

        ObservableList<XYChart.Series<String, Number>> chartData = dataParser.parseData(filteredData);

        // Set the data
        lineChart1.setData(FXCollections.observableArrayList(chartData.get(0))); // Steps
        barChart.setData(FXCollections.observableArrayList(chartData.get(1)));   // Calories
        lineChart2.setData(FXCollections.observableArrayList(chartData.get(2))); // Sleep
        lineChart3.setData(FXCollections.observableArrayList(chartData.get(3))); // Weight
    }

    private void updateDailySummary() {
        // Clear the VBox to prepare for new summary content
        summary.getChildren().clear();

        // Filter meal logs for the selected date
        List<NutritionController.MealLog> logsForDate = mealLogs.stream()
                .filter(log -> log.date().equals(selectedDate))
                .collect(Collectors.toList());

        if (logsForDate.isEmpty()) {
            // If selected date didnt have any data , display a message for user
            Label noDataLabel = new Label("No meals logged for " + selectedDate + ".");
            noDataLabel.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            summary.getChildren().add(noDataLabel);


        }

        // create for daily paragraph summary
        StringBuilder summaryText = new StringBuilder("Summary for " + selectedDate + ":\n");

        // Initialize totals for the pie chart
        double totalCalories = 0;
        double totalProteins = 0;
        double totalCarbohydrates = 0;
        double totalFats = 0;


        for (NutritionController.MealLog log : logsForDate) {
            summaryText.append("- ")
                    .append(log.mealTime())
                    .append(": You ate ")
                    .append(log.foodItem())
                    .append(", portion size: ")
                    .append(log.portionSize())
                    .append(", protein: ")
                    .append(String.format("%.2f", log.proteins()))
                    .append("g, fats: ")
                    .append(String.format("%.2f", log.fats()))
                    .append("g, carbohydrates: ")
                    .append(String.format("%.2f", log.carbohydrates()))
                    .append("g, calories: ")
                    .append(String.format("%.2f", log.calories()))
                    .append(" kcal.\n\n");


            // Update totals for pie chart
            totalCalories += log.calories();
            totalProteins += log.proteins();
            totalCarbohydrates += log.carbohydrates();
            totalFats += log.fats();
        }

    }

}