package ricochetrobots;

import java.util.Arrays;
import static ricochetrobots.RicochetStateSettings.*;

/**
 *
 * @author Philipp
 */
public class BitmaskRicochetState implements RicochetState {

    private final static boolean MOVES_LOOKUP = false;

    private final RicochetStateSettings settings;

    private final int[] lowerMoves;
    private final int[] upperMoves;

    private final int[] botSquare;
    private final int[] cols;
    private final int[] rows;

    private final int[] colBlocked;
    private final int[] rowBlocked;

    public BitmaskRicochetState(RicochetStateSettings settings) {
        this.settings = settings;
        if (settings.getSize() != 16) {
            //assumptions were made...
            //code probably needs to be adjusted before being usable for other sizes
            throw new IllegalStateException("only boardsize of 16x16 supported");
        }
        botSquare = new int[settings.getBotCount()];
        cols = new int[settings.getSize()];
        rows = new int[settings.getSize()];

        colBlocked = new int[settings.getSize() * settings.getSize()];
        rowBlocked = new int[settings.getSize() * settings.getSize()];
        if (MOVES_LOOKUP) {
            lowerMoves = new int[settings.getSize() << settings.getSize()];
            upperMoves = new int[settings.getSize() << settings.getSize()];
            for (int from = 0; from < settings.getSize(); from++) {
                for (int obstacles = 0; obstacles < (1 << settings.getSize()); obstacles++) {
                    int index = movesIndex(from, obstacles);
                    lowerMoves[index] = calcLowerMove(from, obstacles | 0xffff0000);
                    upperMoves[index] = calcUpperMove(from, obstacles | 0xffff0000);
                }
            }
        } else {
            lowerMoves = null;
            upperMoves = null;
        }
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

    @Override
    public void forceMove(int bot, int from, int to) {
        botSquare[bot] = to;
        flipBot(from);
        flipBot(to);
    }

    private void flipBot(int square) {
        int x = settings.x(square);
        int y = settings.y(square);
        cols[x] ^= 1 << y;
        rows[y] ^= 1 << x;
    }

    @Override
    public void setWall(int square, int direction) {
        if (direction >= 2) {
            square += settings.getDirectionOffset(direction);
            direction &= 1;
        }
        int x = settings.x(square);
        int y = settings.y(square);
        if (direction == DOWN) {
            int upperMask = ~0 << y;
            for (int i = 0; i < y; i++) {
                colBlocked[settings.square(x, i)] |= upperMask;
            }
            int lowerMask = ~upperMask;
            for (int i = y; i < settings.getSize(); i++) {
                colBlocked[settings.square(x, i)] |= lowerMask;
            }
        } else {
            int upperMask = ~0 << x;
            for (int i = 0; i < x; i++) {
                rowBlocked[settings.square(i, y)] |= upperMask;
            }
            int lowerMask = ~upperMask;
            for (int i = x; i < settings.getSize(); i++) {
                rowBlocked[settings.square(i, y)] |= lowerMask;
            }
        }
    }

    @Override
    public int findMoveLimit(int square, int direction) {
        int x = settings.x(square);
        int y = settings.y(square);
        switch (direction) {
            case DOWN:
                return settings.square(x, lowerMove(y, cols[x] | colBlocked[square]));
            case LEFT:
                return settings.square(lowerMove(x, rows[y] | rowBlocked[square]), y);
            case UP:
                return settings.square(x, upperMove(y, cols[x] | colBlocked[square]));
            case RIGHT:
                return settings.square(upperMove(x, rows[y] | rowBlocked[square]), y);
            default:
                throw new UnsupportedOperationException("invalid direction: " + direction);
        }
    }

    @Override
    public int findWall(int square, int direction) {
        int x = settings.x(square);
        int y = settings.y(square);
        switch (direction) {
            case DOWN:
                return settings.square(x, lowerMove(y, colBlocked[square]));
            case LEFT:
                return settings.square(lowerMove(x, rowBlocked[square]), y);
            case UP:
                return settings.square(x, upperMove(y, colBlocked[square]));
            case RIGHT:
                return settings.square(upperMove(x, rowBlocked[square]), y);
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
            return settings.getSize() - 1;
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
        return (from << settings.getSize()) | (obstacles & 0xffff);
    }

    @Override
    public int neighborBot(int square, int direction) {
        int x = settings.x(square);
        int y = settings.y(square);
        switch (direction) {
            case DOWN: {
                int potential = cols[x] & ~colBlocked[square];
                if (potential == 0) {
                    return -1;
                }
                do {
                    y--;
                } while (y >= 0 && ((1 << y) & potential) == 0);
                if (y == -1) {
                    return -1;
                }
                return botFromSquare(settings.square(x, y));
            }
            case LEFT: {
                int potential = rows[y] & ~rowBlocked[square];
                if (potential == 0) {
                    return -1;
                }
                do {
                    x--;
                } while (x >= 0 && ((1 << x) & potential) == 0);
                if (x == -1) {
                    return -1;
                }
                return botFromSquare(settings.square(x, y));
            }
            case UP: {
                int potential = cols[x] & ~colBlocked[square];
                if (potential == 0) {
                    return -1;
                }
                do {
                    y++;
                } while (y < settings.getSize() && ((1 << y) & potential) == 0);
                if (y == settings.getSize()) {
                    return -1;
                }
                return botFromSquare(settings.square(x, y));
            }
            case RIGHT: {
                int potential = rows[y] & ~rowBlocked[square];
                if (potential == 0) {
                    return -1;
                }
                do {
                    x++;
                } while (x < settings.getSize() && ((1 << x) & potential) == 0);
                if (x == settings.getSize()) {
                    return -1;
                }
                return botFromSquare(settings.square(x, y));
            }
            default:
                throw new UnsupportedOperationException("invalid direction: " + direction);
        }
    }

    int botFromSquare(int square) {
        for (int bot = 0; bot < settings.getBotCount(); bot++) {
            if (botSquare(bot) == square) {
                return bot;
            }
        }
        throw new IllegalStateException();
    }

    int lowerObstacle(int from, int obstacles) {
        if (from == 0) {
            return -1;
        }
        int shiftedObstacles = obstacles << (Integer.SIZE - from);
        if (shiftedObstacles == 0) {
            return -1;
        }
        return from - Integer.numberOfLeadingZeros(shiftedObstacles) - 1;
    }

    @Override
    public RicochetStateSettings getSettings() {
        return settings;
    }
}
