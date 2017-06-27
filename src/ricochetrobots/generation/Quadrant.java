package ricochetrobots.generation;

import java.util.List;

public class Quadrant {

    private final List<Wall> walls;

    public Quadrant(List<Wall> walls) {
        this.walls = walls;
    }

    public List<Wall> getWalls() {
        return walls;
    }
}
