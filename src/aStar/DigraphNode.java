package aStar;

import game_objects.Tile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DigraphNode {
    private final int id;
    private int x;
    private int y;
    private final Set<DigraphNode> children = new HashSet();
    private Tile t;

    public DigraphNode(int id, int x, int y, Tile t) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.t = t;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        return id == ((DigraphNode) o).id;
    }

    public int hashCode() { return id; }

    public void addNode(DigraphNode child) {
        children.add(child);
    }
    public void removeChild(DigraphNode child) {
        children.remove(child);
    }
    public Set<DigraphNode> getChildren(){
        return Collections.unmodifiableSet(children);
    }

    public boolean compare(int i, int j) {
        return (i==x && j==y);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public Tile getTile() { return t; }

}