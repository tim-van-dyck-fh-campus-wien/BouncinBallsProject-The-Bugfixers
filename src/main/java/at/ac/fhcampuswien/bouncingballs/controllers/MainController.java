package at.ac.fhcampuswien.bouncingballs.controllers;

import at.ac.fhcampuswien.bouncingballs.balls.GraphStats;
import at.ac.fhcampuswien.bouncingballs.balls.InfectableBalls;
import at.ac.fhcampuswien.bouncingballs.balls.Quarantine;
import at.ac.fhcampuswien.bouncingballs.params.GraphCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {

    AnimationTimer animationTimer;
    private ScheduledExecutorService dataTimer;
    private final LinkedList<GraphStats> graphDataSet = new LinkedList<>();
    private static final int maxGraphDataPoints = 50000;
    long prevTime;
    long prevInfections = 0;
    double speedModifier = 1;
    private GraphStats lastDrawnData = null;
    private static final double graphPointRadius = 3;


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
    GridPane containerLeft;


    //MONI labels und buttons von FXML in Java Controller reinladen
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

    //Needed for setting the quarantine area
    boolean getQuarantineCoordinates = false;

    double lastCallTime = 0;


    //Quarantine Button
    @FXML
    protected void quarantineEvt(ActionEvent event) {
        if (this.balls.quarantine == null) {
            getQuarantineCoordinates = true;
        } else if (this.balls.quarantine.quarantineActive == false) {
            getQuarantineCoordinates = true;
        }
    }

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

        //if generate balls returns false, the balls do not fit on the canvas
        this.balls.generateBalls(SimulationValues.getBallCount(), SimulationValues.getInitalInfections());
        this.simulationTimer();
        this.startAnimationTimer();
        // this.startDataTimer();

        //MONI speedModifier höher setzen
        forwardButton.setOnMouseClicked(event -> {
            speedModifier += 0.25;
        });

        //MONI speedModifier runter setzen aber nicht kleiner als 0.1
        rewindButton.setOnMouseClicked(event -> {
            if (speedModifier > 0.1) {
                speedModifier -= 0.25;
            }
        });

        //MONI neue Simulation starten
        newSim.setOnMouseClicked(event -> {
            try {
                //Animation stoppen um resources frei zu machen
                this.animationTimer.stop();

                //Homepage FXML laden
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("homepage.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Bouncing Balls");
                stage.setScene(new Scene(root, 1280, 720));
                stage.setResizable(false);
                stage.show();
                //Homepage FXML darstellen
                ((Node) (event.getSource())).getScene().getWindow().hide();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //MONI reseten der simulation
        resetButton.setOnMouseClicked(event -> {
            //Reset Quarantine Area if a quarantine object exists
            if (this.balls.quarantine != null) {
                this.balls.quarantine.resetQuarantine();
                this.getQuarantineCoordinates = false;
            }
            //Alte bälle löschen
            this.balls.removeAllBalls();
            //Wieder neu erstellen, mit der Menge an Bällen die wir gegeben haben
            this.balls.generateBalls(SimulationValues.getBallCount(), SimulationValues.getInitalInfections());
            //Diagramm auf 0 setzen
            this.graphDataSet.clear();
            this.lastCallTime = 0;
            this.drawGraph();
        });

        //MONI pause button, die animationstimer und somit die simulation wird pausiert und die
        //Bälle können sich nicht mehr bewegen
        pauseButton.setOnMouseClicked(event -> {
            this.stopAnimationTimer();
            // this.stopDataTimer();
        });

        //MONI animation timer starten
        playButton.setOnMouseClicked(event -> {
            long curTime = System.nanoTime();
            //so that the time difference used for moving the balls etc is correct
            prevTime = curTime;
            this.startAnimationTimer();
            //this.startDataTimer();
        });
        //displaying the quarantine prior to setting the final location
        simulation.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (getQuarantineCoordinates) {
                    if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
                        Quarantine.setPreviewAreaCoordinates(new Point(event.getX(), event.getY()));
                    }
                }
            }
        });
        //for setting the quarantines position
        simulation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (getQuarantineCoordinates) {
                    balls.startNewQuarantine(new Point(event.getX(), event.getY()));
                    getQuarantineCoordinates = false;
                }

            }
        });


    }


    private void navigateToHomePage() {

    }

    public void simulationTimer() {
        /*
        Starten die Sim Animation und bewegt fortlaufend die Bälle im Canvas, wo auch
        die Logik visualisiert wird ob ein Ball mit einem andren Kollidiert und somit
        infiziert hat. Die Bildfläche wird als Referenz an die drawing methoden weiter
        gereicht und gezeichnet. Die Berechnung bzgl Geschwindigkeit wird durch die Zeit
        zwischen den Frames kalkuliert und kann zusätzlich mit einem speedModifier beschleunigt werden
         */
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                long curTimeMilli = TimeUnit.NANOSECONDS.toMillis(currentNanoTime);
                double prevNanoTime = prevTime;
                prevTime = TimeUnit.NANOSECONDS.toMillis(prevTime);
                //Clear canvas, set BackgroundColor
                simulationGC.clearRect(0, 0, 1000, 1000);
                simulationGC.setFill(Color.BLACK);
                simulationGC.fillRect(0, 0, 1000, 1000);
                //Zeitunterschied zwischen diesem und vorherigen frame in 100stel sekunden
                double deltaTimeSeconds = (double) (currentNanoTime - prevNanoTime) / 10000000;
                //handles everything sorrounding the Infectable balls
                simulationGC = balls.drawAndHandleTimestep(simulationGC, deltaTimeSeconds * speedModifier);
                if (getQuarantineCoordinates) {
                    simulationGC = Quarantine.drawQuarantinePreviewArea(simulationGC);
                }

                //MONI den zwischenstand der Bälle in den JavaFX elementen darstellen in form von zahlen und text,sowie
                //auch Graph
                long infected = balls.getCountByState(InfectableBalls.InfectionStatus.INFECTED);
                populationCount.setText(Integer.toString(SimulationValues.getBallCount()));
                infectedCount.setText(Long.toString(infected));
                susceptibleCount.setText(Long.toString(balls.getCountByState(InfectableBalls.InfectionStatus.SUSCEPTIBLE)));
                removedCount.setText(Long.toString(balls.getCountByState(InfectableBalls.InfectionStatus.REMOVED)));

                //Die geschwindigkeit der infizierten rate berechnen zwischen 2 frames und die differenze darstellen
                long timeDifference = curTimeMilli - prevTime;
                long infectionDifference = infected - prevInfections;
                long infectionRate = 0;
                if (timeDifference != 0) {
                    infectionRate = infectionDifference / timeDifference;
                }

                infectionrate.setText(Long.toString(infectionRate));
                prevTime = currentNanoTime;


                if (balls.curTime - lastCallTime > 0.1) {
                    //MONI Gecheckt ob die Bälle auch miteinander kollidiert sind
                    handleDataCollection();
                    lastCallTime = balls.curTime;
                }


            }
        };
    }


    private void handleDataCollection() {
        double percentInfected = this.balls.getCountByState(InfectableBalls.InfectionStatus.INFECTED) / (double) SimulationValues.getBallCount();
        double percentSusceptible = this.balls.getCountByState(InfectableBalls.InfectionStatus.SUSCEPTIBLE) / (double) SimulationValues.getBallCount();
        double percentRemoved = this.balls.getCountByState(InfectableBalls.InfectionStatus.REMOVED) / (double) SimulationValues.getBallCount();

        this.graphDataSet.add(new GraphStats(percentInfected, percentSusceptible, percentRemoved));
        if (this.graphDataSet.size() > maxGraphDataPoints) {
            this.graphDataSet.removeFirst();
        }
        this.drawGraph();
    }


    private void drawGraph() {

        /*
            //DrawRemoved
            this.graphGC.setFill(Color.GRAY);
            double removedHeight = percentRemoved * GraphCanvasParams.getHeight();
            double width = GraphCanvasParams.getWidth();
            this.graphGC.fillRect(0, GraphCanvasParams.getHeight() - removedHeight, width, removedHeight);

            //drawInfected
            this.graphGC.setFill(Color.RED);
            double infectedHeight = percentInfected * GraphCanvasParams.getHeight();
            this.graphGC.fillRect(0, GraphCanvasParams.getHeight() - removedHeight - infectedHeight, width, infectedHeight);
            System.out.println("PercentInfected" + this.balls.getCountByState(InfectableBalls.InfectionStatus.INFECTED));

            //drawSusceptible
            this.graphGC.setFill(Color.BLUE);
            double susceptibleHeight = percentSusceptible * GraphCanvasParams.getHeight();
            this.graphGC.fillRect(0, 0, width, susceptibleHeight);

         */
        this.graphGC.clearRect(0, 0, GraphCanvasParams.getWidth(), GraphCanvasParams.getHeight());
        if (this.graphDataSet.size() > 1 && lastDrawnData != this.graphDataSet.getLast()) {
            this.graphGC.setLineWidth(2);

            GraphStats prevGraphStats = null;
            double offsetX = 0;
            double lineWidth = (double) GraphCanvasParams.getWidth() / (graphDataSet.size() - 1);

            for (GraphStats graphStats : graphDataSet) {
                if (prevGraphStats != null) {
                    this.graphGC.setStroke(Color.GRAY);
                    this.graphGC.strokeLine(offsetX, (1 - prevGraphStats.percentRemoved) * GraphCanvasParams.getHeight(), offsetX + lineWidth, (1 - graphStats.percentRemoved) * GraphCanvasParams.getHeight());

                    this.graphGC.setStroke(Color.RED);
                    this.graphGC.strokeLine(offsetX, (1 - prevGraphStats.percentInfected) * GraphCanvasParams.getHeight(), offsetX + lineWidth, (1 - graphStats.percentInfected) * GraphCanvasParams.getHeight());

                    this.graphGC.setStroke(Color.BLUE);
                    this.graphGC.strokeLine(offsetX, (1 - prevGraphStats.percentSuspectible) * GraphCanvasParams.getHeight(), offsetX + lineWidth, (1 - graphStats.percentSuspectible) * GraphCanvasParams.getHeight());

                    offsetX += lineWidth;

                }

                this.graphGC.setFill(Color.GRAY);
                this.graphGC.fillOval(offsetX - graphPointRadius, (1 - graphStats.percentRemoved) * GraphCanvasParams.getHeight() - graphPointRadius, graphPointRadius * 2, graphPointRadius * 2);

                this.graphGC.setFill(Color.RED);
                this.graphGC.fillOval(offsetX - graphPointRadius, (1 - graphStats.percentInfected) * GraphCanvasParams.getHeight() - graphPointRadius, graphPointRadius * 2, graphPointRadius * 2);

                this.graphGC.setFill(Color.BLUE);
                this.graphGC.fillOval(offsetX - graphPointRadius, (1 - graphStats.percentSuspectible) * GraphCanvasParams.getHeight() - graphPointRadius, graphPointRadius * 2, graphPointRadius * 2);

                prevGraphStats = graphStats;

            }

        }
        if (this.graphDataSet.size() > 0) {
            lastDrawnData = this.graphDataSet.getLast();
        }

    }

    private void resetPrevTime() {
        this.prevTime = System.nanoTime();
    }

    private void startAnimationTimer() {
        if (this.animationTimer != null) {
            this.animationTimer.start();
            // this.balls.simulationResumed();
        }
    }

    private void stopAnimationTimer() {
        if (this.animationTimer != null) {
            // this.balls.simulationPaused();
            this.animationTimer.stop();
        }
    }


}
