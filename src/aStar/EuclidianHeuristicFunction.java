package aStar;

import java.util.ArrayList;
import java.util.List;

public class EuclidianHeuristicFunction implements HeuristicFunction{

    private final DigraphCoordinates coordinates;
    private final ArrayList<DigraphNode> deleted;

    public EuclidianHeuristicFunction (DigraphCoordinates coordinates, ArrayList<DigraphNode> deleted) {
        this.coordinates = coordinates;
        this.deleted = deleted;
    }

    @Override
    public double getEstimate(DigraphNode node1, DigraphNode node2) {
        double dist = coordinates.get(node1).distance(coordinates.get(node2));
        int x = node2.getX() - node1.getX();
        int y = node2.getY() - node1.getY();
        if(dist!=1.0 && (deleted.contains(choose(deleted, node1.getX()+x, node1.getY())) || deleted.contains(choose(deleted, node1.getX(), node1.getY()+y)))) {
            dist = 99;
        }
        return dist;
    }

    private static DigraphNode choose(List<DigraphNode> list, int i, int j) {
        for(DigraphNode el : list) {
            if(el.compare(i, j)) {
                return el;
            }
        }
        return null;
    }
}