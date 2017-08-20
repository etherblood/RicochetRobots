package ricochetrobots;

import java.util.Arrays;
import static ricochetrobots.RicochetStateSettings.*;

/**
 *
 * @author Philipp
 */
public class SimpleRicochetState implements RicochetState {

    private final RicochetStateSettings settings;
    private final int[] botSquare;
    private final int[] squareBot;
    private final int[] moveToWallMap;

    public SimpleRicochetState(RicochetStateSettings settings) {
        this.settings = settings;
        botSquare = new int[settings.getBotCount()];
        squareBot = new int[settings.getSize() * settings.getSize()];
        moveToWallMap = new int[NUM_DIRECTIONS * settings.getSize() * settings.getSize()];
        clear();
    }

    public final void clear() {
        for (int y = 0; y < settings.getSize(); y++) {
            for (int x = 0; x < settings.getSize(); x++) {
                int square = settings.square(x, y);
                moveToWallMap[settings.squareDirectionIndex(square, UP)] = settings.square(x, settings.getSize() - 1);
                moveToWallMap[settings.squareDirectionIndex(square, LEFT)] = settings.square(0, y);
                moveToWallMap[settings.squareDirectionIndex(square, DOWN)] = settings.square(x, 0);
                moveToWallMap[settings.squareDirectionIndex(square, RIGHT)] = settings.square(settings.getSize() - 1, y);
            }
        }
        Arrays.fill(squareBot, ricochetrobots.RicochetStateSettings.EMPTY);
        Arrays.fill(botSquare, -1);
    }

    @Override
    public void setWall(int square, int direction) {
        int nextSquare = square + settings.getDirectionOffset(direction);
        int inverseDirection = settings.invertDirection(direction);
        wallUpdate(square, inverseDirection);
        wallUpdate(nextSquare, direction);
    }

    public void removeWall(int square, int direction) {
        int nextSquare = square + settings.getDirectionOffset(direction);
        int inverseDirection = settings.invertDirection(direction);
        int first = findWall(square, inverseDirection);
        int last = findWall(nextSquare, direction);

        wallUpdate(first, last, direction);
        wallUpdate(last, first, inverseDirection);
    }

    private void wallUpdate(int from, int direction) {
        wallUpdate(from, moveToWallMap[settings.squareDirectionIndex(from, direction)], direction);
    }

    private void wallUpdate(int from, int to, int direction) {
        int inverseDirection = settings.invertDirection(direction);
        int offset = settings.getDirectionOffset(direction);
        int current = from;
        while (current != to) {
            moveToWallMap[settings.squareDirectionIndex(current, inverseDirection)] = from;
            current += offset;
        }
        moveToWallMap[settings.squareDirectionIndex(current, inverseDirection)] = from;
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
        int offset = settings.getDirectionOffset(direction);
        int target = findWall(square, direction);
        int next;
        while (square != target && squareBot[next = square + offset] == EMPTY) {
            square = next;
        }
        return square;
    }

    @Override
    public int findWall(int square, int direction) {
        return moveToWallMap[settings.squareDirectionIndex(square, direction)];
    }

    int squareBot(int square) {
        return squareBot[square];
    }

    @Override
    public int botSquare(int bot) {
        return botSquare[bot];
    }

    @Override
    public int neighborBot(int square, int direction) {
        int last = findWall(square, direction);
        int dirOffset = settings.getDirectionOffset(direction);
        while (square != last) {
            square += dirOffset;
            int bot = squareBot(square);
            if (bot != EMPTY) {
                return bot;
            }
        }
        return -1;
    }

    @Override
    public RicochetStateSettings getSettings() {
        return settings;
    }
}
