package at.ac.fhcampuswien.bouncingballs.balls;

import at.ac.fhcampuswien.bouncingballs.params.SimulationValues;

import java.util.Random;

public class InfectionStats {
    private static int susceptibleBalls= SimulationValues.getBallCount();
    private static int infectedBalls=0;
    private static int removedBalls=0;


    public static int getSusceptibleBalls() {
        return susceptibleBalls;
    }

    public static int getInfectedBalls() {
        return infectedBalls;
    }

    public static int getRemovedBalls() {
        return removedBalls;
    }


    public static int getInfectionRate() {
        return removedBalls+infectedBalls+susceptibleBalls;
    }

    public static void printCurStats(){
        System.out.println("Susceptible:"+susceptibleBalls);
        System.out.println("Infected:"+infectedBalls);
        System.out.println("Removed:"+removedBalls);
    }
}
