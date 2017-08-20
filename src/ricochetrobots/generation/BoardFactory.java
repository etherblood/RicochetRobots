package ricochetrobots.generation;

import java.util.List;
import ricochetrobots.RicochetState;

/**
 *
 * @author Philipp
 */
public class BoardFactory {

    public void populate(RicochetState state, List<Quadrant> quadrants) {
        for (int i = 0; i < 4; i++) {
            for (Wall wall : quadrants.get(i).getWalls()) {
                Wall current = wall.translate(-7, -7);
                for (int j = 0; j < i; j++) {
                    current = current.rotateClockwise();
                }
                current = current.translate(7, 7);
                assert 0 <= current.getX() && current.getX() < 16;
                assert 0 <= current.getY() && current.getY() < 16;
                state.setWall(state.getSettings().square(current.getX(), current.getY()), current.getDirection());
            }
        }
    }
}
