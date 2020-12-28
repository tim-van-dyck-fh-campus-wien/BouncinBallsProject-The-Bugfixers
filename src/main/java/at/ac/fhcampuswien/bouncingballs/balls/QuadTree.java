package at.ac.fhcampuswien.bouncingballs.balls;

//Quadtree for fast collision detection/Detection which points might collide

import at.ac.fhcampuswien.bouncingballs.shapes.Circle;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import at.ac.fhcampuswien.bouncingballs.shapes.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public class QuadTree{
    //Describes the boundary of this instance of the quadtree, thus the range in the full canvas covered by this Quadtree
    Rectangle boundary;
    //The capacity describes how many points one instance of a Quadtree can hold
    byte capacity;
    //Array holding all the Points of the instance of this Quadtree
    ArrayList<Point> points = new ArrayList<>();
    //If the Quadtrees capacity is rechead => e.g. if the Quadtree needs to hold
    //more Points than it can hold, it splits into 4 Sub Quadtrees
    QuadTree northwest;//top left
    QuadTree northeast;//top right
    QuadTree southwest;//bottom left
    QuadTree southeast;//bottom right
    static int count=0;
    //boolean value which indicates wheter the given Quadtree has already subdivided
    boolean divided=false;

    //Constructor of a quadtree, initialized by the boundary of this quadtree,
    //thus the area and position it spans inside the canvas
    //and the capacity
    public QuadTree(Rectangle boundary, byte capacity){
        this.boundary = boundary;
        this.capacity = capacity;
    }

    //Insert function of the Quadtree, used to correctly insert points into the quadtree
    //returns true if the insertion was sucessful, otherwise returns false
    public boolean insert(Point p){
        //Check if the given point is within the boundary
        //If it is not, do not insert the point!
        if(!this.boundary.contains(p)){
            return false;
        }
        //Now the point must be within the boundary of the Quadtree
        //otherwise it would not reach this point
        //Check if the maximum capacity of this Quadtree is reached, if not, insert the point!
        if(this.points.size()<capacity){
            this.points.add(p);
           this.count++;
           return true;
        }else{//If the maximum capacity is reached, the quadtree has to subdivide
            if(!this.divided){
                this.subdivide();
                //All points must be reinserted into the new subtrees
                //this makes searching for points more effective
                for(Point point:points){
                    this.insert(point);
                }
                //remove the points from this quadtree, as they were already inserted into the subtrees
                points = new ArrayList<Point>();
            }
        }
        //The Quadtree must be divided to reach this point of code
        //attempt to recursively insert into one of its subtrees
        //only the subtree where the point is within its boundaries will accept the point
        //or one of its child quadtrees
        if(this.northeast.insert(p)) {
            return true;
        }
        else if(this.northwest.insert(p)){
            return true;
        }
        else if(this.southeast.insert(p)){
            return true;
        }
        else if(this.southwest.insert(p)){
            return true;
        }
        return false;

    }
    public void subdivide(){
        //Generate a rectangle for the topleft fourth of the original boundary of the quadtree

        Rectangle ne = new Rectangle(this.boundary.x+this.boundary.w/2, this.boundary.y-this.boundary.h/2,this.boundary.w/2,this.boundary.h/2);
        //Generate a instace of the sub quadtree with those boundarys
        this.northeast = new QuadTree(ne,this.capacity);
        Rectangle nw =  new Rectangle(this.boundary.x-this.boundary.w/2, this.boundary.y-this.boundary.h/2,this.boundary.w/2,this.boundary.h/2);
        this.northwest = new QuadTree(nw,this.capacity);
        Rectangle se = new Rectangle(this.boundary.x+this.boundary.w/2, this.boundary.y+this.boundary.h/2,this.boundary.w/2,this.boundary.h/2);
        this.southeast = new QuadTree(se,this.capacity);
        Rectangle sw = new Rectangle(this.boundary.x-this.boundary.w/2, this.boundary.y+this.boundary.h/2,this.boundary.w/2,this.boundary.h/2);
        this.southwest = new QuadTree(sw,this.capacity);

        //indicate that this quadtree has already divided into sub quadtrees
        this.divided=true;
    }
    //The quadtrees query function recursivly finds all point lying withing the given range
    //It returns all the points within the range
    //Thus can be used to effectivly check for potential collisions etc

    public ArrayList<Point> query(Circle range){//Query by a circle
        ArrayList<Point> found = new ArrayList<>();//List containing all the found points within the range
        //If the given point is not within the boundarys of this quadtree
        //neither this quadtree nor it's children can contain this point
        //thus return an empty array
        if(!this.boundary.intersects(range)){
            return found;
        }else{
            //otherwise check if any of the quadtrees points are within the given search range
            //and add them to the found list
            for(Point p : points){
                if(range.contains(p)){
                    found.add(p);
                }
            }
            //if quadtree subdivided, check child quadtrees recursively
            //and add all found points to the given found list
            if(this.divided){
                found.addAll(this.northwest.query(range));
                found.addAll(this.northeast.query(range));
                found.addAll(this.southwest.query(range));
                found.addAll(this.southeast.query(range));
            }
            //Pass the search result up one level
            return found;
        }
    }

    public ArrayList<Point> query(Rectangle range){//Query by a rectangle

        ArrayList<Point> found = new ArrayList<>();//List containing all the found points within the range
        //If the given point is not within the boundarys of this quadtree
        //neither this quadtree nor it's children can contain this point
        //thus return an empty array
        if(!this.boundary.intersects(range)){
            return found;
        }else{
            //otherwise check if any of the quadtrees points are within the given search range
            //and add them to the found list
            for(Point p : points){
                if(range.contains(p)){
                    found.add(p);
                }
            }
            //if quadtree subdivided, check child quadtrees recursively
            //and add all found points to the given found list
            if(this.divided){
                found.addAll(this.northwest.query(range));
                found.addAll(this.northeast.query(range));
                found.addAll(this.southwest.query(range));
                found.addAll(this.southeast.query(range));
            }
            //Pass the search result up one level
            return found;
        }
    }


}
