package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.InfectableBallsParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.Random;

public class InfectableBall {

    enum InfectionStatus {
        SUSCEPTIBLE,
        INFECTED,
        REMOVED
    }
    InfectionStatus infectionStatus = InfectionStatus.SUSCEPTIBLE;
    private Point coordinates = new Point(0,0);
    private Point velocityVector= new Point(0,0);
    private String id;
    //Counts how many Infectable balls have been generated, needed for the corresponding id identifying the Object
    private static int instanceCount=0;


    public Point getCoordinates(){
        return this.coordinates;
    }
    public InfectableBall() {
        this.genRandomCoordinatesVelocity();
        this.generateIdOfObject();
        instanceCount+=1;
    }
    public String getIdOfInstance(){
        return this.id;
    }
    //Generate the id of the instance
    private void generateIdOfObject(){
        this.id=Integer.toString(instanceCount);
    }
    //Must be called when a new List of Infectable balls wants to be generated
    //needed to correctly generate the id's identifying each instance of a Infectable Balls
    public static void resetInfectableBalls(){
        instanceCount=0;
    }
    public void genRandomCoordinatesVelocity(){
        Random rn = new Random();

        //generate random coordiantes, leave space of the balls radius
        coordinates.x = (double) rn.nextInt(SimulationCanvasParams.getWidth()-InfectableBallsParams.ballradius*2)+InfectableBallsParams.ballradius;
        coordinates.y = (double) rn.nextInt(SimulationCanvasParams.getHeight()-InfectableBallsParams.ballradius*2)+InfectableBallsParams.ballradius;

        //exclude degrees of multiples of 90 degrees while computing the values for the velocity vector
        while(this.velocityVector.x==0||this.velocityVector.y==0){
            this.velocityVector.x = rn.nextInt(20)-10;
            this.velocityVector.y = rn.nextInt(20)-10;
        }


        //Divide by "Einheitsvektor", and multiply by the desired velocity to get the correct velocity
        double lengthOfVector=Math.sqrt(Math.pow(this.velocityVector.x,2)+Math.pow(this.velocityVector.y,2));
        this.velocityVector.x=this.velocityVector.x/lengthOfVector;
        this.velocityVector.y=this.velocityVector.y/lengthOfVector;
        this.velocityVector.x=this.velocityVector.x*InfectableBallsParams.velocity;
        this.velocityVector.y=this.velocityVector.y*InfectableBallsParams.velocity;
         lengthOfVector=Math.sqrt(Math.pow(this.velocityVector.x,2)+Math.pow(this.velocityVector.y,2));
        System.out.println("len"+lengthOfVector);
    }
    public GraphicsContext draw(GraphicsContext gc){
        if(infectionStatus == InfectionStatus.SUSCEPTIBLE){
            gc.setFill(Color.BLUE);
        }else if(infectionStatus== InfectionStatus.INFECTED){
            gc.setFill(Color.RED);
        }
        //fillOval uses the top left corner, and the last two parameters describe the diameter of the oval, thus the radius is multiplied by 2
        gc.fillOval(coordinates.x-InfectableBallsParams.ballradius,coordinates.y-InfectableBallsParams.ballradius,InfectableBallsParams.ballradius*2,InfectableBallsParams.ballradius*2);
        return gc;
    }
    public String print(){
        return "x:"+coordinates.x+" y:"+coordinates.y;
    }
    //translate the ball according to it's velocity vector
    public void move(double time){
        this.coordinates.x+=this.velocityVector.x*time;
        this.coordinates.y+=this.velocityVector.y*time;
        this.checkBorderCollision();
    }

    //Check if Ball is colliding with the edges of the canvas
   private void checkBorderCollision(){
        if(this.coordinates.x - InfectableBallsParams.ballradius < 0){      // Kollision linke Seite
            this.velocityVector.x =- this.velocityVector.x;
        }
       if(this.coordinates.y - InfectableBallsParams.ballradius < 0){       // obere Seite
           this.velocityVector.y =- this.velocityVector.y;
       }
       if(this.coordinates.x + InfectableBallsParams.ballradius > SimulationCanvasParams.getWidth()){       // rechte Seite
           this.velocityVector.x =- this.velocityVector.x;
       }
       if(this.coordinates.y + InfectableBallsParams.ballradius > SimulationCanvasParams.getHeight()){       // rechte Seite
           this.velocityVector.y =- this.velocityVector.y;
       }

    }


}
