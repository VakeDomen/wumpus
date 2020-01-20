package aStar;

import java.util.HashMap;
import java.util.Map;

public class DigraphWeightFuntion {

    private final Map<DigraphNode, Map<DigraphNode, Double>> map = new HashMap<>();

    public void set(DigraphNode tail, DigraphNode head, double weight) {
        map.putIfAbsent(tail, new HashMap<>());
        map.get(tail).put(head,weight);
    }

    public double get(DigraphNode tail, DigraphNode head) {
        if(map.get(tail).get(head) == null) {
            System.out.println("head: " + head.getX() + " " + head.getY() + "  tail: " + tail.getX()  + " " + tail.getY());
            System.out.println(map.get(tail).get(head));
        }
        return map.get(tail).get(head);
    }
}