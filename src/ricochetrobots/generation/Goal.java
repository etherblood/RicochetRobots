package ricochetrobots.generation;

/**
 *
 * @author Philipp
 */
public class Goal {
    private final int x, y, bot, symbol;

    public Goal(int x, int y, int bot, int symbol) {
        this.x = x;
        this.y = y;
        this.bot = bot;
        this.symbol = symbol;
    }
    
    public Goal rotateClockwise() {
        return new Goal(y, -x, bot, symbol);
    }
    
    public Goal translate(int x, int y) {
        return new Goal(this.x + x, this.y + y, bot, symbol);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getBot() {
        return bot;
    }

    public int getSymbol() {
        return symbol;
    }
}
