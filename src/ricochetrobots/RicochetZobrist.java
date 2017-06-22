package ricochetrobots;

import java.util.Random;
import static ricochetrobots.RicochetUtil.*;

/**
 *
 * @author Philipp
 */
public class RicochetZobrist {

    private final long[] botSquareHashes = new long[NUM_BOTS * SIZE * SIZE];

    public void initHashes(Random rng, int importantBot) {
        initSharedHashes(rng);
        initBotSpecificHashes(rng, importantBot);
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

    public long botSquareHash(int bot, int square) {
        return botSquareHashes[botSquareIndex(square, bot)];
    }
    
    public static long uniqueHash(RicochetState state, int importantBot) {
        long[] primes = computePrimes(SIZE * SIZE);
        return uniqueHash(state, importantBot, primes);
    }

    public static long uniqueHash(RicochetState state, int importantBot, long[] primes) {
        long id = 1;
        for (int bot = 0; bot < NUM_BOTS; bot++) {
            if(bot != importantBot) {
                id *= primes[state.botSquare(bot)];
            }
        }
        return id * SIZE * SIZE + state.botSquare(importantBot);
    }
    
    private static long[] computePrimes(int num) {
        long[] array = new long[num];
        array[0] = 2;
        long candidate = 3;
        for (int i = 1; i < array.length; candidate += 2) {
            for (int j = 0; j < i; j++) {
                long prime = array[j];
                if(candidate < prime * prime) {
                    array[i++] = candidate;
                    break;
                }
                if(candidate % prime == 0) {
                    break;
                }
            }
        }
        return array;
    }
}
