import core.Game;
import game_objects.World;
import core.Gui;
import players.AgentPlayer;
import players.HumanPlayer;
import world.WorldFileHandler;

public class Main {

    public static Gui.View view = Gui.View.OVERVIEW;

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("agent")) {
                view = Gui.View.PLAYER_VIEW;
            }
        }

        World world = WorldFileHandler.getWorld("wumpus_world_1.txt");
        Game wumpusGame = new Game(world, new AgentPlayer(world.getN(), world.getM(), world.getExit()));
        Gui gfx = new Gui(wumpusGame, view);
        wumpusGame.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        world = WorldFileHandler.getWorld("wumpus_world_3.txt");
        wumpusGame.setWorld(world);
        wumpusGame.setPlayer( new AgentPlayer(world.getN(), world.getM(), world.getExit()));
        wumpusGame.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            world = WorldFileHandler.getWorld("wumpus_world_2.txt");
            wumpusGame.setWorld(world);
            wumpusGame.setPlayer( new AgentPlayer(world.getN(), world.getM(), world.getExit()));
            wumpusGame.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
