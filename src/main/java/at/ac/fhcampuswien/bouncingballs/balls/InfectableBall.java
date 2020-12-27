package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.InfectableBallsParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.Random;

public class InfectableBall {

    public InfectableBall() {
        /*Random rn = new Random();
        coordinates.x = (double) rn.nextInt(SimulationCanvasParams.getWidth());
        coordinates.y = (double) rn.nextInt(SimulationCanvasParams.getHeight());


        while((int)Math.sqrt(velocityVector.x*velocityVector.x+velocityVector.y*velocityVector.y)!=InfectableBallsParams.velocity){
            double angledegree = (double)rn.nextInt(360);

            if((angledegree%90>=0)&&((angledegree%90)<=5)||(angledegree%90>=84)&&((angledegree%90<=89))){
                angledegree+=20;
            }
            double angle = Math.toRadians(angledegree);
            //eliminate multiples of 90 degrees

            double x=InfectableBallsParams.velocity;
            double y=0;
            System.out.println("angle: "+angle);

            velocityVector.x = x*Math.cos((double)angle)-y*Math.sin((double)angle) ;
            velocityVector.y=x*Math.sin((double)angle)+y*Math.cos((double)angle);
        }


        */
        this.genRandomCoordinatesVelocity();
    }


    enum InfectionStatus {
        SUSCEPTIBLE,
        INFECTED,
        REMOVED
    }
    InfectionStatus infectionStatus = InfectionStatus.SUSCEPTIBLE;
    Point2D.Double coordinates = new Point2D.Double(0,0);
    Point2D.Double velocityVector= new Point2D.Double(0,0);

    public void genRandomCoordinatesVelocity(){
        Random rn = new Random();
        coordinates.x = (double) rn.nextInt(SimulationCanvasParams.getWidth());
        coordinates.y = (double) rn.nextInt(SimulationCanvasParams.getHeight());
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
        }
        gc.fillOval(coordinates.x-InfectableBallsParams.ballradius,coordinates.y-InfectableBallsParams.ballradius,InfectableBallsParams.ballradius,InfectableBallsParams.ballradius);
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
