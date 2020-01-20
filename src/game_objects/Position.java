package game_objects;

public class Position {
    private int x;
    private int y;

    //for player movment
    private Direction facing;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.facing = Direction.RIGHT;
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void turnLeft() {
        if (facing == Direction.UP)
            facing = Direction.LEFT;
        else if (facing == Direction.LEFT)
            facing = Direction.DOWN;
        else if (facing == Direction.DOWN)
            facing = Direction.RIGHT;
        else if (facing == Direction.RIGHT)
            facing = Direction.UP;
    }

    public void turnRight() {
        if (facing == Direction.UP)
            facing = Direction.RIGHT;
        else if (facing == Direction.LEFT)
            facing = Direction.UP;
        else if (facing == Direction.DOWN)
            facing = Direction.LEFT;
        else if (facing == Direction.RIGHT)
            facing = Direction.DOWN;
    }

    public Direction getFacing() {
        return facing;
    }

    public String toString() {
        return "(" + x + " | " + y +  ")";
    }

    public String toStringPlayer() {
        return "(" + x + " | " + y +  ") " + facing;
    }
}
