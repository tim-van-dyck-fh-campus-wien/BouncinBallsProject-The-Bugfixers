package at.ac.fhcampuswien.bouncingballs.params;

public class SimulationValues {
    private static int ballCount=10000;

    public static int getBallCount() {
        return ballCount;
    }

    public static void setBallCount(int ballCount) {
        SimulationValues.ballCount = ballCount;
    }
}
