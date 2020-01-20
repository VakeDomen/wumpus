package game_objects;

import players.Player;

import java.awt.*;
import java.util.ArrayList;

public class Tile {
    private int x;
    private int y;
    private Position position;
    private Type type;
    private ArrayList<Effect> effects;

    public Tile(int x, int y, Type type) {
        this.x = x;
        this.y = y;
        this.position = new Position(x, y);
        this.type = type;
        this.effects = new ArrayList<>();
        if (this.type == Type.GOLD) {
            this.effects.add(Effect.GLITTERY);
        }
    }

    public enum Effect {
        BREEZY,
        SMELLY,
        GLITTERY,
        SAFE,
        DANGEROUS_WUMPUS,
        DANGEROUS_PIT,
    }

    public enum Type {
        EMPTY,
        WUMPUS_ALIVE,
        WUMPUS_DEAD,
        PIT,
        GOLD
    }

    public Effect getEffectEmition() {
        if (type == Type.WUMPUS_DEAD || type == Type.WUMPUS_ALIVE)
            addEffect(Effect.SMELLY);

        if (type == Type.PIT)
            return Effect.BREEZY;
        if (type == Type.WUMPUS_ALIVE)
            return Effect.SMELLY;
        if (type == Type.WUMPUS_DEAD)
            return Effect.SMELLY;
        return null;
    }

    public void removeEffect(Effect effect) {
        effects.remove(effect);
    }

    public void addEffect(Effect effect) {
        if (!effects.contains(effect)){
            effects.add(effect);
        }
    }

    public void killWumpus(){
        if (type == Type.WUMPUS_ALIVE) {
            type = Type.WUMPUS_DEAD;
            System.out.println("Killed wumpus");
        }
    }

    public boolean killsPlayer() {
        return (type == Type.PIT || type == Type.WUMPUS_ALIVE);
    }

    public boolean pickUpGold() {
        if (type == Type.GOLD) {
            type = Type.EMPTY;
            return true;
        }
        return false;
    }

    public ArrayList<Effect> getEffects() {
        return this.effects;
    }

    public Type getType() {
        return this.type;
    }

    public void drawPlayer(Graphics2D g2d, Position player, int width, int height) {
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 50));
        if (player.getFacing() == Position.Direction.UP) {
            g2d.drawString("/\\", x*width + width*5/10, y*height+height*5/10);
        } else if(player.getFacing() == Position.Direction.DOWN) {
            g2d.drawString("\\/", x*width + width*5/10, y*height+height*5/10);
        } else if(player.getFacing() == Position.Direction.LEFT) {
            g2d.drawString("<", x*width + width*5/10, y*height+height*5/10);
        } else if(player.getFacing() == Position.Direction.RIGHT) {
            g2d.drawString(">", x*width + width*5/10, y*height+height*5/10);
        }
    }

    public Position getPosition() {
        return this.position;
    }

    public void draw(Graphics2D g2d, int width, int height) {
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 15));
        Color pre = g2d.getColor();
        g2d.setColor(Color.WHITE);
        if (effects.contains(Effect.SAFE)) {
            g2d.setColor(new Color(110, 250, 130));
        }
        g2d.fillRect(
                x * width,
                y * height,
                width,
                height
        );
        g2d.setColor(Color.BLACK);
        g2d.drawRect(
                x * width,
                y * height,
                width,
                height
        );


        if (type == Type.PIT) {
            g2d.fillOval(
                    x * width,
                    y * height,
                    width,
                    height
            );
        }
        if (type == Type.WUMPUS_ALIVE || effects.contains(Effect.DANGEROUS_WUMPUS)) {
            Color c = g2d.getColor();
            g2d.setColor(new Color(102, 51, 0));
            g2d.fillOval(
                    x * width,
                    y * height,
                    width,
                    height
            );
            g2d.setColor(c);
        }
        if (type == Type.WUMPUS_DEAD) {
            Color c = g2d.getColor();
            g2d.setColor(new Color(204, 0, 0));
            g2d.fillOval(
                    x * width,
                    y * height,
                    width,
                    height
            );
            g2d.setColor(c);
        }
        if (type == Type.GOLD) {
            Color c = g2d.getColor();
            g2d.setColor(new Color(220, 200, 86));
            g2d.fillRect(
                    x * width + width/10,
                    y * height + height*8/10,
                    width*2/10,
                    height*2/10
            );
            g2d.setColor(c);
        }
        if (effects.contains(Effect.BREEZY)) {
            g2d.drawString("Br", x*width + width/10, y*height+height/10);
        }
        if (effects.contains(Effect.SMELLY)) {
            g2d.drawString("Sm", x*width + width*4/10, y*height+height/10);
        }
        if (effects.contains(Effect.GLITTERY)) {
            g2d.drawString("Gl", x*width + width*8/10, y*height+height/10);
        }

        g2d.drawString("(" + x + "|" + y + ")", x*width + width*8/10, y*height+height*9/10);


        g2d.setColor(pre);
    }

}
