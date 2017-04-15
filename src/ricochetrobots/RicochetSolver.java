package ricochetrobots;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import static ricochetrobots.RicochetUtil.*;

/**
 *
 * @author Philipp
 */
public class RicochetSolver {
    private final static boolean VERBOSE = true, DIRECTION_PRUNING = true;
    private final List<Integer> result = new LinkedList<>();
    private final TranspositionTable table;
    private final TranspositionEntry entry = new TranspositionEntry();
    private final int[] targetDistance = new int[SIZE * SIZE];
    private long nodes;
    private final int[] bots = new int[NUM_BOTS];
    private int targetBot;
    private final RicochetState state;
    private final RicochetZobrist zobrist = new RicochetZobrist();
    private long hash;

    public RicochetSolver(RicochetState state, TranspositionTable table) {
        this.state = state;
        this.table = table;
    }
    
    public List<Integer> solve(int targetBot, int targetSquare) {
        long millis = System.currentTimeMillis();
        nodes = 0;
        prunes = nonprunes = 0;
        ttHits = ttNonHits = 0;
        this.targetBot = targetBot;
        populateTargetDistance(targetSquare);
        zobrist.initHashes(new Random(17), targetBot);
        hash = 0;
        for (int bot : bots) {
            hash ^= zobrist.botSquareHash(bot, state.botSquare(bot));
        }
        table.clear();
        result.clear();
        for (int bot = 0; bot < bots.length; bot++) {
            bots[bot] = bot;
        }
        bots[targetBot] = 0;
        bots[0] = targetBot;
        int depth = 0;
        while (!search(depth, 0)) {
            println("no solution for depth " + depth + " after " + (System.currentTimeMillis() - millis) + "ms");
            depth++;
        }
        millis = System.currentTimeMillis() - millis;
        println("solved with depth " + depth);
        println(nodes + "nodes in " + millis + "ms (" + nodes / millis + "knps)");
        println("branching: " + String.format("%s", Math.pow(nodes, 1d / depth)));
        int tableCount = table.count();
        println("used " + tableCount + " of " + table.size() + " available entries. (" + String.format("%s", (double)tableCount / table.size()) + " fillrate)");
        if(DIRECTION_PRUNING) {
            println("pruned directions amount " + prunes + "/" + (prunes + nonprunes));
        }
            println("tthits " + ttHits + "/" + (ttHits + ttNonHits));
        return result;
    }

    private boolean search(int remainingDepth, int pruneDirectionsFlags) {
        nodes++;
        int minTargetDistance = targetDistance[state.botSquare(targetBot)]; //targetDistance[state.botSquare(targetBot)];
        if(remainingDepth < minTargetDistance) {
            //early exit
            return false;
        }
        table.saveDepth(hash, remainingDepth);
        if(remainingDepth == minTargetDistance) {
            //target can only be reached by moving targetBot, if depth == 0 we already solved it
            if (remainingDepth == 0 || searchBotMoves(targetBot, remainingDepth, pruneDirectionsFlags)) {
                return true;
            }
        } else {
            //normal search
            for (int currentBot : bots) {
                if (searchBotMoves(currentBot, remainingDepth, pruneDirectionsFlags)) {
                    return true;
                }
            }
        }
        return false;
    }

    long ttHits = 0, ttNonHits = 0;
    long prunes = 0, nonprunes = 0;
    private boolean searchBotMoves(int currentBot, int remainingDepth, int pruneDirectionsFlags) {
        int increment, startDirection;
        if(DIRECTION_PRUNING && ((2 << currentBot) & pruneDirectionsFlags) != 0) {
            increment = 2;
            startDirection = ~pruneDirectionsFlags & 1;
            prunes++;
        } else {
            increment = 1;
            startDirection = 0;
            nonprunes++;
        }
        for (int direction = startDirection; direction < NUM_DIRECTIONS; direction += increment) {
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
                
                int nextPruneDirectionsFlags;
                if(DIRECTION_PRUNING) {
                    if(((direction ^ pruneDirectionsFlags) & 1) != 0) {
                        nextPruneDirectionsFlags = direction & 1;
                    } else {
                        nextPruneDirectionsFlags = pruneDirectionsFlags;
                    }
                    nextPruneDirectionsFlags |= 2 << currentBot;
                } else {
                    nextPruneDirectionsFlags = 0;
                }
                
                boolean solved = search(remainingDepth - 1, nextPruneDirectionsFlags);
                hash = prevHash;
                state.forceMove(currentBot, to, from);//undo move
                if (solved) {
                    result.add(0, direction);
                    result.add(0, currentBot);
                    return true;
                }
            }
        }
        return false;
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
            while(currentSquare != target) {
                currentSquare += DIRECTION_OFFSETS[direction];
                if(targetDistance[currentSquare] > nextDistance) {
                    targetDistance[currentSquare] = nextDistance;
                    chainUpdateDistance(currentSquare);
                }
            }
        }
    }
    
    private void println(String s) {
        if(VERBOSE) {
            System.out.println(s);
        }
    }
}
