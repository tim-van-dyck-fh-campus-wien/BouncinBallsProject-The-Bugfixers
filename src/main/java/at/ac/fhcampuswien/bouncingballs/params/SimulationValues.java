package at.ac.fhcampuswien.bouncingballs.params;

public class SimulationValues {
    private static int ballCount=900;
    private static double timeToRecover=5;
    private static int initalInfections=1;
    private static double quarantineTime = 5;//Time the quarantine lasts
    private static double quarantineSize=100;//Size of the Rectangle defining the quarantine

    public static int getInitalInfections() {
        return initalInfections;
    }

    public static double getQuarantineTime() {
        return quarantineTime;
    }

    public static double getQuarantineSize() {
        return quarantineSize;
    }

    public static void setInitalInfections(int initalInfections) {
        SimulationValues.initalInfections = initalInfections;
    }

    public static double getTimeToRecover() {
        return timeToRecover;
    }

    public static void setTimeToRecover(double timeToRecover) {
        SimulationValues.timeToRecover = timeToRecover;
    }

    //Time a ball needs to recover from an infection in Seconds

    public static int getBallCount() {
        return ballCount;
    }

    public static void setBallCount(int ballCount) {
        SimulationValues.ballCount = ballCount;
    }
}
