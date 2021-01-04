package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.InfectableBallsParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class InfectableBall {

    InfectableBalls.InfectionStatus infectionStatus = InfectableBalls.InfectionStatus.SUSCEPTIBLE;
    private Point coordinates = new Point(0, 0);
    private Point velocityVector = new Point(0, 0);
    private int id;
    //Counts how many Infectable balls have been generated, needed for the corresponding id identifying the Object
    //Time where the ball was infected
    private double startOfInfection;
    private double timeElapsedSinceInfection;

    public Point getVelocityVector() {
        return velocityVector;
    }

    public void setVelocityVector(Point velocityVector) {
        this.velocityVector = velocityVector;
    }

    public Point getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public InfectableBall(Point coordinates, int identifier, boolean initInfected) {
        this.id = identifier;
        if (initInfected) {
            this.infectBall(0);

        }
        this.coordinates = coordinates;
        this.genRandomVelocitys();
    }

    public int getIdOfInstance() {
        return this.id;
    }

    public void genRandomVelocitys() {
        Random rn = new Random();
        while (this.velocityVector.x == 0 || this.velocityVector.y == 0) {
            this.velocityVector.x = rn.nextInt(20) - 10;
            this.velocityVector.y = rn.nextInt(20) - 10;
        }


        //Divide by "Einheitsvektor", and multiply by the desired velocity to get the correct velocity
        double lengthOfVector = Math.sqrt(Math.pow(this.velocityVector.x, 2) + Math.pow(this.velocityVector.y, 2));
        this.velocityVector.x = this.velocityVector.x / lengthOfVector;
        this.velocityVector.y = this.velocityVector.y / lengthOfVector;
        this.velocityVector.x = this.velocityVector.x * InfectableBallsParams.velocity;
        this.velocityVector.y = this.velocityVector.y * InfectableBallsParams.velocity;
        lengthOfVector = Math.sqrt(Math.pow(this.velocityVector.x, 2) + Math.pow(this.velocityVector.y, 2));
       // System.out.println("len" + lengthOfVector);
    }

    public void genRandomCoordinatesVelocity() {
        Random rn = new Random();

        //generate random coordiantes, leave space of the balls radius
        coordinates.x = (double) rn.nextInt(SimulationCanvasParams.getWidth() - InfectableBallsParams.ballradius * 2) + InfectableBallsParams.ballradius;
        coordinates.y = (double) rn.nextInt(SimulationCanvasParams.getHeight() - InfectableBallsParams.ballradius * 2) + InfectableBallsParams.ballradius;

        //exclude degrees of multiples of 90 degrees while computing the values for the velocity vector
        while (this.velocityVector.x == 0 || this.velocityVector.y == 0) {
            this.velocityVector.x = rn.nextInt(20) - 10;
            this.velocityVector.y = rn.nextInt(20) - 10;
        }


        //Divide by "Einheitsvektor", and multiply by the desired velocity to get the correct velocity
        double lengthOfVector = Math.sqrt(Math.pow(this.velocityVector.x, 2) + Math.pow(this.velocityVector.y, 2));
        this.velocityVector.x = this.velocityVector.x / lengthOfVector;
        this.velocityVector.y = this.velocityVector.y / lengthOfVector;
        this.velocityVector.x = this.velocityVector.x * InfectableBallsParams.velocity;
        this.velocityVector.y = this.velocityVector.y * InfectableBallsParams.velocity;
        lengthOfVector = Math.sqrt(Math.pow(this.velocityVector.x, 2) + Math.pow(this.velocityVector.y, 2));
       // System.out.println("len" + lengthOfVector);
    }

    public GraphicsContext draw(GraphicsContext gc) {
        if (infectionStatus == InfectableBalls.InfectionStatus.SUSCEPTIBLE) {
            gc.setFill(Color.BLUE);
        } else if (infectionStatus == InfectableBalls.InfectionStatus.INFECTED) {
            gc.setFill(Color.RED);
        } else if (infectionStatus == InfectableBalls.InfectionStatus.REMOVED) {
            gc.setFill(Color.GRAY);
        }
        //fillOval uses the top left corner, and the last two parameters describe the diameter of the oval, thus the radius is multiplied by 2
        gc.fillOval(coordinates.x - InfectableBallsParams.ballradius, coordinates.y - InfectableBallsParams.ballradius, InfectableBallsParams.ballradius * 2, InfectableBallsParams.ballradius * 2);
        return gc;
    }

    public String print() {
        return "x:" + coordinates.x + " y:" + coordinates.y;
    }

    //translate the ball according to it's velocity vector
    public void move(double time) {
        this.coordinates.x += this.velocityVector.x * time;
        this.coordinates.y += this.velocityVector.y * time;
        this.checkBorderCollision();
    }

    //Check if Ball is colliding with the edges of the canvas
    private void checkBorderCollision() {
        if (this.coordinates.x - InfectableBallsParams.ballradius < 0) {      // Kollision linke Seite
            this.velocityVector.x = -this.velocityVector.x;
        }
        if (this.coordinates.y - InfectableBallsParams.ballradius < 0) {       // obere Seite
            this.velocityVector.y = -this.velocityVector.y;
        }
        if (this.coordinates.x + InfectableBallsParams.ballradius > SimulationCanvasParams.getWidth()) {       // rechte Seite
            this.velocityVector.x = -this.velocityVector.x;
        }
        if (this.coordinates.y + InfectableBallsParams.ballradius > SimulationCanvasParams.getHeight()) {       // rechte Seite
            this.velocityVector.y = -this.velocityVector.y;
        }

    }

    public void infectBall(double timeOfInfection) {
        if (this.infectionStatus == InfectableBalls.InfectionStatus.SUSCEPTIBLE) {
            this.infectionStatus = InfectableBalls.InfectionStatus.INFECTED;
            this.startOfInfection = timeOfInfection;
        }
    }
    //Save relevant Information when simulation is paused


    public void refreshInfectionStatus(double currentTime) {
        if (this.infectionStatus == InfectableBalls.InfectionStatus.INFECTED) {
            double delta = currentTime - this.startOfInfection;
           // System.out.println("start of infection is"+startOfInfection);

            //System.out.println(deltaSeconds);
            if (delta > SimulationValues.getTimeToRecover()) {
                this.infectionStatus = InfectableBalls.InfectionStatus.REMOVED;
            }
        }
    }


}
