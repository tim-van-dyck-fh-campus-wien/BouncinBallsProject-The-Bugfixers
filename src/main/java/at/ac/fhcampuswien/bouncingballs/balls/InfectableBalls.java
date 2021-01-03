package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.InfectableBallsParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import at.ac.fhcampuswien.bouncingballs.shapes.Circle;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import at.ac.fhcampuswien.bouncingballs.shapes.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

//Class containing a List of Infectable Balls, and functions sourrounding it
public class InfectableBalls {

    public enum InfectionStatus {
        SUSCEPTIBLE,
        INFECTED,
        REMOVED
    }

    List<InfectableBall> balls = new ArrayList<>();
    public QuadTree tree = new QuadTree(new Rectangle(SimulationCanvasParams.getWidth() / 2, SimulationCanvasParams.getHeight() / 2, SimulationCanvasParams.getWidth() / 2, SimulationCanvasParams.getHeight() / 2), (byte) 8);
    //CurrentTime in hundreths of a second
    public double curTime=0;
    //needed for correct collision handling!
    public double previousTimeDifference=-1;

    public long getCountByState(InfectionStatus status){
        return this.balls.stream().filter(ball -> ball.infectionStatus == status).count();
    }

    public void removeAllBalls(){
        this.curTime=0;
        this.balls.clear();
    }
    //Function used to generate the balls from scratch and assign the start positions to them.

    public void generateBalls(int population, int initialInfected) {
        Random rand = new Random();

        int infectedAddedCount = 0;

        //Space the balls evenly so that no balls overlap on initialization
        //Calculate how many balls can fit horizontally on the canvas
        int ballSpaceHorizontal = SimulationCanvasParams.getWidth() / (InfectableBallsParams.ballradius * 2);
        int ballSpaceVertical = SimulationCanvasParams.getHeight() / (InfectableBallsParams.ballradius * 2);//calc how many balls fit vertically
        //Check if all balls can fit inside the canvas

        System.out.println("Balls Can Fit: " + ballSpaceVertical);
        if ((ballSpaceHorizontal * ballSpaceVertical > SimulationValues.getBallCount())) {
            List<Point> possibleBallCoordinates = new ArrayList<>();//List containing all possible coordinates for a ball while the Canvas is evenly divided

            //Fill the list with all the possible coordinates
            for (double y = InfectableBallsParams.ballradius; y < SimulationCanvasParams.getHeight() - InfectableBallsParams.ballradius; y = y + InfectableBallsParams.ballradius * 2) {
                for (double x = InfectableBallsParams.ballradius; x < SimulationCanvasParams.getWidth() - InfectableBallsParams.ballradius; x = x + InfectableBallsParams.ballradius * 2) {
                    possibleBallCoordinates.add(new Point(x, y));
                }
            }

            //Place the balls randomly on the grid without overlapping
            for (int cnt = 0; cnt < population; cnt++) {
                boolean infected = false;
                if(infectedAddedCount < initialInfected){
                    infected = true;
                    infectedAddedCount++;
                }
                int randomIndex = rand.nextInt(possibleBallCoordinates.size());
                this.balls.add(new InfectableBall(possibleBallCoordinates.get(randomIndex),cnt,infected));
                possibleBallCoordinates.remove(randomIndex);
            }
            System.out.println("Balls are:" + this.balls.size() + "\nshould be: " + SimulationValues.getBallCount());

        } else {//balls do not fit!
            System.out.println("ERROR!Balls cannot fit inside the canvas! ERROR");
        }
    }

    //function used to handle a single timestep
    //draws the balls, moves them, handles collisions etc.
    public GraphicsContext drawAndHandleTimestep(GraphicsContext gc, double time) {
        this.curTime= this.curTime + (time/100);
        System.out.println("curtime"+this.curTime);
        this.resetQuadtree();
        this.fillQuadtree();
        //The collision Handling has to happen prior to the move operation of the balls!
        gc = this.handleCollision(gc,time);
        this.moveAllBalls(time);
        this.previousTimeDifference=time;
        return draw(gc);

    }

    //draws all the balls
    public GraphicsContext draw(GraphicsContext gc) {
        for (InfectableBall el : balls) {
            gc = el.draw(gc);
        }
        return gc;
    }

    public void fillQuadtree() {
        for (InfectableBall el : balls) {
            tree.insert(new Point(el.getCoordinates(), el.getIdOfInstance()));
        }
    }

    //calls the move function on all balls
    public void moveAllBalls(double time) {
        for (InfectableBall el : balls) {
            el.move(time);
            el.refreshInfectionStatus(this.curTime);
        }
    }

    //removes all points from the quadtree
    private void resetQuadtree() {
        this.tree = new QuadTree(new Rectangle(SimulationCanvasParams.getWidth() / 2, SimulationCanvasParams.getHeight() / 2, SimulationCanvasParams.getWidth() / 2, SimulationCanvasParams.getHeight() / 2), (byte) 8);
    }

