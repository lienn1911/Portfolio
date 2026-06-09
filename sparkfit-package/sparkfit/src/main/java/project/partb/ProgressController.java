package project.partb;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import project.partb.handleData.DataFilter;
import project.partb.handleData.DataParser;
import project.partb.handleData.DataReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;


public class ProgressController {

    private MainApplication mainApp;

    @FXML
    private PieChart pieChartSleep;

    @FXML
    private PieChart pieChartSteps;

    @FXML
    private PieChart pieChartCalories;

    @FXML
    private PieChart pieChartWeight;

    //pie chart goals

    @FXML private Label calorieGoal, weightGoal, stepsGoal, sleepGoal;

    @FXML
    private TextField textFieldCalories;

    @FXML
    private TextField textFieldWeight;

    @FXML
    private TextField textFieldSleep;

    @FXML
    private TextField textFieldSteps;

    @FXML
    private LineChart<String, Number> lineChart1;
    @FXML
    private LineChart<String, Number> lineChart2;
    @FXML
    private LineChart<String, Number> lineChart3;
    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private DatePicker toDate, fromDate;

    @FXML
    private Button btnOK;

    @FXML private Button button;

    @FXML private JFXButton checkProgress;



    private DataReader dataReader = new DataReader();
    private DataFilter dataFilter = new DataFilter();
    private DataParser dataParser = new DataParser();

    UserSession session = UserSession.getInstance();
    private static final String PROGRESS_FILE = "daily_progress.txt";
    private static final String GOALS_FILE = "user_goals.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    private MenuItem menuHome, menuExercise, menuNutrition, menuGoal, menuProgress, menuProfile;

