package at.ac.fhcampuswien.bouncingballs.controllers;

import at.ac.fhcampuswien.bouncingballs.balls.InfectableBall;
import at.ac.fhcampuswien.bouncingballs.params.GraphCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    GridPane containerleft;

    List<InfectableBall> balls = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.simulation = new Canvas(SimulationCanvasParams.getWidth(),SimulationCanvasParams.getHeight());
        this.simulationGC=this.simulation.getGraphicsContext2D();
        this.graph = new Canvas(GraphCanvasParams.getWidth(),GraphCanvasParams.getHeight());
        this.graphContainer.getChildren().add(this.graph);
        GridPane.setValignment(graphContainer, VPos.BOTTOM);
        this.graphGC=this.graph.getGraphicsContext2D();
        this.simulationGC.setFill(Color.BLACK);
        this.graphGC.setFill(Color.RED);
        this.borderPane.setRight(this.simulation);



        //this.simulationGC.fillRect(0,0,10000,10000);
        this.graphGC.fillRect(0,0,10000,10000);
        this.initializeBalls();
        this.simulationTimer();
    }
    int x=0;

    public void simulationTimer(){
        final long startNanoTime = System.nanoTime();
        new AnimationTimer(){
            @Override
            public void handle(long currentNanoTime) {

                simulationGC.clearRect(0,0,1000,1000);
                for(InfectableBall el : balls){
                    simulationGC=el.draw(simulationGC);
                    el.move(0.1);
                }
            }
        }.start();
    }
    public void initializeBalls(){
        for(int c =0;c<SimulationValues.getBallCount();c++){
            this.balls.add(new InfectableBall());
        }
        int c=0;
        for(InfectableBall el : balls){
            System.out.println(c+" "+el.print());
            System.out.println("))))");
        }
    }

}
