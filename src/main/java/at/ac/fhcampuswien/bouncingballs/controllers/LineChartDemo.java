/*
package at.ac.fhcampuswien.bouncingballs.controllers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.scene.chart.LineChart;

public class LineChartDemo extends Application  {
    public void start(Stage primaryStage){
        try {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("time");
            final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

            XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
            series.setName("Test");
            series.getData().add(new XYChart.Data<String, Number>("Jan", 1000));
            series.getData().add(new XYChart.Data<String, Number>("Feb", 1500));
            series.getData().add(new XYChart.Data<String, Number>("Mar", 2500));
            series.getData().add(new XYChart.Data<String, Number>("Apr", 5000));

            Scene scene = new Scene(lineChart, 640, 480);
            lineChart.getData().add(series);

            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        }
    }


 */
