package ricochetrobots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import static ricochetrobots.RicochetUtil.*;

/**
 *
 * @author Philipp
 */
public class BoardPanel extends JPanel {

    private final RicochetState state;
    private final Color[] colors;
    public int target = -1;
    public Color targetColor = Color.WHITE;

    public BoardPanel(RicochetState state, Color[] colors) {
        this.state = state;
        this.colors = colors;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform t = AffineTransform.getTranslateInstance(0, getHeight());
        t.scale(1, -1);
        g2d.transform(t);
        
        float tileWidth = getWidth() / SIZE;
        float tileHeight = getHeight() / SIZE;
        
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int square = square(x, y);
                if(square == target) {
                    g.setColor(targetColor);
                    g.fillRect((int)(x(square) * tileWidth), (int)(y(square) * tileHeight), (int)tileWidth, (int)tileHeight);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                }
                if (state.isWall(square, DOWN)) {
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setStroke(new BasicStroke(1));
                    g2d.setColor(Color.DARK_GRAY);
                }
                g.drawLine((int) (x * tileWidth), (int) (y * tileHeight), (int) ((x + 1) * tileWidth), (int) (y * tileHeight));
                if (state.isWall(square, UP)) {
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setStroke(new BasicStroke(1));
                    g2d.setColor(Color.DARK_GRAY);
                }
                g.drawLine((int) (x * tileWidth), (int) ((y + 1) * tileHeight), (int) ((x + 1) * tileWidth), (int) ((y + 1) * tileHeight));
                if (state.isWall(square, LEFT)) {
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setStroke(new BasicStroke(1));
                    g2d.setColor(Color.DARK_GRAY);
                }
                g.drawLine((int) (x * tileWidth), (int) (y * tileHeight), (int) (x * tileWidth), (int) ((y + 1) * tileHeight));
                if (state.isWall(square, RIGHT)) {
                    g2d.setStroke(new BasicStroke(3));
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setStroke(new BasicStroke(1));
                    g2d.setColor(Color.DARK_GRAY);
                }
                g.drawLine((int) ((x + 1) * tileWidth), (int) (y * tileHeight), (int) ((x + 1) * tileWidth), (int) ((y + 1) * tileHeight));
            }
        }

        for (int bot = 0; bot < NUM_BOTS; bot++) {
            g.setColor(colors[bot]);
            int square = state.botSquare(bot);
            g.fillOval((int)(x(square) * tileWidth), (int)(y(square) * tileHeight), (int)tileWidth, (int)tileHeight);
        }
    }

}
