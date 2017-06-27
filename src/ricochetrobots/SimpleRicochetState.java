package ricochetrobots;

import java.util.Arrays;
import static ricochetrobots.RicochetUtil.*;

/**
 *
 * @author Philipp
 */
public class SimpleRicochetState implements RicochetState {
    
    private final int[] botSquare = new int[NUM_BOTS];
    private final int[] squareBot = new int[SIZE * SIZE];
    private final int[] moveToWallMap = new int[NUM_DIRECTIONS * SIZE * SIZE];
    
    public SimpleRicochetState() {
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
    
    @Override
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
    
    @Override
    public void addBot(int bot, int square) {
        botSquare[bot] = square;
        squareBot[square] = bot;
    }
    
    @Override
    public void forceMove(int bot, int index, int targetIndex) {
        squareBot[index] = EMPTY;
        squareBot[targetIndex] = bot;
        botSquare[bot] = targetIndex;
    }

    @Override
    public int findMoveLimit(int square, int direction) {
        int offset = DIRECTION_OFFSETS[direction];
        int target = findWall(square, direction);
        int next;
        while(square != target && squareBot[next = square + offset] == EMPTY) {
            square = next;
        }
        return square;
    }
    @Override
    public int findWall(int square, int direction) {
        return moveToWallMap[squareDirectionIndex(square, direction)];
    }
    
    int squareBot(int square) {
        return squareBot[square];
    }

    @Override
    public int botSquare(int bot) {
        return botSquare[bot];
    }
}
