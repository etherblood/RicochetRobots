package ricochetrobots;

/**
 *
 * @author Philipp
 */
public class RicochetUtil {
    static final int DOWN = 0;
    static final int LEFT = 1;
    static final int UP = 2;
    static final int RIGHT = 3;
    static final int NUM_DIRECTIONS = 4;
    static final int NUM_BOTS = 5;
    static final int SIZE = 16;
    static final int EMPTY = -1;
    static final int[] DIRECTION_OFFSETS = new int[NUM_DIRECTIONS];
    
    static {
        DIRECTION_OFFSETS[UP] = SIZE;
        DIRECTION_OFFSETS[LEFT] = -1;
        DIRECTION_OFFSETS[DOWN] = -SIZE;
        DIRECTION_OFFSETS[RIGHT] = 1;
    }
    
    public static int squareDirectionIndex(int square, int direction) {
        return NUM_DIRECTIONS * square + direction;
    }

    public static int botSquareIndex(int square, int bot) {
        return square * NUM_BOTS + bot;
    }

    public static int botDirectionIndex(int bot, int direction) {
        return bot << 2 | direction;
    }
    
    public final static int square(int x, int y) {
//        assert x >= 0 && y >= 0;
        return x | (y << 4);
    }
    
    public final static int x(int square) {
        return square & 0xf;
    }
    
    public final static int y(int square) {
        return square >>> 4;
    }
    
    public final static int invertDirection(int direction) {
        return direction ^ 2;
    }

}
