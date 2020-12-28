package at.ac.fhcampuswien.bouncingballs.controllers;

import at.ac.fhcampuswien.bouncingballs.balls.InfectableBall;
import at.ac.fhcampuswien.bouncingballs.balls.InfectableBalls;
import at.ac.fhcampuswien.bouncingballs.balls.InfectionStats;
import at.ac.fhcampuswien.bouncingballs.balls.QuadTree;
import at.ac.fhcampuswien.bouncingballs.params.GraphCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import at.ac.fhcampuswien.bouncingballs.shapes.Circle;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import at.ac.fhcampuswien.bouncingballs.shapes.Rectangle;
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

    //List<InfectableBall> balls = new ArrayList<>();
    InfectableBalls balls = new InfectableBalls();

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
        this.graphGC.fillRect(0,0,SimulationCanvasParams.getWidth(),SimulationCanvasParams.getHeight());
        //this.initializeBalls();
        this.balls.generateBalls();
        this.simulationTimer();
    }
    int x=0;

    public void simulationTimer(){
        final long startNanoTime = System.nanoTime();
        new AnimationTimer(){
            @Override
            public void handle(long currentNanoTime) {

                //Clear canvas, set BackgroundColor
                simulationGC.clearRect(0,0,1000,1000);
                simulationGC.setFill(Color.BLACK);
                simulationGC.fillRect(0,0,1000,1000);

                //handles everything sorrounding the Infectable balls
                simulationGC = balls.drawAndHandleTimestep(simulationGC,currentNanoTime);


                //Quadtree Test
                double x,y,w,h;
                x=SimulationCanvasParams.getWidth()/2;
                y=SimulationCanvasParams.getHeight()/2;
                w=20;
                h=20;
                double r=100;
                Circle searchCirc = new Circle(x,y,r);

                Rectangle searchRange = new Rectangle(x,y,w,h);
                simulationGC.setStroke(Color.ROSYBROWN);
               // simulationGC.strokeRect(x-w,y-h,w*2,h*2);
                simulationGC.fillOval(x-r,y-r,r*2,r*2);
                for(Point p: balls.tree.query(searchCirc)){
                    simulationGC.setFill(Color.BLUEVIOLET);
                    double radius = 5;
                    simulationGC.fillOval(p.x-radius,p.y-radius,radius*2,radius*2);
                }
               // InfectionStats.infektionsgeschehen();

               // InfectionStats.printCurStats();
            }
        }.start();
    }

}
