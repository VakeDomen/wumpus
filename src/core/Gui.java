package core;

import core.Game;
import game_objects.Position;
import game_objects.Tile;

import javax.swing.*;
import java.awt.*;
import java.io.ObjectInputFilter;

public class Gui extends JPanel {

    private JFrame f;
    private int tileWidth;
    private int tileHeight;
    private View view = View.PLAYER_VIEW;

    private int height = 1024;

    private Game game;

    enum View {
        OVERVIEW,
        PLAYER_VIEW
    }


    public Gui(Game wumpusGame) {
        this.game = wumpusGame;
        initFrame();
        initGui();
    }


    private void initFrame() {
        f = new JFrame("Wumpus game");
        f.setPreferredSize(new Dimension(calcWidth(height), height));
        f.pack();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setVisible(true);
    }

    private void initGui() {
        this.setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        calcWidth(height);
        Tile[][] cave = null;
        if (view == View.OVERVIEW) {
            cave = game.getCave();
        }
        if (view == View.PLAYER_VIEW) {
            cave = game.getPlayerCave();
        }
        drawBoard(g2d, cave);
        drawPlayer(g2d, cave);
        drawCredits(g2d);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.repaint();
    }

    private void drawCredits(Graphics2D g2d) {
        if (game.playerDead())
            g2d.drawString("Player died!", height/2, calcWidth(height)/2);
        if (game.playerWon())
            g2d.drawString("Player won!", height/2, calcWidth(height)/2);
    }

    private void drawPlayer(Graphics2D g2d, Tile[][] cave) {
        Position player = game.getPlayer();
        cave[player.getX()][player.getY()].drawPlayer(g2d, player, tileWidth, tileHeight);
    }

    private void drawBoard(Graphics2D g2d, Tile[][] cave) {
        for (Tile[] row : cave) {
            for (Tile tile : row) {
                tile.draw(g2d, tileWidth, tileHeight);
            }
        }
    }

    private int calcWidth(int height) {
        Tile[][] cave = game.getCave();
        int y = cave[0].length;
        tileHeight = height / y  - 10;
        tileWidth = tileHeight;
        return cave.length * tileWidth;
    }
}
