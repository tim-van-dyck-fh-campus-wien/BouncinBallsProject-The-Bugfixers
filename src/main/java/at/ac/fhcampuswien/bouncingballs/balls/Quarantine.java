package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import at.ac.fhcampuswien.bouncingballs.shapes.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//The Class representing the Quarantine Area activated by clicking the quarantine button
public class Quarantine {
    Rectangle quarantineArea;//Outline of the Quarantined Area
    private double startOfQuarantine;//starttime of the quarantine
    public boolean quarantineActive = false;
    private static Rectangle tempQuarantinePreviewArea;//used for drawing the outline of the quarantine area before it has beeen placed by clicking

    public Quarantine(Point coordinates, double startTime) {
        quarantineArea = new Rectangle(coordinates.x, coordinates.y, SimulationValues.getQuarantineSize(), SimulationValues.getQuarantineSize());
        startOfQuarantine = startTime;
        quarantineActive = true;
    }

    public void resetQuarantine(){
        quarantineActive=false;
    }

    public GraphicsContext draw(GraphicsContext gc) {
        if (quarantineActive) {
            gc.setStroke(Color.GREENYELLOW);
            gc.strokeRect(quarantineArea.x - (quarantineArea.w), quarantineArea.y - (quarantineArea.h), quarantineArea.w * 2, quarantineArea.h * 2);
        }
        return gc;
    }


    public static void setPreviewAreaCoordinates(Point cord){
        tempQuarantinePreviewArea = new Rectangle(cord.x,cord.y,SimulationValues.getQuarantineSize(),SimulationValues.getQuarantineSize());
    }

    public static GraphicsContext drawQuarantinePreviewArea(GraphicsContext gc){
        gc.setStroke(Color.DARKGOLDENROD);
        if(tempQuarantinePreviewArea!=null){
            gc.strokeRect(tempQuarantinePreviewArea.x - (tempQuarantinePreviewArea.w), tempQuarantinePreviewArea.y - (tempQuarantinePreviewArea.h), tempQuarantinePreviewArea.w * 2, tempQuarantinePreviewArea.h * 2);
        }
        return gc;
    }

    //returns true if quarantine just ended otherwise false
    public boolean refreshQuarantineStatus(double currentTime) {
        if (quarantineActive) {//Checks Quarantine status every timestep
            double delta = currentTime - this.startOfQuarantine;
            if (delta > SimulationValues.getQuarantineTime()) {
                this.quarantineActive = false;
                return true;
            }
        }

        return false;
    }
}



