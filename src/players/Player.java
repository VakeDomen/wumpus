package players;

import core.Game;
import game_objects.Position;
import game_objects.Tile;

import java.util.ArrayList;

public interface Player {

    Game.Move getMove(Position position, ArrayList<Tile.Effect> exposedEffects);
    Tile[][] getCave();

}
