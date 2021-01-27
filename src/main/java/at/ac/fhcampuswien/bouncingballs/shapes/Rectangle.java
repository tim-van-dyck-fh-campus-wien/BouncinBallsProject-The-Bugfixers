package at.ac.fhcampuswien.bouncingballs.shapes;

//Basic Rectangle Class, describing a Rectangle in 2d Space
public class Rectangle {

        public double x;//center x
        public double y;//center y
        public double w;//width from center to outer edge, thus half the actual width
        public  double h;//height from center to outer edge, thus half the actual height

        public Rectangle(double x, double y, double w, double h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
        public boolean contains(Point point){
            //check if given point is within the boundaries of this rectangle
            return(point.x>=this.x-this.w && point.x <= this.x+this.w && point.y>=this.y-this.h && point.y <= this.y+this.h);
        }
        //for the search function of the Quadtree => checks if the search range as an rectangle overlaps with the current subsections rectangle!
        public boolean intersects(Rectangle range){
            return !(range.x-range.w > this.x+this.w|| range.x+range.w<this.x-this.w||range.y-range.h > this.y+this.h|| range.y+range.h < this.y -this.h);
        }
        //check if the given circle intersects with the rectangle, this exact method is implemented with the asumption that the boundary is indeed a square
        //thus it may return wrong values when the rectangle is not a square!
        public boolean intersects(Circle range){
            //calculate distance between the circles center and the rectangles center
            double deltaX = this.x-range.x;
            double deltaY = this.y-range.y;
            double distance = Math.sqrt(Math.pow(deltaX,2)+Math.pow(deltaY,2));
            if((distance<(this.h+range.radius))){
                return true;
            }
            return false;
        }


}
