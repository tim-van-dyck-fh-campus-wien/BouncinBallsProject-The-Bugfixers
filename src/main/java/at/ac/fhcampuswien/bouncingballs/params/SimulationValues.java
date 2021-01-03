package at.ac.fhcampuswien.bouncingballs.params;

public class SimulationValues {
    private static int ballCount=900;
    private static double timeToRecover=5;
    private static int initalInfections=1;

    public static int getInitalInfections() {
        return initalInfections;
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
