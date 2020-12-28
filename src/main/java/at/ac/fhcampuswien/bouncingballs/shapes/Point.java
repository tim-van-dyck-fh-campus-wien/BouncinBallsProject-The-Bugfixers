package at.ac.fhcampuswien.bouncingballs.shapes;
//basic Point class, describing a single point in 2d Space
public class Point {

    public double x;    //y Coordinate
    public double y;    //y Coordinate
    public String id;   //id string needed to identify a given Point, must not be used! Only needed for the Quadtree
    public Point(double x, double y,String id){
        this.x=x;
        this.y=y;
        this.id=id;
    }
    public Point(Point p, String id){
        this.x=p.x;
        this.y=p.y;
        this.id=id;
    }
    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }
    public boolean Intersects(float r, float x, float y){
        double dx=Math.abs(this.x-x);
        double dy=Math.abs(this.y-y);
        double rBetween=Math.sqrt(dx*dx+dy*dy);
        if(rBetween<2*r){
            return true;
        }else{
            return false;
        }

    }

}
