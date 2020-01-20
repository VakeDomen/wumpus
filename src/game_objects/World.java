package game_objects;

import core.Game;

import java.util.ArrayList;

public class World {

    private int M;
    private int N;

    private Position player;
    private Position exit;
    private Position wumpus;
    private Position gold;
    private Position[] pits;

    private Tile[][] cave;
    private boolean playerHasArrow;
    private boolean playerHasGold;

    public World(int N, int M, Position player, Position wumpus, Position gold, Position[] pits, Position exit) {
        this.cave = new Tile[N][M];
        this.N = N;
        this.M = M;
        this.exit = exit;
        this.player = player;
        this.wumpus = wumpus;
        this.gold = gold;
        this.pits = pits;
        this.cave = initCave(N, M, wumpus, gold, pits);
        this.playerHasArrow = true;
        this.playerHasGold = false;
    }



    public void playerShoot() {
        if (playerHasArrow) {
            shoot(player.getFacing());
            playerHasArrow = false;
        } else {
            System.out.println("No arrow");
        }
    }

    public void turnPlayer(Game.Move turnMove) {
        if (turnMove == Game.Move.TURN_LEFT) {
            player.turnLeft();
        } else {
            player.turnRight();
        }
    }

    public void pickUpGold() {
        if (cave[player.getX()][player.getY()].pickUpGold()){
            playerHasGold = true;
        } else {
            System.out.println("No gold!");
        }
    }

    public boolean playerHasGold() {
        return playerHasGold;
    }

    public boolean isPlayerAtExit() {
        return (player.getX() == exit.getX() && player.getY() == exit.getY());
    }

    public boolean isPlayerDead() {
        return cave[player.getX()][player.getY()].killsPlayer();
    }

    public void movePlayerForward() {
        if (player.getFacing() == Position.Direction.UP) {
            if (player.getY() - 1 > -1) {
                player.setY(player.getY() - 1);
            } else {
                System.out.println("Wall up");
            }
            return;
        }
        if (player.getFacing() == Position.Direction.DOWN) {
            if (player.getY() + 1 < M) {
                player.setY(player.getY() + 1);
            } else {
                System.out.println("wall down");
            }
            return;
        }
        if (player.getFacing() == Position.Direction.LEFT) {
            if (player.getX() - 1 > -1) {
                player.setX(player.getX() - 1);
            } else {
                System.out.println("wall left");
            }
            return;
        }
        if (player.getFacing() == Position.Direction.RIGHT) {
            if (player.getX() + 1 < N) {
                player.setX(player.getX() + 1);
            } else {
                System.out.println("wall right");
            }
            return;
        }
    }

    private Tile[][] initCave(int N, int M, Position wumpus, Position gold, Position[] pits) {

        Tile[][] cave = new Tile[N][M];
        //generate empty
        for (int i = 0 ; i < N ; i++) {
            for (int j = 0; j < M; j++) {
                cave[i][j] = new Tile(i, j, Tile.Type.EMPTY);
            }
        }
        //add special tiles
        cave[wumpus.getX()][wumpus.getY()] = new Tile(wumpus.getX(), wumpus.getY(), Tile.Type.WUMPUS_ALIVE);
        cave[gold.getX()][gold.getY()] = new Tile(gold.getX(), gold.getY(), Tile.Type.GOLD);
        for (Position pit : pits) {
            cave[pit.getX()][pit.getY()] = new Tile(pit.getX(), pit.getY(), Tile.Type.PIT);
        }

        cave = spreadEffect(wumpus, cave);
        for (Position pit : pits) {
            cave = spreadEffect(pit, cave);
        }
        return cave;
    }

    private Tile[][] spreadEffect(Position tile, Tile[][] cave) {
        Tile.Effect tileEffect = cave[tile.getX()][tile.getY()].getEffectEmition();
        if (tile.getX() - 1 > -1) {
            cave[tile.getX() - 1][tile.getY()].addEffect(tileEffect);
        }
        if (tile.getX() + 1 < N) {
            cave[tile.getX() + 1][tile.getY()].addEffect(tileEffect);
        }
        if (tile.getY() - 1 > -1) {
            cave[tile.getX()][tile.getY() - 1].addEffect(tileEffect);
        }
        if (tile.getY() + 1 < M) {
            cave[tile.getX()][tile.getY() + 1].addEffect(tileEffect);
        }
        return cave;
    }

    private void shoot(Position.Direction direction) {
        if (direction == Position.Direction.UP) {
            for (int i = player.getY() ; i > -1 ; i--) {
                cave[player.getX()][i].killWumpus();
            }
            return;
        }
        if (direction == Position.Direction.DOWN) {
            for (int i = player.getY() ; i < M ; i++) {
                cave[player.getX()][i].killWumpus();
            }
            return;
        }
        if (direction == Position.Direction.LEFT) {
            for (int i = player.getX() ; i > -1 ; i--) {
                cave[i][player.getY()].killWumpus();
            }
            return;
        }
        if (direction == Position.Direction.RIGHT) {
            for (int i = player.getX() ; i < N ; i++) {
                cave[i][player.getY()].killWumpus();
            }
            return;
        }
    }

    public Tile[][] getCave() {
        return cave;
    }

    public Position getPlayer() {
        return player;
    }

    public ArrayList<Tile.Effect> getEffects(Position player) {
        return cave[player.getX()][player.getY()].getEffects();
    }

    public int getN() {
        return N;
    }

    public int getM() {
        return M;
    }

    public Position getExit() {
        return this.exit;
    }
}
