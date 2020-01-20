package aStar;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class DigraphCoordinates {
    private final Map<DigraphNode, Point2D.Double> map = new  HashMap<>();

    public void put(DigraphNode node, Point2D.Double point) {
        map.put(node, point);
    }

    public Point2D.Double get(DigraphNode node){
        return map.get(node);
    }
}