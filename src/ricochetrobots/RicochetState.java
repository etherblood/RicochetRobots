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
    
    public default int moveBot(int bot, int direction) {
        int from = botSquare(bot);
        int to = findMoveLimit(from, direction);
        forceMove(bot, from, to);
        return to;
    }
}
