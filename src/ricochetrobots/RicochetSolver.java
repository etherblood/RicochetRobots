package ricochetrobots;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import static ricochetrobots.RicochetStateSettings.*;

/**
 *
 * @author Philipp
 */
public class RicochetSolver {

    private final static boolean VERBOSE = true;

    private List<RicochetMove> result;
    private final RicochetStateSettings settings;
    private final TranspositionTable table;
    private final TranspositionEntry entry = new TranspositionEntry();
    private final int[] targetDistance;
    private long nodes;
    private final int[] botsOrder;
    private int targetBot;
    private final RicochetState state;
    private final RicochetZobrist zobrist = new RicochetZobrist();
    private long hash;

    public RicochetSolver(RicochetState state, TranspositionTable table, RicochetStateSettings settings) {
        this.state = state;
        this.table = table;
        this.settings = settings;
        targetDistance = new int[settings.getSize() * settings.getSize()];
        botsOrder = new int[settings.getBotCount()];
    }

    public List<RicochetMove> solve(int targetBot, int targetSquare) {
        long millis = System.currentTimeMillis();
        nodes = 0;
        prunes = nonprunes = 0;
        ttHits = ttNonHits = 0;
        Arrays.fill(leafyNodes, 0);
        Arrays.fill(innerNodes, 0);
        this.targetBot = targetBot;
        populateTargetDistance(targetSquare);
        zobrist.initHashes(new Random(491), targetBot);//seed 491 creates unique hashes which always have at least 1 bit set in the topmost 33 bits
        table.clear();
        hash = 0;
        for (int bot = 0; bot < botsOrder.length; bot++) {
            botsOrder[bot] = bot;
            hash ^= zobrist.botSquareHash(bot, state.botSquare(bot));
        }
        botsOrder[targetBot] = 0;
        botsOrder[0] = targetBot;

        result = new LinkedList<>();
        int depth = 0;
        while (!search(depth, ~0)) {
            println("no solution for depth " + depth + " after " + (System.currentTimeMillis() - millis) + "ms");
            depth++;
        }
        millis = System.currentTimeMillis() - millis;
        println("solved with depth " + depth);
        println(nodes + "nodes in " + millis + "ms (" + nodes / millis + "knps)");
        println("branching: " + Math.pow(nodes, 1d / depth));
        int tableCount = table.count();
        println("used " + tableCount + " of " + table.size() + " available entries. (" + (double) tableCount / table.size() + " fillrate)");
        println("pruned directions amount " + prunes + "/" + (prunes + nonprunes) + " (" + (double) prunes / (prunes + nonprunes) + " prunerate)");
        println("tthits " + ttHits + "/" + (ttHits + ttNonHits) + " (" + (double) ttHits / (ttHits + ttNonHits) + " hitrate)");
        println(Arrays.stream(leafyNodes).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
        println(Arrays.stream(innerNodes).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
        return result;
    }

    private boolean search(int remainingDepth, int availableDirections) {
        nodes++;
        int minTargetDistance = targetDistance[state.botSquare(targetBot)];
        if (remainingDepth < minTargetDistance) {
            //early exit
            return false;
        }
        if (remainingDepth == minTargetDistance) {
            table.saveDepthIfEmpty(hash, remainingDepth);
            leafyNodes[remainingDepth]++;
            //target can only be reached by moving targetBot, if depth == 0 we already solved it
            if (remainingDepth == 0 || searchBotMoves(targetBot, remainingDepth, availableDirections)) {
                return true;
            }
        } else {
            table.saveDepth(hash, remainingDepth);
            innerNodes[remainingDepth]++;
            //normal search
            for (int currentBot : botsOrder) {
                if (searchBotMoves(currentBot, remainingDepth, availableDirections)) {
                    return true;
                }
            }
        }
        return false;
    }

    long[] leafyNodes = new long[10];
    long[] innerNodes = new long[25];
    long ttHits = 0, ttNonHits = 0;
    long prunes = 0, nonprunes = 0;

    private boolean searchBotMoves(int currentBot, int remainingDepth, int availableDirections) {
        int dirs = availableDirections >>> (4 * currentBot);
        for (int direction = 0; direction < 4; direction++) {
            if (((dirs >>> direction) & 1) == 0) {
                prunes++;
                continue;
            }
            nonprunes++;
            int from = state.botSquare(currentBot);
            int to = state.findMoveLimit(from, direction);
            if (from != to) {//skip if bot won't move
                long childHash = hash ^ zobrist.botSquareHash(currentBot, from) ^ zobrist.botSquareHash(currentBot, to);
                if (table.load(childHash, entry) && entry.depth >= remainingDepth - 1) {
                    ttHits++;
                    continue;
                }
                ttNonHits++;

                long prevHash = hash;
                hash = childHash;
                state.forceMove(currentBot, from, to);

                int nextAvailableDirections = nextAvailableDirections(availableDirections, currentBot, direction, to);
                boolean solved = search(remainingDepth - 1, nextAvailableDirections);
                hash = prevHash;
                state.forceMove(currentBot, to, from);//undo move
                if (solved) {
                    result.add(0, new RicochetMove(currentBot, direction));
                    return true;
                }
            }
        }
        return false;
    }

    private int nextAvailableDirections(int availableDirections, int movedBot, int lastDirection, int movedTo) {
        boolean horizontalMove = (lastDirection & 1) != 0;
        if (horizontalMove) {
            int upperNeighbor = state.neighborBot(movedTo, UP);
            if (upperNeighbor != -1) {
                availableDirections |= 1 << (4 * upperNeighbor + DOWN);
            }
            int lowerNeighbor = state.neighborBot(movedTo, DOWN);
            if (lowerNeighbor != -1) {
                availableDirections |= 1 << (4 * lowerNeighbor + UP);
            }
            int leftRight = (1 << LEFT) | (1 << RIGHT);
            int downUp = (1 << DOWN) | (1 << UP);
            availableDirections &= ~(leftRight << (4 * movedBot));
            availableDirections |= downUp << (4 * movedBot);
        } else {
            int upperNeighbor = state.neighborBot(movedTo, RIGHT);
            if (upperNeighbor != -1) {
                availableDirections |= 1 << (4 * upperNeighbor + LEFT);
            }
            int lowerNeighbor = state.neighborBot(movedTo, LEFT);
            if (lowerNeighbor != -1) {
                availableDirections |= 1 << (4 * lowerNeighbor + RIGHT);
            }
            int leftRight = (1 << LEFT) | (1 << RIGHT);
            int downUp = (1 << DOWN) | (1 << UP);
            availableDirections &= ~(downUp << (4 * movedBot));
            availableDirections |= leftRight << (4 * movedBot);
        }
        return availableDirections;
    }

    private void populateTargetDistance(int targetSquare) {
        Arrays.fill(targetDistance, Integer.MAX_VALUE);
        targetDistance[targetSquare] = 0;
        chainUpdateDistance(targetSquare);
    }

    private void chainUpdateDistance(int square) {
        int nextDistance = targetDistance[square] + 1;
        for (int direction = 0; direction < NUM_DIRECTIONS; direction++) {
            int target = state.findWall(square, direction);
            int currentSquare = square;
            while (currentSquare != target) {
                currentSquare += settings.getDirectionOffset(direction);
                if (targetDistance[currentSquare] > nextDistance) {
                    targetDistance[currentSquare] = nextDistance;
                    chainUpdateDistance(currentSquare);
                }
            }
        }
    }

    private void println(String s) {
        if (VERBOSE) {
            System.out.println(s);
        }
    }
}
