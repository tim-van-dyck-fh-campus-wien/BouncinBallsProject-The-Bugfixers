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
    public static void infektionsgeschehen(){
        Random rn = new Random();
       int rand =  rn.nextInt(100);
       //if random number 0 => ball gets infected
       if(rand==0){
           if(susceptibleBalls>0){
               susceptibleBalls-=1;
               infectedBalls+=1;
           }
       }else if(rand==1){
           if(infectedBalls>0){
               infectedBalls-=1;
               removedBalls+=1;
           }
       }


    }
    public static void printCurStats(){
        System.out.println("Susceptible:"+susceptibleBalls);
        System.out.println("Infected:"+infectedBalls);
        System.out.println("Removed:"+removedBalls);
        System.out.println();
    }
}
