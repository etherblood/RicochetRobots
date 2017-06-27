package ricochetrobots;

import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static ricochetrobots.RicochetUtil.DOWN;
import static ricochetrobots.RicochetUtil.LEFT;
import static ricochetrobots.RicochetUtil.RIGHT;
import static ricochetrobots.RicochetUtil.UP;
import static ricochetrobots.RicochetUtil.square;

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
        RicochetState state = new BitmaskRicochetState();
        setup5bots22movesForBlue(state);
        int targetBot = blue;
        int targetSquare = square(9, 12);
        
        RicochetSolver solver = new RicochetSolver(state, new TranspositionTable(26));
        List<RicochetMove> result = solver.solve(targetBot, targetSquare);
        assertEquals(22, result.size());
        for (RicochetMove move : result) {
            state.moveBot(move.getBot(), move.getDirection());
        }
        assertEquals(targetSquare, state.botSquare(targetBot));
    }

    private void setup5bots22movesForBlue(RicochetState state) {
        state.addBot(red, square(2, 14));
        state.addBot(blue, square(11, 2));
        state.addBot(yellow, square(2, 1));
        state.addBot(green, square(0, 3));
        state.addBot(silver, square(2, 2));

        state.setWall(square(4, 0), RIGHT);
        state.setWall(square(9, 0), RIGHT);
        state.setWall(square(14, 0), UP);
        state.setWall(square(2, 1), UP);
        state.setWall(square(2, 1), RIGHT);
        state.setWall(square(13, 1), RIGHT);
        state.setWall(square(10, 2), RIGHT);
        state.setWall(square(11, 2), UP);
        state.setWall(square(0, 3), RIGHT);
        state.setWall(square(1, 3), UP);
        state.setWall(square(15, 3), UP);
        state.setWall(square(0, 4), UP);
        state.setWall(square(5, 4), RIGHT);
        state.setWall(square(5, 5), UP);

        state.setWall(square(5, 6), RIGHT);
        state.setWall(square(7, 6), UP);
        state.setWall(square(8, 6), UP);
        state.setWall(square(10, 6), UP);
        state.setWall(square(13, 6), UP);
        state.setWall(square(13, 6), RIGHT);
        state.setWall(square(3, 7), UP);
        state.setWall(square(3, 7), RIGHT);
        state.setWall(square(6, 7), RIGHT);
        state.setWall(square(8, 7), RIGHT);
        state.setWall(square(10, 7), RIGHT);
        state.setWall(square(6, 3), UP);

        state.setWall(square(7, 8), LEFT);
        state.setWall(square(7, 8), UP);
        state.setWall(square(8, 8), RIGHT);
        state.setWall(square(8, 8), UP);
        state.setWall(square(3, 9), DOWN);
        state.setWall(square(3, 9), RIGHT);
        state.setWall(square(14, 9), LEFT);
        state.setWall(square(14, 9), DOWN);
        state.setWall(square(11, 10), UP);
        state.setWall(square(11, 10), LEFT);
        state.setWall(square(1, 11), UP);
        state.setWall(square(1, 11), LEFT);

        state.setWall(square(2, 14), LEFT);
        state.setWall(square(2, 14), DOWN);
        state.setWall(square(0, 14), DOWN);
        state.setWall(square(6, 15), LEFT);
        state.setWall(square(11, 15), RIGHT);
        state.setWall(square(13, 14), UP);
        state.setWall(square(13, 14), RIGHT);
        state.setWall(square(9, 12), RIGHT);
        state.setWall(square(9, 12), DOWN);
        state.setWall(square(15, 10), UP);
        state.setWall(square(6, 12), UP);
        state.setWall(square(6, 12), RIGHT);
    }

}
