package ricochetrobots;

import java.util.Arrays;
import static ricochetrobots.RicochetUtil.*;

/**
 *
 * @author Philipp
 */
public class BitmaskRicochetState implements RicochetState {

    private final int[] botSquare = new int[NUM_BOTS];
    private final int[] cols = new int[SIZE];
    private final int[] rows = new int[SIZE];

    private final int[] colBlocked = new int[SIZE * SIZE];
    private final int[] rowBlocked = new int[SIZE * SIZE];

    public BitmaskRicochetState() {
        clear();
    }

    public final void clear() {
        Arrays.fill(cols, 0);
        Arrays.fill(rows, 0);
        Arrays.fill(colBlocked, 0);
        Arrays.fill(rowBlocked, 0);
        Arrays.fill(botSquare, -1);
    }

    @Override
    public void addBot(int bot, int square) {
        botSquare[bot] = square;
        flipBot(square);
    }

    public int moveBot(int bot, int direction) {
        int from = botSquare[bot];
        int to = findMoveLimit(from, direction);
        forceMove(bot, from, to);
        return to;
    }

    @Override
    public void forceMove(int bot, int from, int to) {
        botSquare[bot] = to;
        flipBot(from);
        flipBot(to);
    }

    private void flipBot(int square) {
        int x = x(square);
        int y = y(square);
        cols[x] ^= 1 << y;
        rows[y] ^= 1 << x;
    }

    @Override
    public void setWall(int square, int direction) {
        if (direction >= 2) {
            square += DIRECTION_OFFSETS[direction];
            direction &= 1;
        }
        int x = x(square);
        int y = y(square);
        if (isHorizontal(direction)) {
            int upperMask = ~0 << x;
            for (int i = 0; i < x; i++) {
                rowBlocked[square(i, y)] |= upperMask;
            }
            for (int i = x; i < SIZE; i++) {
                rowBlocked[square(i, y)] |= ~upperMask;
            }
        } else {
            int upperMask = ~0 << y;
            for (int i = 0; i < y; i++) {
                colBlocked[square(x, i)] |= upperMask;
            }
            for (int i = y; i < SIZE; i++) {
                colBlocked[square(x, i)] |= ~upperMask;
            }
        }
    }

    @Override
    public int findMoveLimit(int square, int direction) {
        int x = x(square);
        int y = y(square);
        switch (direction) {
            case DOWN:
                return square(x, lowerMove(y, cols[x] | colBlocked[square]));
            case LEFT:
                return square(lowerMove(x, rows[y] | rowBlocked[square]), y);
            case UP:
                return square(x, upperMove(y, cols[x] | colBlocked[square]));
            case RIGHT:
                return square(upperMove(x, rows[y] | rowBlocked[square]), y);
            default:
                throw new UnsupportedOperationException("invalid direction: " + direction);
        }
    }

    @Override
    public int findWall(int square, int direction) {
        int x = x(square);
        int y = y(square);
        switch (direction) {
            case DOWN:
                return square(x, lowerMove(y, colBlocked[square]));
            case LEFT:
                return square(lowerMove(x, rowBlocked[square]), y);
            case UP:
                return square(x, upperMove(y, colBlocked[square]));
            case RIGHT:
                return square(upperMove(x, rowBlocked[square]), y);
            default:
                throw new UnsupportedOperationException("invalid direction: " + direction);
        }
    }

    private boolean isHorizontal(int direction) {
        return (direction & 1) != 0;
    }

    int lowerMove(int from, int obstacles) {
        if (from == 0) {
            return 0;
        }
        int shiftedObstacles = obstacles << (Integer.SIZE - from);
//        int shiftedObstacles = (obstacles << Short.SIZE) << (Short.SIZE - from);//can be used instead of if&statement above
        if (shiftedObstacles == 0) {
            return 0;
        }
        return from - Integer.numberOfLeadingZeros(shiftedObstacles);
    }

    int upperMove(int from, int obstacles) {
        int shiftedObstacles = obstacles >>> from + 1;
        if (shiftedObstacles == 0) {
            return SIZE - 1;
        }
        return from + Integer.numberOfTrailingZeros(shiftedObstacles);
    }

    @Override
    public int botSquare(int bot) {
        return botSquare[bot];
    }
}
