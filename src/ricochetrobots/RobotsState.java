package ricochetrobots;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class RobotsState {
    static final int UP = 0;
    static final int LEFT = 1;
    static final int DOWN = 2;
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
    
    private final int[] botSquare = new int[NUM_BOTS];
    private final int[] squareBot = new int[SIZE * SIZE];
    private final long[] botSquareHashes = new long[NUM_BOTS * SIZE * SIZE];
    private final int[] moveToWallMap = new int[NUM_DIRECTIONS * SIZE * SIZE];
    
    private long hash;
    
    public RobotsState() {
        clear();
    }
    
    public final void clear() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                int square = square(x, y);
                moveToWallMap[squareDirectionIndex(square, UP)] = square(x, SIZE - 1);
                moveToWallMap[squareDirectionIndex(square, LEFT)] = square(0, y);
                moveToWallMap[squareDirectionIndex(square, DOWN)] = square(x, 0);
                moveToWallMap[squareDirectionIndex(square, RIGHT)] = square(SIZE - 1, y);
            }
        }
        Arrays.fill(squareBot, EMPTY);
        Arrays.fill(botSquare, -1);
    }
    
    public void initHashes(Random rng, int importantBot) {
        initSharedHashes(rng);
        initBotSpecificHashes(rng, importantBot);
        hash = 0;
    }
    
    private void initSharedHashes(Random rng) {
        for (int square = 0; square < SIZE * SIZE; square++) {
            long sharedHash = rng.nextLong();
            for (int bot = 0; bot < NUM_BOTS; bot++) {
                botSquareHashes[botSquareIndex(square, bot)] = sharedHash;
            }
        }
    }
    
    private void initBotSpecificHashes(Random rng, int bot) {
        for (int square = 0; square < SIZE * SIZE; square++) {
            long specificHash = rng.nextLong();
            botSquareHashes[botSquareIndex(square, bot)] = specificHash;
        }
    }
    
    public void setWall(int square, int direction) {
        int nextSquare = square + DIRECTION_OFFSETS[direction];
        int inverseDirection = invertDirection(direction);
        wallUpdate(square, inverseDirection);
        wallUpdate(nextSquare, direction);
    }
    
    public void removeWall(int square, int direction) {
        int nextSquare = square + DIRECTION_OFFSETS[direction];
        int inverseDirection = invertDirection(direction);
        int first = findWall(square, inverseDirection);
        int last = findWall(nextSquare, direction);
        
        wallUpdate(first, last, direction);
        wallUpdate(last, first, inverseDirection);
    }
    
    private void wallUpdate(int from, int direction) {
        wallUpdate(from, moveToWallMap[squareDirectionIndex(from, direction)], direction);
    }
    
    private void wallUpdate(int from, int to, int direction) {
        int inverseDirection = invertDirection(direction);
        int offset = DIRECTION_OFFSETS[direction];
        int current = from;
        while(current != to) {
            moveToWallMap[squareDirectionIndex(current, inverseDirection)] = from;
            current += offset;
        }
        moveToWallMap[squareDirectionIndex(current, inverseDirection)] = from;
    }
    
    public void addBot(int bot, int square) {
        botSquare[bot] = square;
        squareBot[square] = bot;
        hash ^= botSquareHashes[botSquareIndex(square, bot)];
    }
    
    public int moveBot(int bot, int direction) {
        int from = botSquare[bot];
        int to = findMoveLimit(from, direction);
        forceMove(bot, from, to);
        return to;
    }
    
    public void forceMove(int bot, int index, int targetIndex) {
        squareBot[index] = EMPTY;
        squareBot[targetIndex] = bot;
        botSquare[bot] = targetIndex;
        hash ^= botSquareHashes[botSquareIndex(index, bot)];
        hash ^= botSquareHashes[botSquareIndex(targetIndex, bot)];
    }

    public int findMoveLimit(int square, int direction) {
        int offset = DIRECTION_OFFSETS[direction];
        int target = findWall(square, direction);
        int next;
        while(square != target && squareBot[next = square + offset] == EMPTY) {
            square = next;
        }
        return square;
    }
    public int findWall(int square, int direction) {
        return moveToWallMap[squareDirectionIndex(square, direction)];
    }
    
    boolean isWall(int square, int direction) {
        return findWall(square, direction) == square;
    }
    
    int squareBot(int square) {
        return squareBot[square];
    }

    public long getHash() {
        return hash;
    }

    int botSquare(int bot) {
        return botSquare[bot];
    }
    
    private static int squareDirectionIndex(int square, int direction) {
        return NUM_DIRECTIONS * square + direction;
    }

    private static int botSquareIndex(int square, int bot) {
        return square * NUM_BOTS + bot;
    }
    
    final static int square(int x, int y) {
        return x + y * SIZE;
    }
    
    final static int x(int square) {
        return square % SIZE;
    }
    
    final static int y(int square) {
        return square / SIZE;
    }
    
    final static int invertDirection(int direction) {
        return direction ^ 2;
    }
}