    @FXML
    public void initialize() {
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


        // save daily progress
        button.setOnAction(e -> save());

        //load today's progress
        checkProgress.setOnAction(e -> loadProgressForToday());

        //overall progress
        btnOK.setOnAction(e -> updateCharts());
    }

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }


    // save the daily progress

    @FXML
    private void save() {

        // to get username of user logged in
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        String caloriesStr = textFieldCalories.getText();
        String weightStr = textFieldWeight.getText();
        String sleepStr = textFieldSleep.getText();
        String stepsStr = textFieldSteps.getText();

        if (caloriesStr.isEmpty() || weightStr.isEmpty() || sleepStr.isEmpty() || stepsStr.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        if (!isNumeric(caloriesStr) || !isNumeric(weightStr) || !isNumeric(sleepStr) || !isNumeric(stepsStr)) {
            showAlert("Error", "All values must be numbers!");
            return;
        }

        int calories = Integer.parseInt(caloriesStr);
        int weight = Integer.parseInt(weightStr);
        int sleep = Integer.parseInt(sleepStr);
        int steps = Integer.parseInt(stepsStr);

        LocalDate today = LocalDate.now();
        String dateStr = today.format(DATE_FORMATTER);

        Map<String, Integer> progressData = new HashMap<>();
        progressData.put("calories", calories);
        progressData.put("weight", weight);
        progressData.put("sleep", sleep);
        progressData.put("steps", steps);

        saveProgress(username, dateStr, progressData);
        updatePieCharts(progressData);
    }

    private void saveProgress(String username, String date, Map<String, Integer> progressData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROGRESS_FILE, true))) {
            writer.write(username + "," + date + "," + progressData.get("calories") + "," + progressData.get("weight") + "," + progressData.get("sleep") + "," + progressData.get("steps"));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // load progress to pie chart for today

    private void loadProgressForToday() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert("Error", "No user logged in.");
            return;
        }

        String username = session.getUsername();
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DATE_FORMATTER);

        try (BufferedReader reader = new BufferedReader(new FileReader(PROGRESS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6 && parts[0].equals(username) && parts[1].equals(dateStr)) {
                    Map<String, Integer> progressData = new HashMap<>();
                    progressData.put("calories", Integer.parseInt(parts[2]));
                    progressData.put("weight", Integer.parseInt(parts[3]));
                    progressData.put("sleep", Integer.parseInt(parts[4]));
                    progressData.put("steps", Integer.parseInt(parts[5]));
                    updatePieCharts(progressData);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showAlert("Info", "No Workouts Today!");
    }

    // update DAILY progress pie charts

    private void updatePieCharts(Map<String, Integer> progressData) {
        Map<String, Integer> goalsData = loadGoals();

        // Update Calories Pie Chart
        pieChartCalories.getData().clear();
        if (progressData.containsKey("calories") && goalsData.containsKey("calories")) {
            int caloriesBurned = progressData.get("calories");
            int caloriesGoal = goalsData.get("calories");
            PieChart.Data burnedData = new PieChart.Data("Calories Burned", caloriesBurned);
            PieChart.Data goalData = new PieChart.Data("Progress Left", Math.max(0, caloriesGoal - caloriesBurned));
            pieChartCalories.getData().addAll(burnedData, goalData);
            addTooltip(burnedData);
            addTooltip(goalData);
        } else {
            System.out.println("Calories data is missing.");
        }

        // Update Weight Pie Chart
        pieChartWeight.getData().clear();
        if (progressData.containsKey("weight") && goalsData.containsKey("weight")) {
            int weight = progressData.get("weight");
            int weightGoal = goalsData.get("weight");
            PieChart.Data weightData = new PieChart.Data("Body Weight", weight);
            PieChart.Data goalData = new PieChart.Data("Progress Left", Math.max(0, weightGoal - weight));
            pieChartWeight.getData().addAll(weightData, goalData);
            addTooltip(weightData);
            addTooltip(goalData);
        } else {
            System.out.println("Weight data is missing.");
        }

        // Update Sleep Pie Chart
        pieChartSleep.getData().clear();
        if (progressData.containsKey("sleep") && goalsData.containsKey("sleep")) {
            int sleep = progressData.get("sleep");
            int sleepGoal = goalsData.get("sleep");
            PieChart.Data sleepData = new PieChart.Data("Sleep", sleep);
            PieChart.Data goalData = new PieChart.Data("Progress Left", Math.max(0, sleepGoal - sleep));
            pieChartSleep.getData().addAll(sleepData, goalData);
            addTooltip(sleepData);
            addTooltip(goalData);
        } else {
            System.out.println("Sleep data is missing.");
        }

        // Update Steps Pie Chart
        pieChartSteps.getData().clear();
        if (progressData.containsKey("steps") && goalsData.containsKey("steps")) {
            int steps = progressData.get("steps");
            int stepsGoal = goalsData.get("steps");
            PieChart.Data stepsData = new PieChart.Data("Steps", steps);
            PieChart.Data goalData = new PieChart.Data("Progress Left", Math.max(0, stepsGoal - steps));
            pieChartSteps.getData().addAll(stepsData, goalData);
            addTooltip(stepsData);
            addTooltip(goalData);
        } else {
            System.out.println("Steps data is missing.");
        }

        //update goal labels
        calorieGoal.setText(progressData.get("calories") + " kcal");
        weightGoal.setText(progressData.get("weight") + " kg");
        sleepGoal.setText(progressData.get("sleep") + " hours");
        stepsGoal.setText(progressData.get("steps") + " steps");
    }


    // Method to add tooltips to pie chart data
    private void addTooltip(PieChart.Data data) {
        data.getNode().addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Tooltip tooltip = new Tooltip(data.getName() + ": " + data.getPieValue());
                Tooltip.install(data.getNode(), tooltip);
            }
        });
    }

    private Map<String, Integer> loadGoals() {
        Map<String, Integer> goalsData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(GOALS_FILE))) {
            String line;
            if ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    goalsData.put("calories", Integer.parseInt(parts[1]));
                    goalsData.put("weight", Integer.parseInt(parts[2]));
                    goalsData.put("steps", Integer.parseInt(parts[3]));
                    goalsData.put("sleep", Integer.parseInt(parts[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return goalsData;
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }


    // For Overall Progress

    // showing overall progress charts
    @FXML
    private void updateCharts() {
        String loggedInUser = session.getUsername();

        List<String[]> data = dataReader.readData(PROGRESS_FILE);
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

        // Set the charts data
        lineChart1.setData(FXCollections.observableArrayList(chartData.get(0))); // Steps
        barChart.setData(FXCollections.observableArrayList(chartData.get(1)));   // Calories
        lineChart2.setData(FXCollections.observableArrayList(chartData.get(2))); // Sleep
        lineChart3.setData(FXCollections.observableArrayList(chartData.get(3))); // Weight
    }

    //format of alert for certain methods that use it

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
