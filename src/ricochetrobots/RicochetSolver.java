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
    
    private List<RicochetMove> result;
    private final TranspositionTable table;
    private final TranspositionEntry entry = new TranspositionEntry();
    private final int[] targetDistance = new int[SIZE * SIZE];
    private long nodes;
    private final int[] botsOrder = new int[NUM_BOTS];
    private int targetBot;
    private final RicochetState state;
    private final RicochetZobrist zobrist = new RicochetZobrist();
    private long hash;

    public RicochetSolver(RicochetState state, TranspositionTable table) {
        this.state = state;
        this.table = table;
    }
    
    public List<RicochetMove> solve(int targetBot, int targetSquare) {
        long millis = System.currentTimeMillis();
        nodes = 0;
        prunes = nonprunes = 0;
        ttHits = ttNonHits = 0;
        this.targetBot = targetBot;
        populateTargetDistance(targetSquare);
        zobrist.initHashes(new Random(17), targetBot);
        table.clear();
        hash = 0;
        for (int bot = 0; bot < NUM_BOTS; bot++) {
            botsOrder[bot] = bot;
            hash ^= zobrist.botSquareHash(bot, state.botSquare(bot));
        }
        botsOrder[targetBot] = 0;
        botsOrder[0] = targetBot;
        
        result = new LinkedList<>();
        int depth = 0;
        while (!search(depth, 0)) {
            println("no solution for depth " + depth + " after " + (System.currentTimeMillis() - millis) + "ms");
            depth++;
        }
        millis = System.currentTimeMillis() - millis;
        println("solved with depth " + depth);
        println(nodes + "nodes in " + millis + "ms (" + nodes / millis + "knps)");
        println("branching: " + Math.pow(nodes, 1d / depth));
        int tableCount = table.count();
        println("used " + tableCount + " of " + table.size() + " available entries. (" + (double)tableCount / table.size() + " fillrate)");
        if(DIRECTION_PRUNING) {
            println("pruned directions amount " + prunes + "/" + (prunes + nonprunes) + " (" + (double) prunes / (prunes + nonprunes) + " prunerate)");
        }
            println("tthits " + ttHits + "/" + (ttHits + ttNonHits) + " (" + (double)ttHits / (ttHits + ttNonHits) + " hitrate)");
        return result;
    }

    private boolean search(int remainingDepth, int pruneDirectionsFlags) {
        nodes++;
        int minTargetDistance = targetDistance[state.botSquare(targetBot)];
        if(remainingDepth < minTargetDistance) {
            //early exit
            return false;
        }
        if(remainingDepth == minTargetDistance) {
            table.saveDepthIfEmpty(hash, remainingDepth);
            //target can only be reached by moving targetBot, if depth == 0 we already solved it
            if (remainingDepth == 0 || searchBotMoves(targetBot, remainingDepth, pruneDirectionsFlags)) {
                return true;
            }
        } else {
            table.saveDepth(hash, remainingDepth);
            //normal search
            for (int currentBot : botsOrder) {
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
                    result.add(0, new RicochetMove(currentBot, direction));
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
