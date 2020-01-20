package aStar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarPathFinder {

    private static final class HeapEntry implements Comparable<HeapEntry>{
        private final DigraphNode node;
        private final double distance;

        HeapEntry(DigraphNode node, double distance){
            this.node = node;
            this.distance = distance;
            ;		}

        DigraphNode getNode() {
            return node;
        }

        @Override
        public int compareTo(HeapEntry o) {
            return Double.compare(distance, o.distance);
        }
    }

    public static ArrayList<DigraphNode> search(
            DigraphNode source,
            DigraphNode target,
            DigraphWeightFuntion weightFunction,
            HeuristicFunction heuristicFunction
    ){
        PriorityQueue<HeapEntry> OPEN = new PriorityQueue<>();
        Set<DigraphNode> CLOSED = new HashSet<>();
        Map<DigraphNode, Double> DISTANCE = new HashMap<>();
        Map<DigraphNode, DigraphNode> PARENTS = new HashMap();
        OPEN.add(new HeapEntry(source, 0.0));
        DISTANCE.put(source, 0.0);
        PARENTS.put(source, null);

        while(!OPEN.isEmpty()) {
            DigraphNode currentNode = OPEN.remove().getNode();
            if( currentNode.equals(target)) {
                return traceBackPath(currentNode, PARENTS);
            }
            if(CLOSED.contains(currentNode)) {
                continue;
            }
            for(DigraphNode childNode : currentNode.getChildren()) {
                if(CLOSED.contains(childNode)) {
                    continue;
                }
                double tentativeDistance = DISTANCE.get(currentNode) + weightFunction.get(currentNode, childNode);
                if(!DISTANCE.containsKey(childNode) || DISTANCE.get(childNode) > tentativeDistance) {
                    DISTANCE.put(childNode, tentativeDistance);
                    PARENTS.put(childNode, currentNode);
                    OPEN.add(new HeapEntry(childNode, tentativeDistance + heuristicFunction.getEstimate(childNode, target)));
                }
            }
        }
        return new ArrayList<>();
    }

    private static ArrayList<DigraphNode> traceBackPath(DigraphNode target, Map<DigraphNode, DigraphNode> PARENTS){
        ArrayList<DigraphNode> path = new ArrayList<>();
        DigraphNode currentNode = target;

        while (currentNode != null) {
            path.add(currentNode);
            currentNode = PARENTS.get(currentNode);
        }
        Collections.reverse(path);
        return path;
    }
}