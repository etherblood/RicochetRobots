package ricochetrobots;

import java.util.Random;

/**
 *
 * @author Philipp
 */
public class RicochetZobrist {

    private final RicochetStateSettings settings;
    private final long[] botSquareHashes;

    public RicochetZobrist(RicochetStateSettings settings) {
        this.settings = settings;
        botSquareHashes = new long[settings.getBotCount() * settings.getSize() * settings.getSize()];
    }

    public void initHashes(Random rng, int importantBot) {
        initSharedHashes(rng);
        initBotSpecificHashes(rng, importantBot);
    }

    private void initSharedHashes(Random rng) {
        for (int square = 0; square < settings.getSize() * settings.getSize(); square++) {
            long sharedHash = rng.nextLong();
            for (int bot = 0; bot < settings.getBotCount(); bot++) {
                botSquareHashes[settings.botSquareIndex(square, bot)] = sharedHash;
            }
        }
    }

    private void initBotSpecificHashes(Random rng, int bot) {
        for (int square = 0; square < settings.getSize() * settings.getSize(); square++) {
            long specificHash = rng.nextLong();
            botSquareHashes[settings.botSquareIndex(square, bot)] = specificHash;
        }
    }

    public long botSquareHash(int bot, int square) {
        return botSquareHashes[settings.botSquareIndex(square, bot)];
    }

    public static long uniqueHash(RicochetState state, int importantBot) {
        RicochetStateSettings settings = state.getSettings();
        long[] primes = computePrimes(settings.getSize() * settings.getSize());
        return uniqueHash(state, importantBot, primes);
    }

    public static long uniqueHash(RicochetState state, int importantBot, long[] primes) {
        RicochetStateSettings settings = state.getSettings();
        long id = 1;
        for (int bot = 0; bot < settings.getBotCount(); bot++) {
            if (bot != importantBot) {
                id *= primes[state.botSquare(bot)];
            }
        }
        return id * settings.getSize() * settings.getSize() + state.botSquare(importantBot);
    }

    private static long[] computePrimes(int num) {
        long[] array = new long[num];
        array[0] = 2;
        long candidate = 3;
        for (int i = 1; i < array.length; candidate += 2) {
            for (int j = 0; j < i; j++) {
                long prime = array[j];
                if (candidate < prime * prime) {
                    array[i++] = candidate;
                    break;
                }
                if (candidate % prime == 0) {
                    break;
                }
            }
        }
        return array;
    }
}
