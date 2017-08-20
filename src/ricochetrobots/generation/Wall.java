package ricochetrobots.generation;

import ricochetrobots.RicochetStateSettings;

/**
 *
 * @author Philipp
 */
public class Wall {

    private final int x, y, direction;

    public Wall(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Wall rotateClockwise() {
        return new Wall(y, -x + 1, (direction + 1) % RicochetStateSettings.NUM_DIRECTIONS);
    }

    public Wall translate(int x, int y) {
        return new Wall(this.x + x, this.y + y, direction);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }
}
