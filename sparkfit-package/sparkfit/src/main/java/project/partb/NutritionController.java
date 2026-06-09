package project.partb;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NutritionController {

    private MainApplication mainApp;

    @FXML private DatePicker date;
    @FXML private ComboBox<String> mealTime;
    @FXML private TextField foodItem;
    @FXML private Spinner<Integer> portionSize;
    @FXML private TextField calories;
    @FXML private TextField proteins;
    @FXML private TextField carbohydrates;
    @FXML private TextField fats;
    @FXML private DatePicker summaryDate;
    @FXML private PieChart pieChart;
    @FXML private VBox summary;
    @FXML private Label labelWarning;
    @FXML private Label incorrectValue;
    @FXML private DatePicker dateFilter1;
    @FXML private DatePicker dateFilter2;
    @FXML private ListView<String> historyList;
    @FXML private Label summaryWarning;

    @FXML
    private MenuItem menuHome, menuExercise, menuNutrition, menuGoal, menuProgress, menuProfile;

    private static final Logger logger = Logger.getLogger(NutritionController.class.getName());


    // create list for saved logged meals data
    private final List<MealLog> mealLogs = new ArrayList<>();

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    //initialize meal time and portion size
    @FXML
    private void initialize() {
        System.out.println("Initializing Meal Time ComboBox...");
        mealTime.setItems(FXCollections.observableArrayList("Breakfast", "Lunch", "Dinner", "Snack"));
        portionSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        System.out.println("Initialization complete: Meal Time options set.");


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

    //log meals methods
    @FXML
    private void logMeals(ActionEvent actionEvent) {
        try {
            if (date.getValue() == null || mealTime.getValue() == null || foodItem.getText().isEmpty() ||
                    calories.getText().isEmpty() || proteins.getText().isEmpty() ||
                    carbohydrates.getText().isEmpty() || fats.getText().isEmpty()) {
                incorrectValue.setText("Please fill in all fields.");
                return;
            }

            LocalDate mealDate = date.getValue();
            String meal = mealTime.getValue();
            String food = foodItem.getText();
            int portion = portionSize.getValue();
            double cal = Double.parseDouble(calories.getText());
            double prot = Double.parseDouble(proteins.getText());
            double carbs = Double.parseDouble(carbohydrates.getText());
            double fat = Double.parseDouble(fats.getText());

            incorrectValue.setText("");

            // Add the logged meal data to the list
            String username = UserSession.getInstance().getUsername();
            MealLog mealLog = new MealLog(mealDate, username, meal, food, portion, cal, prot, carbs, fat);
            mealLogs.add(mealLog);

            // Save to file
            saveMealLogs();

            System.out.println("Meal logged: " + mealLog);

            updateHistoryList();
            clearInputFields();

        } catch (NumberFormatException e) {
            incorrectValue.setText("Please enter valid numerical values for calories, proteins, carbohydrates, and fats.");
        }
    }



    //daily summary
    @FXML
    private void showSummary(ActionEvent event) {
        LocalDate selectedDate = summaryDate.getValue();
        if (selectedDate == null) {
            summaryWarning.setText("Please select a date to show the summary.");
            return;
        }

        // Load meal logs from file before showing the summary
        loadMealLogsForCurrentUser();

        updateDailySummary(selectedDate);
    }

    private void saveMealLogs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("meals.txt"))) {
            oos.writeObject(mealLogs);
        } catch (IOException e) {
            incorrectValue.setText("Error saving meal logs. Please try again.");
            logger.severe("Failed to save meal logs: " + e.getMessage());
        }
    }

    private void loadMealLogsForCurrentUser() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("meals.txt"))) {
            mealLogs.clear();
            mealLogs.addAll((List<MealLog>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            summaryWarning.setText("Error loading previous meals. Please try again.");
            logger.severe("Failed to load meal logs: " + e.getMessage());
        }
    }



    private void updateDailySummary(LocalDate selectedDate) {
        summary.getChildren().clear();

        // Filter logs for the selected date
        List<MealLog> logsForDate = mealLogs.stream()
                .filter(log -> log.date().equals(selectedDate))
                .collect(Collectors.toList());

        if (logsForDate.isEmpty()) {
            Label noDataLabel = new Label("No meals logged for " + selectedDate + ".");
            noDataLabel.setStyle("-fx-font-size: 16; -fx-text-fill: gray;");
            summary.getChildren().add(noDataLabel);
            return;
        }

        // Build the summary text
        StringBuilder summaryText = new StringBuilder("Summary for " + selectedDate + ":\n");
        double totalCalories = 0, totalProteins = 0, totalCarbohydrates = 0, totalFats = 0;

        for (MealLog log : logsForDate) {
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

            totalCalories += log.calories();
            totalProteins += log.proteins();
            totalCarbohydrates += log.carbohydrates();
            totalFats += log.fats();
        }

        // Display the summary
        Label summaryLabel = new Label(summaryText.toString());
        summaryLabel.setWrapText(true);
        summaryLabel.setStyle("-fx-font-family: 'Berlin Sans FB'; -fx-font-size: 13; -fx-text-fill: #333333;");
        summary.getChildren().add(summaryLabel);

        // Update the PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Calories", totalCalories),
                new PieChart.Data("Proteins", totalProteins),
                new PieChart.Data("Carbohydrates", totalCarbohydrates),
                new PieChart.Data("Fats", totalFats)
        );
        pieChart.setData(pieChartData);
    }




    //history meal logs in a specified data range
    @FXML
    private void filterButton(ActionEvent actionEvent) {
        LocalDate startDate = dateFilter1.getValue();
        LocalDate endDate = dateFilter2.getValue();

        if (startDate == null || endDate == null) {
            labelWarning.setText("Please select both dates.");
            return;
        }
        if (endDate.isBefore(startDate)) {
            labelWarning.setText("End Date cannot be before Start Date.");
            return;
        }

        // Load meal logs from file before filtering
        loadMealLogsForCurrentUser();

        filterHistory(startDate, endDate);
    }


    private void filterHistory(LocalDate startDate, LocalDate endDate) {
        List<MealLog> filteredLogs = mealLogs.stream()
                .filter(log -> !log.date().isBefore(startDate) && !log.date().isAfter(endDate))
                .collect(Collectors.toList());

        ObservableList<String> historyItems = FXCollections.observableArrayList();
        for (MealLog log : filteredLogs) {
            historyItems.add(log.toString());
        }
        historyList.setItems(historyItems);
    }

    private void updateHistoryList() {
        ObservableList<String> historyItems = FXCollections.observableArrayList();
        for (MealLog log : mealLogs) {
            historyItems.add(log.toString());
        }
        historyList.setItems(historyItems);
    }

    //clear input field for user next input
    private void clearInputFields() {
        date.setValue(null);
        mealTime.setValue(null);
        foodItem.clear();
        portionSize.getValueFactory().setValue(1);
        calories.clear();
        proteins.clear();
        carbohydrates.clear();
        fats.clear();
    }


    public record MealLog(LocalDate date, String username, String mealTime, String foodItem, int portionSize, double calories,
                          double proteins, double carbohydrates, double fats) implements Serializable {

        @Override
            public String toString() {
                return String.format("Date: %s, Meal: %s, Food: %s, Portion: %d, Calories: %.2f, Proteins: %.2f, Carbohydrates: %.2f, Fats: %.2f",
                        date, mealTime, foodItem, portionSize, calories, proteins, carbohydrates, fats);
            }
        }

    public void returnHome(ActionEvent actionEvent){

    }

}