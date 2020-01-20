package aStar;

public interface HeuristicFunction {

    public double getEstimate(DigraphNode node1, DigraphNode node2);

}