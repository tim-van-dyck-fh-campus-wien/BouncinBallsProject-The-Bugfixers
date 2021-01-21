package at.ac.fhcampuswien.bouncingballs.params;

public class InfectableBallsParams {

    public static int ballradius;
    //pixel per 100ths of a second
    public static double velocity=0.2;

    public static void setBallradius(int ballradius) {
        InfectableBallsParams.ballradius = ballradius;
    }

    public static int getBallradius() {
        return ballradius;
    }


}
