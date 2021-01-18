package at.ac.fhcampuswien.bouncingballs.balls;

public class GraphStats {

    public double percentInfected;
    public double percentSuspectible;
    public double percentRemoved;

    public GraphStats(double percentInfected, double percentSuspectible, double percentRemoved) {
        this.percentInfected = percentInfected;
        this.percentSuspectible = percentSuspectible;
        this.percentRemoved = percentRemoved;
    }
}
