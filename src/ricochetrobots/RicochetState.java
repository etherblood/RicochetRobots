package ricochetrobots;

import static ricochetrobots.RicochetUtil.NUM_BOTS;
import static ricochetrobots.RicochetUtil.x;
import static ricochetrobots.RicochetUtil.y;

/**
 *
 * @author Philipp
 */
public interface RicochetState {

    int botSquare(int bot);

    void forceMove(int bot, int from, int to);

    int findMoveLimit(int from, int direction);

    int findWall(int from, int direction);

    void addBot(int bot, int square);

    void setWall(int square, int direction);

    default boolean isWall(int square, int direction) {
        return findWall(square, direction) == square;
    }

    public default int moveBot(int bot, int direction) {
        int from = botSquare(bot);
        int to = findMoveLimit(from, direction);
        forceMove(bot, from, to);
        return to;
    }

    public default int colBots(int square, int[] output) {
        int count = 0;
        int x = x(square);
        for (int bot = 0; bot < NUM_BOTS; bot++) {
            int neighborSquare = botSquare(bot);
            if (x(neighborSquare) == x) {
                output[count++] = bot;
            }
        }
        return count;
    }

    public default int rowBots(int square, int[] output) {
        int count = 0;
        int y = y(square);
        for (int bot = 0; bot < NUM_BOTS; bot++) {
            int neighborSquare = botSquare(bot);
            if (y(neighborSquare) == y) {
                output[count++] = bot;
            }
        }
        return count;
    }
}
