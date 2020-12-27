package at.ac.fhcampuswien.bouncingballs.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable  {
    @FXML
    BorderPane borderPane;
    @FXML
    VBox graphContainer;

    Canvas simulation;
    GraphicsContext simulationGC;

    @FXML
    Canvas graph;
    GraphicsContext graphGC;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.simulation = new Canvas(500,500);
        this.simulationGC=this.simulation.getGraphicsContext2D();
        this.graph = new Canvas(500,500);
        this.graphGC=this.graph.getGraphicsContext2D();
        this.simulationGC.setFill(Color.BLACK);
        this.graphGC.setFill(Color.RED);
        this.borderPane.setRight(this.simulation);

        this.graphContainer.getChildren().add(this.graph);

        this.simulationGC.fillRect(0,0,10000,10000);
        this.graphGC.fillRect(0,0,10000,10000);

    }
}