    //Testmethode zur ersten Simulation des Ansteckungsmechanismus
    private GraphicsContext handleCollision(GraphicsContext gc,double currentTimeDifference) {
        //List <InfectableBall> collisionHandled = new ArrayList<>();
        //Dummy Liste bestehend aus allen Bällen,
        List<InfectableBall> toHandle = new ArrayList<>(balls);
        //Infectionsradius, in welchem ein ball infiziert wird! Beim Quadtree wird hier der doppelte radius verwendet
        //da hier auch die größe des anderen balls zählt
        double infectionRadius = InfectableBallsParams.ballradius;
        //Loop welcher die Balliste durchgeht
        //Und überprüft ob dieser ball infiziert ist, falls er infiziert ist,
        //wird der Quadtree genutzt, um andere Bälle zu finden welche in seinem Infektionsradius sind
        //Falls Bälle gefunden werden, werden diese infiziert
        for (int c = 0; c < toHandle.size(); ) {
            InfectableBall ball = toHandle.get(c);
            //Zeichnen des Infektionskreises um den derzeitigen Ball
            //gc.setStroke(Color.WHITE);
            //gc.strokeOval(ball.getCoordinates().x-infectionRadius,ball.getCoordinates().y-infectionRadius,2*infectionRadius,2*infectionRadius);
            //gc.strokeRect(ball.getCoordinates().x-infectionRadius,ball.getCoordinates().y-infectionRadius,2*infectionRadius,2*infectionRadius);
            //Erstellen des Kreises welcher dem Infektionsradius entspricht, um den Quadtree zu durchsuchen
            Circle infectionCircle = new Circle(ball.getCoordinates().x, ball.getCoordinates().y, infectionRadius * 2);
            // Rectangle infectionRect = new Rectangle(ball.getCoordinates().x,ball.getCoordinates().y,infectionRadius,infectionRadius);

            //Suche nur nach Bällen welche in der Nähe des Balls sind, falls der derzeitige Ball infiziert ist
            //Denn nur dann kann der Ball einen anderen Ball infizieren
            for (Point point : tree.query(infectionCircle)) {
                //Nur jener ball soll infiziert werden, welcher noch nicht infiziert ist
                if (point.id != ball.getIdOfInstance()) {
                    //Suche jenen Ball welcher dem vom Quadtree zurückgegebenen Point enstpricht
                    //Dies basiert auf der im Point abgespeicherten ID des Balls welche bei der Instanzierung
                    //vergeben wird
                    List<InfectableBall> res = balls.stream().filter(v -> (v.getIdOfInstance() == point.id)).collect(Collectors.toList());
                    for (InfectableBall el : res) {
                        //Infiziere jenen ball welcher im Infektionsradius des ürsprünglichen infizierten Balls ist
                        if (ball.infectionStatus == InfectionStatus.INFECTED) {
                            //el.infectionStatus = InfectableBall.InfectionStatus.INFECTED;
                            el.infectBall(this.curTime);
                        } else if (el.infectionStatus == InfectionStatus.INFECTED) {
                            //ball.infectionStatus= InfectableBall.InfectionStatus.INFECTED;
                            ball.infectBall(this.curTime);
                        }
                        // System.out.println(el.print());
                        Point firstVelocityVector = ball.getVelocityVector();
                        Point secondVelocityVector = el.getVelocityVector();
                        Point newFirstVelocityVector = new Point(0, 0);
                        Point newSecondVelocityVector = new Point(0, 0);

                        //Calculate velocity vectors after collision
                        newFirstVelocityVector.x = secondVelocityVector.x;
                        newFirstVelocityVector.y = secondVelocityVector.y;
                        newSecondVelocityVector.x = firstVelocityVector.x;
                        newSecondVelocityVector.y = firstVelocityVector.y;



                        ball.setVelocityVector(newFirstVelocityVector);
                        el.setVelocityVector(newSecondVelocityVector);

                        //In order to guarantee that the balls dont collide in the next frame,
                        //we have to check wheter the currentTimestep is smaller than the previous one
                        //if it is, we have to move the colliding balls by the difference of those two,
                        //so that the balls definetly don't collide in the next frame!
                        if(currentTimeDifference<previousTimeDifference){
                            ball.move(previousTimeDifference-currentTimeDifference);
                            el.move(previousTimeDifference-currentTimeDifference);
                        }
                        toHandle.remove(el);

                    }
                    toHandle.remove(ball);
                }
                //   System.out.println(point.id);
            }

            c++;

        }
        return gc;
    }


    public static Point calculateVectorBetweenTwoPointsAndStretch(Point a, Point b, double velocity) {
        Point vector = Point.calculateVectorBetweenTwoPoints(a, b);
        return Point.stretchVectorToVelocity(vector, velocity);
    }

}
