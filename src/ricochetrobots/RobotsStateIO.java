package ricochetrobots;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class RobotsStateIO {

    public void writeText(RobotsState state, String file) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8.name())) {
            for (int y = 0; y < RobotsState.SIZE; y++) {
                for (int x = 0; x < RobotsState.SIZE; x++) {
                    int square = RobotsState.square(x, y);
                    int value = 0;
                    for (int direction = 0; direction < RobotsState.NUM_DIRECTIONS; direction++) {
                        if (state.isWall(square, direction)) {
                            value |= 1 << direction;
                        }
                    }
                    out.write(Character.forDigit(value, 16));
                }
                out.println();
            }
            for (int bot = 0; bot < RobotsState.NUM_BOTS; bot++) {
                out.write(Integer.toHexString(state.botSquare(bot)));
                out.println();
            }
        }
    }

    public void readText(RobotsState state, String file) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
        for (int y = 0; y < RobotsState.SIZE; y++) {
            String line = lines.get(y);
                for (int x = 0; x < RobotsState.SIZE; x++) {
                    int square = RobotsState.square(x, y);
                    int value = Character.digit(line.charAt(x), 16);
                    for (int direction = 0; direction < RobotsState.NUM_DIRECTIONS; direction++) {
                        if((value & (1 << direction)) != 0 && !state.isWall(square, direction)) {
                            state.setWall(square, direction);
                        }
                    }
                }
            }
            for (int bot = 0; bot < RobotsState.NUM_BOTS; bot++) {
                String line = lines.get(bot + RobotsState.SIZE);
                state.addBot(bot, Integer.parseInt(line, 16));
            }
    }

    public void write(RobotsState state, String file) throws IOException {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            for (int square = 0; square < RobotsState.SIZE * RobotsState.SIZE; square++) {
                if (RobotsState.y(square) > 0) {
                    out.writeBoolean(state.isWall(square, RobotsState.DOWN));
                }
                if (RobotsState.x(square) > 0) {
                    out.writeBoolean(state.isWall(square, RobotsState.LEFT));
                }
            }
            for (int bot = 0; bot < RobotsState.NUM_BOTS; bot++) {
                int botSquare = state.botSquare(bot);
                out.writeByte(botSquare);
            }
        }
    }

    public void read(RobotsState state, String file) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            for (int square = 0; square < RobotsState.SIZE * RobotsState.SIZE; square++) {
                if (RobotsState.y(square) > 0 && in.readBoolean()) {
                    state.setWall(square, RobotsState.DOWN);
                }
                if (RobotsState.x(square) > 0 && in.readBoolean()) {
                    state.setWall(square, RobotsState.LEFT);
                }
            }
            for (int bot = 0; bot < RobotsState.NUM_BOTS; bot++) {
                int square = in.readByte() & 0xff;
                if (0 <= square) {
                    state.addBot(bot, square);
                }
            }
        }
    }
}
