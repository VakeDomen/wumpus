package core;

import game_objects.Position;
import game_objects.Tile;
import game_objects.World;
import players.AgentPlayer;
import players.Player;

import java.awt.*;

public class Game {

    private World world;
    private Player player;

    public Game(World world, Player player) {
        this.world = world;
        this.player = player;
    }

    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    public void setWorld(World world) {
        this.world = world;
    }


    public enum Move {
        TURN_LEFT,
        TURN_RIGHT,
        MOVE_FORWARD,
        SHOOT,
        PICKUP_GOLD
    }

    public void start() {
        System.out.println("Starting game");
        gameLoop();
        System.out.println("Game ended");
    }

    public void stop() {

    }

    public void restart() {

    }

    private void gameLoop() {
        while (!isGameOver()) {
            Move move = getPlayerMove();
            applyMove(move);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (playerWon()) {
            System.out.println("Player won!");
        } else if (playerDead()) {
            System.out.println("Player died!");
        } else {
            System.out.println("something wired happened");
        }
    }

    private Move getPlayerMove() {
        return this.player.getMove(world.getPlayer(), world.getEffects(world.getPlayer()));
    }

    private void applyMove(Move move) {
        if (move == Move.TURN_LEFT || move == Move.TURN_RIGHT) {
            world.turnPlayer(move);
        } else if (move == Move.MOVE_FORWARD) {
            world.movePlayerForward();
        } else if (move == Move.SHOOT) {
            world.playerShoot();
        } else if (move == Move.PICKUP_GOLD) {
            world.pickUpGold();
        }
    }

    private boolean isGameOver() {
        return (playerWon() || playerDead());
    }

    public boolean playerWon(){
        return (world.isPlayerAtExit() && world.playerHasGold());
    }

    public boolean playerDead() {
        return world.isPlayerDead();
    }

    public Tile[][] getCave() {
        return world.getCave();
    }

    public Position getPlayer() {
        return world.getPlayer();
    }

    public Tile[][] getPlayerCave() {
        return player.getCave();
    }
}
