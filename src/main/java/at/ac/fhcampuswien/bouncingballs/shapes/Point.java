package at.ac.fhcampuswien.bouncingballs.shapes;
//basic Point class, describing a single point in 2d Space
public class Point {

    public double x;    //y Coordinate
    public double y;    //y Coordinate
    public int id;   //id string needed to identify a given Point, must not be used! Only needed for the Quadtree

    public Point(Point p, int id){
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
    public static Point calculateVectorBetweenTwoPoints(Point a, Point b){
        Point res = new Point(0,0);
        res.x=a.x-b.x;
        res.y=a.y-b.y;
        return res;
    }
    public static Point multiplyVectorByScalar(Point vector,double number){
        Point res = new Point(0,0);
        res.x = vector.x*number;
        res.y = vector.y*number;
        return res;
    }
    public static Point divideVectorByScalar(Point vector,double number){
        Point res = new Point(vector.x,vector.y);
        res.x=vector.x/number;
        res.y=vector.y/number;
        return res;
    }
    public static Point stretchVectorToVelocity(Point vector,double velocity){
        Point res = new Point(0,0);
        double length = calculateLengthOfVector(vector);
        res = divideVectorByScalar(vector,length);
        return multiplyVectorByScalar(res,velocity);
    }
    public static double calculateLengthOfVector(Point vector){
        return Math.sqrt(Math.pow(vector.x,2)+Math.pow(vector.y,2));
    }

}
