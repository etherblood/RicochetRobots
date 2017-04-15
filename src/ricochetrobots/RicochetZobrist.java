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
}
