package ricochetrobots;

/**
 *
 * @author Philipp
 */
public class RicochetStateSettings {
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int RIGHT = 3;
    public static final int NUM_DIRECTIONS = 4;

    private final int botCount;
    private final int size;
    private final int EMPTY = -1;
    private final int[] directionOffsets = new int[NUM_DIRECTIONS];

    public RicochetStateSettings(int botCount, int size) {
        this.botCount = botCount;
        this.size = size;

        directionOffsets[UP] = size;
        directionOffsets[LEFT] = -1;
        directionOffsets[DOWN] = -size;
        directionOffsets[RIGHT] = 1;
    }
    
    
    public int squareDirectionIndex(int square, int direction) {
        return NUM_DIRECTIONS * square + direction;
    }

    public int botSquareIndex(int square, int bot) {
        return square * botCount + bot;
    }

    public int botDirectionIndex(int bot, int direction) {
        return bot << 2 | direction;
    }
    
    public int square(int x, int y) {
        return x | (y << 4);
    }
    
    public int x(int square) {
        return square & 0xf;
    }
    
    public int y(int square) {
        return square >>> 4;
    }
    
    public int invertDirection(int direction) {
        return direction ^ 2;
    }

    public int getBotCount() {
        return botCount;
    }

    public int getSize() {
        return size;
    }

    public int getEMPTY() {
        return EMPTY;
    }
    
    public int getDirectionOffset(int direction) {
        return directionOffsets[direction];
    }

}
