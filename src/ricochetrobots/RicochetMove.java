package ricochetrobots;

/**
 *
 * @author Philipp
 */
public class RicochetMove {
    private final int bot, direction;

    RicochetMove(int bot, int direction) {
        this.bot = bot;
        this.direction = direction;
    }

    public int getBot() {
        return bot;
    }

    public int getDirection() {
        return direction;
    }
}
