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
public class RicochetIO {

    public void writeText(SimpleRicochetState state, String file) throws FileNotFoundException, UnsupportedEncodingException {
        RicochetStateSettings settings = state.getSettings();
        try (PrintWriter out = new PrintWriter(file, StandardCharsets.UTF_8.name())) {
            for (int y = 0; y < settings.getSize(); y++) {
                for (int x = 0; x < settings.getSize(); x++) {
                    int square = settings.square(x, y);
                    int value = 0;
                    for (int direction = 0; direction < RicochetStateSettings.NUM_DIRECTIONS; direction++) {
                        if (state.isWall(square, direction)) {
                            value |= 1 << direction;
                        }
                    }
                    out.write(Character.forDigit(value, 16));
                }
                out.println();
            }
            for (int bot = 0; bot < settings.getBotCount(); bot++) {
                out.write(Integer.toHexString(state.botSquare(bot)));
                out.println();
            }
        }
    }

    public void readText(SimpleRicochetState state, String file) throws IOException {
        RicochetStateSettings settings = state.getSettings();
        List<String> lines = Files.readAllLines(Paths.get(file), StandardCharsets.UTF_8);
        for (int y = 0; y < settings.getSize(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < settings.getSize(); x++) {
                int square = settings.square(x, y);
                int value = Character.digit(line.charAt(x), 16);
                for (int direction = 0; direction < RicochetStateSettings.NUM_DIRECTIONS; direction++) {
                    if ((value & (1 << direction)) != 0 && !state.isWall(square, direction)) {
                        state.setWall(square, direction);
                    }
                }
            }
        }
        for (int bot = 0; bot < settings.getBotCount(); bot++) {
            String line = lines.get(bot + settings.getSize());
            state.addBot(bot, Integer.parseInt(line, 16));
        }
    }

    public void write(SimpleRicochetState state, String file) throws IOException {
        RicochetStateSettings settings = state.getSettings();
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(file))) {
            for (int square = 0; square < settings.getSize() * settings.getSize(); square++) {
                if (settings.y(square) > 0) {
                    out.writeBoolean(state.isWall(square, RicochetStateSettings.DOWN));
                }
                if (settings.x(square) > 0) {
                    out.writeBoolean(state.isWall(square, RicochetStateSettings.LEFT));
                }
            }
            for (int bot = 0; bot < settings.getBotCount(); bot++) {
                int botSquare = state.botSquare(bot);
                out.writeByte(botSquare);
            }
        }
    }

    public void read(SimpleRicochetState state, String file) throws IOException {
        RicochetStateSettings settings = state.getSettings();
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            for (int square = 0; square < settings.getSize() * settings.getSize(); square++) {
                if (settings.y(square) > 0 && in.readBoolean()) {
                    state.setWall(square, RicochetStateSettings.DOWN);
                }
                if (settings.x(square) > 0 && in.readBoolean()) {
                    state.setWall(square, RicochetStateSettings.LEFT);
                }
            }
            for (int bot = 0; bot < settings.getBotCount(); bot++) {
                int square = in.readByte() & 0xff;
                if (0 <= square) {
                    state.addBot(bot, square);
                }
            }
        }
    }
}
