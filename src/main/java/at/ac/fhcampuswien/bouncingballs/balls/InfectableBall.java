package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.InfectableBallsParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;
import java.util.Random;

public class InfectableBall {

    public InfectableBall(){
        Random rn = new Random();
        coordinates.x =(double) rn.nextInt(SimulationCanvasParams.getWidth());
        coordinates.y = (double)rn.nextInt(SimulationCanvasParams.getHeight());
        int angle = rn.nextInt(360);
        double x=InfectableBallsParams.velocity;
        double y=0;
        velocityVector.x = x*Math.cos((double)angle)-y*Math.sin((double)angle) ;
        velocityVector.y=x*Math.sin((double)angle)+y*Math.sin((double)angle);
    }

    enum InfectionStatus {
        SUSCEPTIBLE,
        INFECTED,
        REMOVED
    }
    InfectionStatus infectionStatus = InfectionStatus.SUSCEPTIBLE;
    Point2D.Double coordinates = new Point2D.Double(0,0);
    Point2D.Double velocityVector= new Point2D.Double(0,0);

    public GraphicsContext draw(GraphicsContext gc){
        if(infectionStatus == InfectionStatus.SUSCEPTIBLE){
            gc.setFill(Color.BLUE);
        }
        gc.fillOval(coordinates.x,coordinates.y,InfectableBallsParams.ballradius,InfectableBallsParams.ballradius);
        return gc;
    }
    public String print(){
        return "x:"+coordinates.x+" y:"+coordinates.y;
    }
    public void move(double time){
        this.coordinates.x+=this.velocityVector.x*time;
        this.coordinates.y+=this.velocityVector.y*time;

    }


}
