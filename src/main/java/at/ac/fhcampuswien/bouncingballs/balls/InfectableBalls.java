package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.SimulationCanvasParams;
import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import at.ac.fhcampuswien.bouncingballs.shapes.Rectangle;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

//Class containing a List of Infectable Balls, and functions sourrounding it
public class InfectableBalls {
    List<InfectableBall> balls = new ArrayList<>();
  public QuadTree tree = new QuadTree(new Rectangle(SimulationCanvasParams.getWidth()/2,SimulationCanvasParams.getHeight()/2,SimulationCanvasParams.getWidth()/2,SimulationCanvasParams.getHeight()/2),(byte)8);

    public void generateBalls(){
        for(int c = 0; c< SimulationValues.getBallCount(); c++){
            this.balls.add(new InfectableBall());
        }
        int c=0;
        for(InfectableBall el : balls){
            System.out.println(c+" "+el.print());
            System.out.println("))))");
        }
    }
    //function used to handle a single timestep
    //draws the balls, moves them, handles collisions etc.
    public GraphicsContext drawAndHandleTimestep(GraphicsContext gc,double time){
        this.resetQuadtree();
        this.moveAllBalls(0.1);
        this.fillQuadtree();
        return draw(gc);

    }
    //draws all the balls
    public GraphicsContext draw(GraphicsContext gc){
        for(InfectableBall el : balls){
            gc=el.draw(gc);
        }
        return gc;
    }
    public void fillQuadtree(){
        for(InfectableBall el:balls){
            tree.insert(new Point(el.getCoordinates(),el.getIdOfInstance()));
        }
    }
    //calls the move function on all balls
    public void moveAllBalls(double time){
        for(InfectableBall el:balls){
            el.move(time);
        }
    }
    //removes all points from the quadtree
    private void resetQuadtree(){
        this.tree=new QuadTree(new Rectangle(SimulationCanvasParams.getWidth()/2,SimulationCanvasParams.getHeight()/2,SimulationCanvasParams.getWidth()/2,SimulationCanvasParams.getHeight()/2),(byte)8);
    }

}
