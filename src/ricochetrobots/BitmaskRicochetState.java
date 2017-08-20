package ricochetrobots;

import java.util.Arrays;
import static ricochetrobots.RicochetUtil.*;

/**
 *
 * @author Philipp
 */
public class BitmaskRicochetState implements RicochetState {

    private final static boolean MOVES_LOOKUP = false;

    private final int[] lowerMoves;
    private final int[] upperMoves;

    private final int[] botSquare = new int[NUM_BOTS];
    private final int[] cols = new int[SIZE];
    private final int[] rows = new int[SIZE];

    private final int[] colBlocked = new int[SIZE * SIZE];
    private final int[] rowBlocked = new int[SIZE * SIZE];

    public BitmaskRicochetState() {
        if (SIZE != 16) {
            //assumptions were made...
            //code probably needs to be adjusted before being usable for other sizes
            throw new IllegalStateException("only boardsize of 16x16 supported");
        }
        clear();
        if (MOVES_LOOKUP) {
            lowerMoves = new int[SIZE << SIZE];
            upperMoves = new int[SIZE << SIZE];
            for (int from = 0; from < SIZE; from++) {
                for (int obstacles = 0; obstacles < (1 << SIZE); obstacles++) {
                    int index = movesIndex(from, obstacles);
                    lowerMoves[index] = calcLowerMove(from, obstacles | 0xffff0000);
                    upperMoves[index] = calcUpperMove(from, obstacles | 0xffff0000);
                }
            }
        } else {
            lowerMoves = null;
            upperMoves = null;
        }
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
        if (direction == DOWN) {
            int upperMask = ~0 << y;
            for (int i = 0; i < y; i++) {
                colBlocked[square(x, i)] |= upperMask;
            }
            int lowerMask = ~upperMask;
            for (int i = y; i < SIZE; i++) {
                colBlocked[square(x, i)] |= lowerMask;
            }
        } else {
            int upperMask = ~0 << x;
            for (int i = 0; i < x; i++) {
                rowBlocked[square(i, y)] |= upperMask;
            }
            int lowerMask = ~upperMask;
            for (int i = x; i < SIZE; i++) {
                rowBlocked[square(i, y)] |= lowerMask;
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

    int lowerMove(int from, int obstacles) {
        if (MOVES_LOOKUP) {
            return lowerMoves[movesIndex(from, obstacles)];
        }
        return calcLowerMove(from, obstacles);
    }

    private int calcLowerMove(int from, int obstacles) {
        if (from == 0) {
            return 0;
        }
        int shiftedObstacles = obstacles << (Integer.SIZE - from);
        if (shiftedObstacles == 0) {
            return 0;
        }
        return from - Integer.numberOfLeadingZeros(shiftedObstacles);
    }

//    int lowerMove_branchless(int from, int obstacles) {
//        int shiftedObstacles = (~obstacles << Short.SIZE) << (Short.SIZE - from);
//        return from - Integer.numberOfLeadingZeros(~shiftedObstacles);
//    }
//
//    int lowerMove_branchless2(int from, int obstacles) {
//        int shiftedObstacles = (int) ((long)~obstacles << (Integer.SIZE - from));
//        return from - Integer.numberOfLeadingZeros(~shiftedObstacles);
//    }
    int upperMove(int from, int obstacles) {
        if (MOVES_LOOKUP) {
            return upperMoves[movesIndex(from, obstacles)];
        }
        return calcUpperMove(from, obstacles);
    }

    private int calcUpperMove(int from, int obstacles) {
        int shiftedObstacles = obstacles >>> from + 1;
        if (shiftedObstacles == 0) {
            return SIZE - 1;
        }
        return from + Integer.numberOfTrailingZeros(shiftedObstacles);
    }

//    int upperMove_branchless(int from, int obstacles) {
//        int shiftedObstacles = ((1 << SIZE) | obstacles) >>> from + 1;
//        return from + Integer.numberOfTrailingZeros(shiftedObstacles);
//    }
    @Override
    public int botSquare(int bot) {
        return botSquare[bot];
    }

    private int movesIndex(int from, int obstacles) {
        assert (from & 0xf) == from : from;
        return (from << SIZE) | (obstacles & 0xffff);
    }
}
