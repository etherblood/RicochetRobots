package ricochetrobots;

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

    default int moveBot(int bot, int direction) {
        int from = botSquare(bot);
        int to = findMoveLimit(from, direction);
        forceMove(bot, from, to);
        return to;
    }

    default int colBots(int square, int[] output) {
        int count = 0;
        int x = getSettings().x(square);
        for (int bot = 0; bot < getSettings().getBotCount(); bot++) {
            int neighborSquare = botSquare(bot);
            if (getSettings().x(neighborSquare) == x) {
                output[count++] = bot;
            }
        }
        return count;
    }

    default int rowBots(int square, int[] output) {
        int count = 0;
        int y = getSettings().y(square);
        for (int bot = 0; bot < getSettings().getBotCount(); bot++) {
            int neighborSquare = botSquare(bot);
            if (getSettings().y(neighborSquare) == y) {
                output[count++] = bot;
            }
        }
        return count;
    }
    
    int neighborBot(int square, int direction);
    
    RicochetStateSettings getSettings();

    default int squareBot(int square) {
        for (int bot = 0; bot < getSettings().getBotCount(); bot++) {
            if(botSquare(bot) == square) {
                return bot;
            }
        }
        throw new IllegalStateException();
    }
}
