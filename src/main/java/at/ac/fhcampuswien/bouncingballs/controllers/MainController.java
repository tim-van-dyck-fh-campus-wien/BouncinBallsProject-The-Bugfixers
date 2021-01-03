package at.ac.fhcampuswien.bouncingballs.controllers;

import at.ac.fhcampuswien.bouncingballs.balls.InfectableBalls;
import at.ac.fhcampuswien.bouncingballs.balls.InfectionStats;
import at.ac.fhcampuswien.bouncingballs.params.GraphCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {

    AnimationTimer animationTimer;
    long prevTime;
    long prevInfections = 0;
    double speedModifier = 1;

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

    @FXML
    private Label populationCount;

    @FXML
    private Label infectedCount;

    @FXML
    private Label susceptibleCount;

    @FXML
    private Label removedCount;

    @FXML
    private Label infectionrate;

    @FXML
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button newSim;

    @FXML
    private Button resetButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button rewindButton;

    InfectableBalls balls = new InfectableBalls();






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resetPrevTime();

        this.simulation = new Canvas(SimulationCanvasParams.getWidth(), SimulationCanvasParams.getHeight());
        this.simulationGC = this.simulation.getGraphicsContext2D();
        this.graph = new Canvas(GraphCanvasParams.getWidth(), GraphCanvasParams.getHeight());
        this.graphContainer.getChildren().add(this.graph);
        GridPane.setValignment(graphContainer, VPos.BOTTOM);
        this.graphGC = this.graph.getGraphicsContext2D();
        this.simulationGC.setFill(Color.BLACK);
        this.graphGC.setFill(Color.TRANSPARENT);
        this.borderPane.setCenter(this.simulation);

        this.graphGC.fillRect(0, 0, SimulationCanvasParams.getWidth(), SimulationCanvasParams.getHeight());

        this.balls.generateBalls(SimulationValues.getBallCount(),SimulationValues.getInitalInfections());
        this.simulationTimer();
        this.startAnimationTimer();

        forwardButton.setOnMouseClicked(event -> {
            speedModifier+=0.25;
        });

        rewindButton.setOnMouseClicked(event -> {
            if(speedModifier>0.1){
                speedModifier-=0.25;
            }
        });

        newSim.setOnMouseClicked(event -> {
            try {
                this.animationTimer.stop();
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homepage.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Bouncing-Balls");
                stage.setScene(new Scene(root, 1280, 720));
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });

        resetButton.setOnMouseClicked(event -> {
            this.balls.removeAllBalls();
            this.balls.generateBalls(SimulationValues.getBallCount(),SimulationValues.getInitalInfections());
            System.out.println(prevTime);
        });

        pauseButton.setOnMouseClicked(event -> {
            this.stopAnimationTimer();
        });
        playButton.setOnMouseClicked(event -> {
            this.startAnimationTimer();
        });
    }



    public void simulationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                long curTimeMilli = TimeUnit.NANOSECONDS.toMillis(currentNanoTime);
                double curTimeMS = TimeUnit.NANOSECONDS.toMillis(currentNanoTime - prevTime) ;
                double prevNanoTime = prevTime;
                prevTime = TimeUnit.NANOSECONDS.toMillis(prevTime);
                //System.out.println(curTimeMS);
                //Clear canvas, set BackgroundColor
                simulationGC.clearRect(0, 0, 1000, 1000);
                simulationGC.setFill(Color.BLACK);
                simulationGC.fillRect(0, 0, 1000, 1000);
                //Zeitunterschied zwischen diesem und vorherigen frame in 100stel sekunden
                double deltaTimeSeconds=(double)(currentNanoTime-prevNanoTime)/10000000;
                System.out.println(deltaTimeSeconds);

                //handles everything sorrounding the Infectable balls
                //System.out.println(deltaTimeSeconds);
                simulationGC = balls.drawAndHandleTimestep(simulationGC, deltaTimeSeconds*speedModifier);


                //Quadtree Test

                /*double x,y,w,h;
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
                }*/
                // InfectionStats.infektionsgeschehen();

                // InfectionStats.printCurStats();

                long infected = balls.getCountByState(InfectableBalls.InfectionStatus.INFECTED);
                populationCount.setText(Integer.toString(SimulationValues.getBallCount()));
                infectedCount.setText(Long.toString(infected));
                susceptibleCount.setText(Long.toString(balls.getCountByState(InfectableBalls.InfectionStatus.SUSCEPTIBLE)));
                removedCount.setText(Long.toString(balls.getCountByState(InfectableBalls.InfectionStatus.REMOVED)));
                long timeDifference = curTimeMilli - prevTime;
                long infectionDifference = infected - prevInfections;
                long infectionRate=0;
                if (timeDifference != 0) {
                     infectionRate = infectionDifference/timeDifference;
                }

                infectionrate.setText(Long.toString(infectionRate));
                prevTime = currentNanoTime;
            }
        };
    }

    private void resetPrevTime(){
        this.prevTime = System.nanoTime();
    }

    private void startAnimationTimer(){
        if(this.animationTimer != null){
            this.animationTimer.start();
           // this.balls.simulationResumed();
        }
    }

    private void stopAnimationTimer(){
        if(this.animationTimer != null){
           // this.balls.simulationPaused();
            this.animationTimer.stop();
        }
    }

}
