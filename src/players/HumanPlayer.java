package players;

import core.Game;
import game_objects.Position;
import game_objects.Tile;

import javax.crypto.spec.PSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer implements Player {
    @Override
    public Game.Move getMove(Position position, ArrayList<Tile.Effect> exposedEffects) {
        System.out.println("called1");
        Scanner in = new Scanner(System.in);
        Game.Move move = null;
        while(move == null) {
            String input = in.next();
            move = parseMove(input);
        }
        System.out.println("Move: " + move);
        return move;
    }

    @Override
    public Tile[][] getCave() {
        return null;
    }

    private Game.Move parseMove(String input) {
        System.out.println("input: " + input);
        if (input.equals("a"))
            return Game.Move.TURN_LEFT;
        if (input.equals("d"))
            return Game.Move.TURN_RIGHT;
        if (input.equals("w"))
            return Game.Move.MOVE_FORWARD;
        if (input.equals("e"))
            return Game.Move.SHOOT;
        if (input.equals("q"))
            return Game.Move.PICKUP_GOLD;
        return null;
    }
}
