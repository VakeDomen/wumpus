package aStar;

import game_objects.Tile;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class AgentPathfinding {

    private DigraphWeightFuntion weightFunction;
    private HeuristicFunction hf;
    private DigraphCoordinates coordinates;
    private ArrayList<DigraphNode> deleted;
    private ArrayList<DigraphNode> graph;
    private ArrayList<Tile> tiles;

    public AgentPathfinding(ArrayList<Tile> tiles) {
        this.tiles = tiles;
        initGraph();
    }


    public ArrayList<DigraphNode> findPath(Tile start, Tile end) {
        return AStarPathFinder.search(findNode(start), findNode(end), weightFunction, hf);
    }

    private void initGraph() {
        createNodes();
        connectNodes();
        createDigraphCoords();
        createWeightFunction();

        this.deleted = new ArrayList<>();
        this.hf = new EuclidianHeuristicFunction(coordinates, deleted);
    }

    private void createWeightFunction() {
        this.weightFunction = new DigraphWeightFuntion();
        for(DigraphNode node : graph) {
            Point2D.Double p1 = coordinates.get(node);
            for(DigraphNode child : node.getChildren()) {
                Point2D.Double p2 = coordinates.get(child);
                this.weightFunction.set(node, child, weightDiagonal(node, child));
            }
        }
    }

    private void createDigraphCoords() {
        this.coordinates = new DigraphCoordinates();
        for(DigraphNode node : graph) {
            this.coordinates.put(node, new Point2D.Double(node.getX()*1.0,node.getY()*1.0));
        }
    }

    private void connectNodes() {
        for (DigraphNode node : graph) {
            ArrayList<DigraphNode> neighbours = findNeighbours(node);
            for (DigraphNode neigh : neighbours) {
                node.addNode(neigh);
            }
        }
    }

    protected int weightDiagonal(DigraphNode head, DigraphNode tail) {
        int x = Math.abs(head.getX()-tail.getX());
        int y = Math.abs(head.getY()-tail.getY());
        //we don't want diagonals
        if(x+y == 2) return 99;
        else return 10;
    }

    private ArrayList<DigraphNode> findNeighbours(DigraphNode node) {
        ArrayList<DigraphNode> neighbours = new ArrayList<>();
        for (DigraphNode n : graph) {
            if ( neighbours(node, n)) {
                neighbours.add(n);
            }
        }
        return neighbours;
    }

    private boolean neighbours(DigraphNode n1, DigraphNode n2) {
        int x1 = n1.getTile().getPosition().getX();
        int y1 = n1.getTile().getPosition().getY();
        int x2 = n2.getTile().getPosition().getX();
        int y2 = n2.getTile().getPosition().getY();
        return ((x1 == x2 && Math.abs(y1 - y2) == 1) || (y1 == y2 && Math.abs(x1 - x2) == 1));
    }

    private void createNodes() {
        this.graph = new ArrayList<>(tiles.size());
        int id = 0;
        for(Tile tile : tiles) {
            this.graph.add(new DigraphNode(id, tile.getPosition().getX(), tile.getPosition().getY(), tile));
            id++;
        }
    }

    public void addTile(Tile t) {
        if (!this.tiles.contains(t)) {
            this.tiles.add(t);
            initGraph();
        }
    }

    private DigraphNode findNode(Tile t) {
        for (DigraphNode node : this.graph) {
            if (t == node.getTile()) {
                return node;
            }
        }
        return null;
    }
}
