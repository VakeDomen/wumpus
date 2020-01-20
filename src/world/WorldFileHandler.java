package world;

import game_objects.Position;
import game_objects.World;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class WorldFileHandler {

    private static String mapsDir = "./src/world/maps/";
    private static BufferedReader reader;


    public static World getWorld(String name) {
        File file = new File(mapsDir + name);
        int N = 0;
        int M = 0;
        Position player = null;
        Position wumpus = null;
        Position gold = null;
        LinkedList<Position> pits = new LinkedList<>();
        Position exit = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line == "\n")
                    continue;


                if (line.charAt(0) == 'M') {
                    N = Integer.parseInt(String.valueOf(line.charAt(1)));
                    M = Integer.parseInt(String.valueOf(line.charAt(2)));
                    continue;
                }

                //agent
                if (line.charAt(0) == 'A') {
                    int x = Integer.parseInt(String.valueOf(line.charAt(1)));
                    int y = Integer.parseInt(String.valueOf(line.charAt(2)));
                    player = new Position(x, y);
                    continue;
                }

                //wumpus
                if (line.charAt(0) == 'W') {
                    int x = Integer.parseInt(String.valueOf(line.charAt(1)));
                    int y = Integer.parseInt(String.valueOf(line.charAt(2)));
                    wumpus = new Position(x, y);
                    continue;
                }

                //pits
                if (line.charAt(0) == 'P') {
                    int x = Integer.parseInt(String.valueOf(line.charAt(1)));
                    int y = Integer.parseInt(String.valueOf(line.charAt(2)));
                    Position pit = new Position(x, y);
                    pits.add(pit);
                    continue;
                }

                // gold and exit
                if (line.charAt(0) == 'G') {

                    //exit
                    if (line.charAt(1) == 'O') {
                        int x = Integer.parseInt(String.valueOf(line.charAt(2)));
                        int y = Integer.parseInt(String.valueOf(line.charAt(3)));
                        exit = new Position(x, y);
                        continue;
                    }

                    //gold
                    int x = Integer.parseInt(String.valueOf(line.charAt(1)));
                    int y = Integer.parseInt(String.valueOf(line.charAt(2)));
                    gold = new Position(x, y);
                    continue;
                }


            }
        } catch (Exception e) {
            System.out.println("Failed loading map...");
            e.printStackTrace();
        }

        if (N == 0 || M == 0 || player == null || exit == null || gold == null || wumpus == null) {
            System.out.println("Invalid world file!");
            System.out.println("N: " + N + " | M: " + M + " | player: " + player.toStringPlayer() + " | exit: " + exit.toString() + " | gold: " + gold.toString() + " | wumpus: " + wumpus.toString());
            return null;
        }

        Position[] pitArray = new Position[pits.size()];
        pits.toArray(pitArray);

        System.out.println("N: " + N + " | M: " + M + " | player: " + player.toStringPlayer() + " | exit: " + exit.toString() + " | gold: " + gold.toString() + " | wumpus: " + wumpus.toString());
        return new World(N, M, player, wumpus, gold, pitArray, exit);
    }
}
