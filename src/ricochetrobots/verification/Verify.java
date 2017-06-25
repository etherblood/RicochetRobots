package ricochetrobots.verification;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import ricochetrobots.RicochetZobrist;

/**
 *
 * @author Philipp
 */
public class Verify {

    public static void main(String[] args) throws IOException {
        long seed = args.length >= 1? Long.parseLong(args[0]) : 0;
        if(args.length != 0) {
            System.setOut(new PrintStream(Paths.get("seed" + seed + ".txt").toFile()));
        } else {
            System.out.println("logs will not be written to file due to missing arguments");
        }
        System.out.println(new Date() + " STARTED...");
        RicochetZobrist zobrist = new RicochetZobrist();
        zobrist.initHashes(new Random(seed), 0);
        long[] zobristHashes = new long[512];
        for (int i = 0; i < 256; i++) {
            zobristHashes[i] = zobrist.botSquareHash(1, i);
            zobristHashes[256 + i] = zobrist.botSquareHash(0, i);
        }
        long[] data = new long[2 << 26];
        for (int mask = 0; mask < 128; mask++) {
            doStuff(data, zobristHashes, 55, mask);
        }
        System.out.println(new Date() + " DONE");
    }

    private static void doStuff(long[] data, long[] zobrist, int shift, long mask) {
        int count = 0;
        for (int bot0 = 0; bot0 < 256; bot0++) {
            long hash0 = zobrist[256 + bot0];
            for (int bot1 = 0; bot1 < 253; bot1++) {
                if (bot0 == bot1) {
                    continue;
                }
                long hash1 = hash0 ^ zobrist[bot1];
                for (int bot2 = bot1 + 1; bot2 < 254; bot2++) {
                    if (bot0 == bot2) {
                        continue;
                    }
                    long hash2 = hash1 ^ zobrist[bot2];
                    for (int bot3 = bot2 + 1; bot3 < 255; bot3++) {
                        if (bot0 == bot3) {
                            continue;
                        }
                        long hash3 = hash2 ^ zobrist[bot3];
                        for (int bot4 = bot3 + 1; bot4 < 256; bot4++) {
                            if (bot0 == bot4) {
                                continue;
                            }
                            long hash4 = hash3 ^ zobrist[bot4];
                            if((hash4 >>> shift) == mask) {
                                data[count++] = hash4;
                            }
                        }
                    }
                }
            }
        }
        Arrays.sort(data, 0, count);
        for (int i = 0; i < count - 1; i++) {
            if(data[i] == data[i + 1]) {
                System.out.println(new Date() + " collision: " + Long.toHexString(data[i]));
            }
        }
        System.out.println(new Date() + " finished mask " + Long.toHexString(mask) + " with " + count + " hashes");
    }
}
