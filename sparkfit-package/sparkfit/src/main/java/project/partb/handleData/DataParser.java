package project.partb.handleData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.util.List;

public class DataParser {
    public ObservableList<XYChart.Series<String, Number>> parseData(List<String[]> data) {
        XYChart.Series<String, Number> stepsSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> caloriesSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> sleepSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> weightSeries = new XYChart.Series<>();

        // Set series names for better readability
        stepsSeries.setName("Steps");
        caloriesSeries.setName("Calories");
        sleepSeries.setName("Sleep");
        weightSeries.setName("Weight");

        for (String[] record : data) {
            String date = record[1];
            int calories = Integer.parseInt(record[2]);
            double weight = Double.parseDouble(record[3]);
            double sleep = Double.parseDouble(record[4]);
            int steps = Integer.parseInt(record[5]);

            stepsSeries.getData().add(new XYChart.Data<>(date, steps));
            caloriesSeries.getData().add(new XYChart.Data<>(date, calories));
            sleepSeries.getData().add(new XYChart.Data<>(date, sleep));
            weightSeries.getData().add(new XYChart.Data<>(date, weight));
        }

        ObservableList<XYChart.Series<String, Number>> chartData = FXCollections.observableArrayList();
        chartData.addAll(stepsSeries, caloriesSeries, sleepSeries, weightSeries);
        return chartData;
    }

}
