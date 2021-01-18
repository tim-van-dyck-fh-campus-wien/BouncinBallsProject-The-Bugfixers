package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;
import at.ac.fhcampuswien.bouncingballs.shapes.Point;
import at.ac.fhcampuswien.bouncingballs.shapes.Rectangle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Quarantine {
    Rectangle quarantineArea;
    private double startOfQuarantine;
    public boolean quarantineActive = false;
    private static Rectangle tempQuarantinePreviewArea;

    public Quarantine(Point coordinates, double startTime) {
        quarantineArea = new Rectangle(coordinates.x, coordinates.y, SimulationValues.getQuarantineSize(), SimulationValues.getQuarantineSize());
        startOfQuarantine = startTime;
        quarantineActive = true;
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
        if (quarantineActive) {
            double delta = currentTime - this.startOfQuarantine;
            // System.out.println("start of infection is"+startOfInfection);
            //System.out.println(deltaSeconds);
            if (delta > SimulationValues.getQuarantineTime()) {
                this.quarantineActive = false;
                return true;
            }
        }

        return false;
    }
}



