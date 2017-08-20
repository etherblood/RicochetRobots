package ricochetrobots;

import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static ricochetrobots.RicochetStateSettings.*;

/**
 *
 * @author Philipp
 */
public class SamplePuzzlesTest {

    private final int red = 0;
    private final int blue = 1;
    private final int yellow = 2;
    private final int green = 3;
    private final int silver = 4;

    @Test
    public void solve5bots22movesForBlue() {
        RicochetState state = new BitmaskRicochetState(new RicochetStateSettings(5, 16));
        setup5bots22movesForBlue(state);
        int targetBot = blue;
        int targetSquare = state.getSettings().square(9, 12);

        RicochetSolver solver = new RicochetSolver(state, new TranspositionTable(26));
        List<RicochetMove> result = solver.solve(targetBot, targetSquare);
        assertEquals(22, result.size());
        for (RicochetMove move : result) {
            state.moveBot(move.getBot(), move.getDirection());
        }
        assertEquals(targetSquare, state.botSquare(targetBot));
    }

    private void setup5bots22movesForBlue(RicochetState state) {
        state.addBot(red, state.getSettings().square(2, 14));
        state.addBot(blue, state.getSettings().square(11, 2));
        state.addBot(yellow, state.getSettings().square(2, 1));
        state.addBot(green, state.getSettings().square(0, 3));
        state.addBot(silver, state.getSettings().square(2, 2));

        state.setWall(state.getSettings().square(4, 0), RIGHT);
        state.setWall(state.getSettings().square(9, 0), RIGHT);
        state.setWall(state.getSettings().square(14, 0), UP);
        state.setWall(state.getSettings().square(2, 1), UP);
        state.setWall(state.getSettings().square(2, 1), RIGHT);
        state.setWall(state.getSettings().square(13, 1), RIGHT);
        state.setWall(state.getSettings().square(10, 2), RIGHT);
        state.setWall(state.getSettings().square(11, 2), UP);
        state.setWall(state.getSettings().square(0, 3), RIGHT);
        state.setWall(state.getSettings().square(1, 3), UP);
        state.setWall(state.getSettings().square(15, 3), UP);
        state.setWall(state.getSettings().square(0, 4), UP);
        state.setWall(state.getSettings().square(5, 4), RIGHT);
        state.setWall(state.getSettings().square(5, 5), UP);

        state.setWall(state.getSettings().square(5, 6), RIGHT);
        state.setWall(state.getSettings().square(7, 6), UP);
        state.setWall(state.getSettings().square(8, 6), UP);
        state.setWall(state.getSettings().square(10, 6), UP);
        state.setWall(state.getSettings().square(13, 6), UP);
        state.setWall(state.getSettings().square(13, 6), RIGHT);
        state.setWall(state.getSettings().square(3, 7), UP);
        state.setWall(state.getSettings().square(3, 7), RIGHT);
        state.setWall(state.getSettings().square(6, 7), RIGHT);
        state.setWall(state.getSettings().square(8, 7), RIGHT);
        state.setWall(state.getSettings().square(10, 7), RIGHT);
        state.setWall(state.getSettings().square(6, 3), UP);

        state.setWall(state.getSettings().square(7, 8), LEFT);
        state.setWall(state.getSettings().square(7, 8), UP);
        state.setWall(state.getSettings().square(8, 8), RIGHT);
        state.setWall(state.getSettings().square(8, 8), UP);
        state.setWall(state.getSettings().square(3, 9), DOWN);
        state.setWall(state.getSettings().square(3, 9), RIGHT);
        state.setWall(state.getSettings().square(14, 9), LEFT);
        state.setWall(state.getSettings().square(14, 9), DOWN);
        state.setWall(state.getSettings().square(11, 10), UP);
        state.setWall(state.getSettings().square(11, 10), LEFT);
        state.setWall(state.getSettings().square(1, 11), UP);
        state.setWall(state.getSettings().square(1, 11), LEFT);

        state.setWall(state.getSettings().square(2, 14), LEFT);
        state.setWall(state.getSettings().square(2, 14), DOWN);
        state.setWall(state.getSettings().square(0, 14), DOWN);
        state.setWall(state.getSettings().square(6, 15), LEFT);
        state.setWall(state.getSettings().square(11, 15), RIGHT);
        state.setWall(state.getSettings().square(13, 14), UP);
        state.setWall(state.getSettings().square(13, 14), RIGHT);
        state.setWall(state.getSettings().square(9, 12), RIGHT);
        state.setWall(state.getSettings().square(9, 12), DOWN);
        state.setWall(state.getSettings().square(15, 10), UP);
        state.setWall(state.getSettings().square(6, 12), UP);
        state.setWall(state.getSettings().square(6, 12), RIGHT);
    }

    @Test
    public void foglemanSlidersLv8() {
        RicochetState state = new SimpleRicochetState(new RicochetStateSettings(3, 5));
        setupFoglemanSlidersLv8(state);
        int targetBot = red;
        int targetSquare = state.getSettings().square(2, 2);

        RicochetSolver solver = new RicochetSolver(state, new TranspositionTable(10));
        List<RicochetMove> result = solver.solve(targetBot, targetSquare);
        assertEquals(20, result.size());
        for (RicochetMove move : result) {
            state.moveBot(move.getBot(), move.getDirection());
        }
        assertEquals(targetSquare, state.botSquare(targetBot));
    }

    private void setupFoglemanSlidersLv8(RicochetState state) {
        state.addBot(red, state.getSettings().square(1, 4));
        state.addBot(blue, state.getSettings().square(0, 2));
        state.addBot(yellow, state.getSettings().square(4, 2));

        for (int i = 0; i < 4; i++) {
            state.setWall(state.getSettings().square(i, 1), RIGHT);
            state.setWall(state.getSettings().square(i, 3), RIGHT);
        }

        state.setWall(state.getSettings().square(0, 1), UP);
        state.setWall(state.getSettings().square(0, 2), UP);
        state.setWall(state.getSettings().square(4, 1), UP);
        state.setWall(state.getSettings().square(4, 2), UP);
    }

}
