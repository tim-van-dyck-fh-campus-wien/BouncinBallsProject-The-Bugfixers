package at.ac.fhcampuswien.bouncingballs.balls;

import java.awt.geom.Point2D;

public class InfectableBalls {
    enum InfectionStatus {
        SUSCEPTIBLE,
        INFECTED,
        REMOVED
    }
    InfectionStatus infectionStatus = InfectionStatus.SUSCEPTIBLE;
    Point2D.Double coordinate = new Point2D.Double(0,0);

}
