package ricochetrobots.generation;

import java.util.ArrayList;
import java.util.List;
import ricochetrobots.RicochetUtil;

/**
 *
 * @author Philipp
 */
public class QuadrantBuilder {

    private final List<Wall> walls = new ArrayList<>();

    public QuadrantBuilder addWall(Wall wall) {
        walls.add(wall);
        return this;
    }

    public QuadrantBuilder addWall(int x, int y, int direction) {
        return addWall(new Wall(x, y, direction));
    }

    public QuadrantBuilder addWall(int x, int y, String direction) {
        for (int i = 0; i < direction.length(); i++) {
            char c = direction.charAt(i);
            switch (c) {
                case 'N':
                    addWall(x, y, RicochetUtil.UP);
                    break;
                case 'E':
                    addWall(x, y, RicochetUtil.RIGHT);
                    break;
                case 'S':
                    addWall(x, y, RicochetUtil.DOWN);
                    break;
                case 'W':
                    addWall(x, y, RicochetUtil.LEFT);
                    break;
            }
        }
        return this;
    }

    public QuadrantBuilder addGoal(int x, int y, int bot, int symbol) {
        return addGoal(new Goal(x, y, bot, symbol));
    }

    public QuadrantBuilder addGoal(Goal goal) {
        return this;
    }
    
    public Quadrant build() {
        return new Quadrant(new ArrayList<>(walls));
    }
}
