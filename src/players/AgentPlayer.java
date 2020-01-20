package players;

import aStar.AgentPathfinding;
import aStar.DigraphNode;
import core.Game;
import game_objects.Position;
import game_objects.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AgentPlayer implements Player {

    private Tile[][] cave;
    private Position position;
    private Position exit;
    private AgentPathfinding pathfinding;

    private boolean gold;
    private boolean shouldPickUp;
    private boolean shouldShoot;
    private boolean shouldTurn;
    private boolean arrow;
    private boolean[][] visited;
    private Tile goal;
    private ArrayList<Tile> clearedByShot;

    public AgentPlayer(int N, int M, Position exit) {
        this.cave = new Tile[N][M];
        this.visited = new boolean[N][M];
        this.exit = exit;
        this.cave = initCave(cave);
        this.gold = false;
        this.shouldPickUp = false;
        this.shouldTurn = false;
        this.arrow = true;
        this.shouldShoot = false;
        this.pathfinding = new AgentPathfinding(new ArrayList<>());
        this.clearedByShot = new ArrayList<>();
    }

    @Override
    public Game.Move getMove(Position currentPosition, ArrayList<Tile.Effect> exposedEffects) {
        addNewKnowledgeToBase(currentPosition, exposedEffects);
        calculateGoal();
        return moveToGoal();
    }

    private void calculateGoal() {
        /*
        if we have gold, we move to the exit
         */
        if (gold) {
            goal = this.cave[exit.getX()][exit.getY()];
        } else {
            // first we check if we found gold
            // if yes => set goal to this tile, so we pick it up
            if (currentTile().getEffects().contains(Tile.Effect.GLITTERY)) {
                this.shouldPickUp = true;
                return;
            }
            // explore safe tiles
            LinkedList<Tile> safeTiles = unvisitedSafeTiles();
            if (safeTiles.size() > 0) {
                goal = safeTiles.pop();
                return;
            }

            //shoot wumpus
            if (arrow) {
                Tile wumpusTile = findWumpus();
                if (wumpusTile != null) {
                    if (areNeighbours(currentTile(), wumpusTile)) {
                        if (facing(wumpusTile) && arrow) {
                            shouldShoot = true;
                        } else {
                            shouldTurn = true;
                        }
                    } else {
                        goal = safeNeighbour(wumpusTile);
                        return;
                    }
                }
            }



        }
    }

    private void addNewKnowledgeToBase(Position position, ArrayList<Tile.Effect> exposed) {
        this.position = position;
        this.visited[this.position.getX()][this.position.getY()] = true;

        // update with effects
        for(Tile.Effect effect : exposed) {
            currentTile().addEffect(effect);
        }

        // update surrounding if safe
        if (safeFromWumpus(currentTile()) && !currentTile().getEffects().contains(Tile.Effect.BREEZY)) {
            markSurroundingTilesAsSafe(currentTile());
        }


        // check if we can deduce safety on fields between smelly and breezy
        for (Tile t : findTilesWithSurroundingSmellyAndBreezy()) {
            markAsSafe(t);
        }

        //find pits
        for (Tile t : checkForCornerTileOfEffect(Tile.Effect.BREEZY)) {
            t.addEffect(Tile.Effect.DANGEROUS_PIT);
        }


        if (!arrow) {
            for (Tile t : smellyButNotBreezy()) {
                markAsSafe(t);
            }
        }

        markAsSafe(currentTile());
    }

    private Game.Move moveToGoal() {
        if (this.shouldPickUp) {
            shouldPickUp = false;
            gold = true;
            return Game.Move.PICKUP_GOLD;
        }
        if (this.shouldShoot) {
            shoot(position.getFacing());
            shouldShoot = false;
            shouldTurn = false;
            return Game.Move.SHOOT;
        }
        if (shouldTurn) {
            shouldTurn = false;
            return Game.Move.TURN_LEFT;
        }
        if (goal != null) {
            if (facing(calcNextTile()))
                return Game.Move.MOVE_FORWARD;
            else
                return Game.Move.TURN_LEFT;
        }

        return Game.Move.TURN_RIGHT;
    }

    private Tile safeNeighbour(Tile tile) {
        if (getTileAbove(tile).getEffects().contains(Tile.Effect.SAFE))
            return getTileAbove(tile);
        if (getTileBellow(tile).getEffects().contains(Tile.Effect.SAFE))
            return getTileBellow(tile);
        if (getTileLeft(tile).getEffects().contains(Tile.Effect.SAFE))
            return getTileLeft(tile);
        if (getTileRight(tile).getEffects().contains(Tile.Effect.SAFE))
            return getTileRight(tile);
        return null;
    }

    private boolean areNeighbours(Tile a, Tile b) {
        return (a == getTileAbove(b) || a == getTileBellow(b) || a == getTileLeft(b) || a == getTileRight(b));
    }


    private ArrayList<Tile> checkForCornerTileOfEffect(Tile.Effect eff) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0 ; i < cave.length ; i++) {
            for (int j = 0 ; j < cave[0].length ; j++) {
                if (cave[i][j].getEffects().contains(eff)) {
                    Tile above = getTileAbove(cave[i][j]);
                    Tile below = getTileBellow(cave[i][j]);
                    Tile left = getTileLeft(cave[i][j]);
                    Tile right = getTileRight(cave[i][j]);
                    if (notExistOrSafe(above, eff) && notExistOrSafe(right,eff) && notExistOrSafe(below, eff))
                        tiles.add(left);
                    if (notExistOrSafe(left, eff) && notExistOrSafe(right,eff) && notExistOrSafe(below, eff))
                        tiles.add(above);
                    if (notExistOrSafe(above, eff) && notExistOrSafe(left,eff) && notExistOrSafe(below, eff))
                        tiles.add(right);
                    if (notExistOrSafe(above, eff) && notExistOrSafe(right,eff) && notExistOrSafe(left, eff))
                        tiles.add(below);
                }
            }
        }
        return tiles;
    }

    private boolean safeFromWumpus(Tile t) {
        return !t.getEffects().contains(Tile.Effect.SMELLY)
                || (!arrow && !t.getEffects().contains(Tile.Effect.DANGEROUS_PIT) && !t.getEffects().contains(Tile.Effect.BREEZY));
    }

    private ArrayList<Tile> smellyButNotBreezy() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0 ; i < cave.length ; i++) {
            for (int j = 0 ; j < cave[i].length ; j++) {
                if (cave[i][j].getEffects().contains(Tile.Effect.SMELLY) && !cave[i][j].getEffects().contains(Tile.Effect.BREEZY)) {
                    tiles.add(cave[i][j]);
                }
            }
        }
        return tiles;
    }

    private boolean notExistOrSafe(Tile t, Tile.Effect eff) {
        return (t == null || (visited[t.getPosition().getX()][t.getPosition().getY()] && !t.getEffects().contains(eff)));
    }

    private LinkedList<Tile> unvisitedSafeTiles() {
        LinkedList<Tile> tiles = new LinkedList<>();
        for (int i = 0 ; i < cave.length ; i++) {
            for (int j = 0 ; j < cave[i].length ; j++) {
                if (cave[i][j].getEffects().contains(Tile.Effect.SAFE) && !visited[i][j]) {
                    tiles.push(cave[i][j]);
                }
            }
        }
        return tiles;
    }

    private Tile findWumpus() {
        for (Tile t : checkForCornerTileOfEffect(Tile.Effect.SMELLY)) {
            if (t != null) {
                if (!clearedByShot.contains(t)) {
                    t.addEffect(Tile.Effect.DANGEROUS_WUMPUS);
                    return t;
                } else {
                    markAsSafe(t);
                }
            }
        }

        ArrayList<Tile> smelly = findTilesByEffect(Tile.Effect.SMELLY);
        for (Tile t1 : smelly) {
            for (Tile t2 : smelly) {
                if (t1 != t2) {
                    ArrayList<Tile> neighbours = findCommonNeighbours(t1, t2);
                    if (neighbours.size() == 1) {
                        neighbours.get(0).addEffect(Tile.Effect.DANGEROUS_WUMPUS);;
                    } else if (neighbours.size() == 2) {
                        if (neighbours.get(0).getEffects().contains(Tile.Effect.SAFE) && !neighbours.get(1).getEffects().contains(Tile.Effect.SAFE))
                            neighbours.get(1).addEffect(Tile.Effect.DANGEROUS_WUMPUS);
                        else if (neighbours.get(1).getEffects().contains(Tile.Effect.SAFE) && !neighbours.get(0).getEffects().contains(Tile.Effect.SAFE))
                            neighbours.get(0).addEffect(Tile.Effect.DANGEROUS_WUMPUS);
                    }
                }
            }
        }


        for (int i = 0 ; i < cave.length ; i++) {
            for (int j = 0 ; j < cave[i].length ; j++) {
                if (cave[i][j].getEffects().contains(Tile.Effect.DANGEROUS_WUMPUS)) {
                    return cave[i][j];
                }
            }
        }
        return null;
    }

    private ArrayList<Tile> findCommonNeighbours(Tile t1, Tile t2) {
        ArrayList<Tile> common = new ArrayList<>();
        ArrayList<Tile> t1n = neighbours(t1);
        ArrayList<Tile> t2n = neighbours(t2);
        for (Tile n1 : t1n) {
            for (Tile n2 : t2n) {
                if (n1 == n2) {
                    common.add(n1);
                }
            }
        }
        return common;
    }

    private ArrayList<Tile> findTilesByEffect(Tile.Effect eff) {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0 ; i < cave.length ; i++) {
            for (int j = 0 ; j < cave[i].length ; j++) {
                if (cave[i][j].getEffects().contains(eff)) {
                    tiles.add(cave[i][j]);
                }
            }
        }
        return tiles;
    }

    private ArrayList<Tile> findTilesWithSurroundingSmellyAndBreezy() {
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = 0 ; i < cave.length ; i++) {
            for (int j = 0 ; j < cave[i].length ; j++) {
                if (!visited[i][j]) {
                    ArrayList<Tile> neighbours = neighbours(cave[i][j]);
                    boolean onlyBreezy = false;
                    boolean onlySmelly = false;
                    for (Tile t : neighbours) {
                        if (t.getEffects().contains(Tile.Effect.BREEZY) && !t.getEffects().contains(Tile.Effect.SMELLY))
                            onlyBreezy = true;
                        if (!t.getEffects().contains(Tile.Effect.BREEZY) && t.getEffects().contains(Tile.Effect.SMELLY))
                            onlySmelly = true;
                    }
                    if (onlyBreezy && onlySmelly) {
                        tiles.add(cave[i][j]);
                    }
                }
            }
        }
        return tiles;
    }

    private void markAsSafe(Tile t) {
        t.addEffect(Tile.Effect.SAFE);
        pathfinding.addTile(t);
    }

    private boolean facing(Tile tile) {
        if (tile == null)
            return false;
        return  ((this.position.getFacing() == Position.Direction.UP &&         this.position.getY() - 1 == tile.getPosition().getY())
                || (this.position.getFacing() == Position.Direction.DOWN &&     this.position.getY() + 1 == tile.getPosition().getY())
                || (this.position.getFacing() == Position.Direction.LEFT &&     this.position.getX() - 1 == tile.getPosition().getX())
                || (this.position.getFacing() == Position.Direction.RIGHT &&    this.position.getX() + 1 == tile.getPosition().getX()));
    }

    private ArrayList<Tile> neighbours(Tile t) {
        ArrayList<Tile> neigh = new ArrayList<>();
        Tile above = getTileAbove(t);
        Tile below = getTileBellow(t);
        Tile left = getTileLeft(t);
        Tile right = getTileRight(t);
        if (above != null)  neigh.add(above);
        if (below != null)  neigh.add(below);
        if (left != null)  neigh.add(left);
        if (right != null)  neigh.add(right);
        return neigh;
    }

    private void markSurroundingTilesAsSafe(Tile currentTile) {
        Tile above = getTileAbove(currentTile);
        Tile below = getTileBellow(currentTile);
        Tile left = getTileLeft(currentTile);
        Tile right = getTileRight(currentTile);
        if (above != null) {
            markAsSafe(above);
        }
        if (below != null) {
            markAsSafe(below);
        }
        if (left != null) {
            markAsSafe(left);
        }
        if (right != null) {
            markAsSafe(right);
        }
    }


    private void shoot(Position.Direction direction) {
        clearedByShot = new ArrayList<>();
        arrow = false;
        if (direction == Position.Direction.UP) {
            for (int i = position.getY() ; i > -1 ; i--) {
                clearedByShot.add(cave[position.getX()][i]);
            }
            return;
        }
        if (direction == Position.Direction.DOWN) {
            for (int i = position.getY() ; i < cave[0].length ; i++) {
                clearedByShot.add(cave[position.getX()][i]);
            }
            return;
        }
        if (direction == Position.Direction.LEFT) {
            for (int i = position.getX() ; i > -1 ; i--) {
                clearedByShot.add(cave[i][position.getY()]);
            }
            return;
        }
        if (direction == Position.Direction.RIGHT) {
            for (int i = position.getX() ; i < cave.length ; i++) {
                clearedByShot.add(cave[i][position.getY()]);
            }
            return;
        }
    }

    private Tile calcNextTile() {
        ArrayList<DigraphNode> path = this.pathfinding.findPath(currentTile(), goal);
        if (path != null && path.size() > 1)
            return path.get(1).getTile();
        return null;
    }

    private Tile getTileAbove(Tile tile) {
        if (tile.getPosition().getY() > 0)
            return cave[tile.getPosition().getX()][tile.getPosition().getY() - 1];
        return null;
    }

    private Tile getTileBellow(Tile tile) {
        if (tile.getPosition().getY() < cave[tile.getPosition().getX()].length - 1)
            return cave[tile.getPosition().getX()][tile.getPosition().getY() + 1];
        return null;
    }

    private Tile getTileLeft(Tile tile) {
        if (tile.getPosition().getX() > 0)
            return cave[tile.getPosition().getX() - 1][tile.getPosition().getY()];
        return null;
    }

    private Tile getTileRight(Tile tile) {
        if (tile.getPosition().getX() < cave.length - 1)
            return cave[tile.getPosition().getX() + 1][tile.getPosition().getY()];
        return null;
    }

    @Override
    public Tile[][] getCave() {
        return cave;
    }

    private Tile[][] initCave(Tile[][] cave) {
        for (int i = 0 ; i < cave.length ; i++) {
            for (int j = 0 ; j < cave[0].length ; j++) {
                cave[i][j] = new Tile(i, j, Tile.Type.EMPTY);
                visited[i][j] = false;
            }
        }
        return cave;
    }

    private Tile currentTile() {
        return cave[position.getX()][position.getY()];
    }

    private boolean samePosition(Position a, Position b) {
        return (a.getX() == b.getX() && a.getY() == b.getY());
    }
}
