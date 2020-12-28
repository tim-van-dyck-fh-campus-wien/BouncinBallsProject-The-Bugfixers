package at.ac.fhcampuswien.bouncingballs.shapes;

public class Circle {
    public double x;
    public double y;
    public double radius;

    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    //check if a point lies within this circle
    public boolean contains(Point point){
        double deltaX = this.x-point.x;
        double deltaY = this.y-point.y;
        double distance = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
        if((distance<(this.radius))){
            return true;
        }
        //check if given point is within the boundaries of this rectangle
        return false;
    }
}
