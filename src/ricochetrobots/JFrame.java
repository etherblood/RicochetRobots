package ricochetrobots;

import java.awt.Color;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class JFrame extends javax.swing.JFrame {

    private final String[] botTexts = new String[RobotsState.NUM_BOTS];
    private final String[] directionTexts = new String[RobotsState.NUM_DIRECTIONS];
    private final Color[] colors = new Color[RobotsState.NUM_BOTS];

    private final int red = 0;
    private final int blue = 1;
    private final int yellow = 2;
    private final int green = 3;
    private final int silver = 4;

    {
        botTexts[red] = "red ";
        botTexts[blue] = "blue ";
        botTexts[yellow] = "yellow ";
        botTexts[green] = "green ";
        botTexts[silver] = "silver ";

        directionTexts[RobotsState.UP] = "up";
        directionTexts[RobotsState.LEFT] = "left";
        directionTexts[RobotsState.DOWN] = "down";
        directionTexts[RobotsState.RIGHT] = "right";

        colors[red] = Color.RED;
        colors[blue] = Color.BLUE;
        colors[yellow] = Color.YELLOW;
        colors[green] = Color.GREEN;
        colors[silver] = Color.GRAY;
    }

    /**
     * Creates new form JFrame
     */
    public JFrame() {
        initComponents();

        RobotsState state = new RobotsState();

        int targetBot = blue;
        int targetSquare = setup5bots22movesForBlue(state);

        new Thread(() -> {
            RobotsSolver solver = new RobotsSolver(state, new TranspositionTable(26));//26 -> 1 GB
            List<Integer> solve = solver.solve(targetBot, targetSquare);
            for (int i = 0; i < solve.size(); i += 2) {
                System.out.println(botTexts[solve.get(i)] + directionTexts[solve.get(i + 1)]);
            }
        }).start();

        BoardPanel panel = new BoardPanel(state, colors);
        panel.target = targetSquare;
        panel.targetColor = colors[targetBot];
        panel.setSize(500, 500);
        add(panel);
        panel.invalidate();
    }

    private int setup5bots22movesForBlue(RobotsState state) {
        state.addBot(red, RobotsState.square(2, 14));
        state.addBot(blue, RobotsState.square(11, 2));
        state.addBot(yellow, RobotsState.square(2, 1));
        state.addBot(green, RobotsState.square(0, 3));
        state.addBot(silver, RobotsState.square(2, 2));

        state.setWall(RobotsState.square(4, 0), RobotsState.RIGHT);
        state.setWall(RobotsState.square(9, 0), RobotsState.RIGHT);
        state.setWall(RobotsState.square(14, 0), RobotsState.UP);
        state.setWall(RobotsState.square(2, 1), RobotsState.UP);
        state.setWall(RobotsState.square(2, 1), RobotsState.RIGHT);
        state.setWall(RobotsState.square(13, 1), RobotsState.RIGHT);
        state.setWall(RobotsState.square(10, 2), RobotsState.RIGHT);
        state.setWall(RobotsState.square(11, 2), RobotsState.UP);
        state.setWall(RobotsState.square(0, 3), RobotsState.RIGHT);
        state.setWall(RobotsState.square(1, 3), RobotsState.UP);
        state.setWall(RobotsState.square(15, 3), RobotsState.UP);
        state.setWall(RobotsState.square(0, 4), RobotsState.UP);
        state.setWall(RobotsState.square(5, 4), RobotsState.RIGHT);
        state.setWall(RobotsState.square(5, 5), RobotsState.UP);

        state.setWall(RobotsState.square(5, 6), RobotsState.RIGHT);
        state.setWall(RobotsState.square(7, 6), RobotsState.UP);
        state.setWall(RobotsState.square(8, 6), RobotsState.UP);
        state.setWall(RobotsState.square(10, 6), RobotsState.UP);
        state.setWall(RobotsState.square(13, 6), RobotsState.UP);
        state.setWall(RobotsState.square(13, 6), RobotsState.RIGHT);
        state.setWall(RobotsState.square(3, 7), RobotsState.UP);
        state.setWall(RobotsState.square(3, 7), RobotsState.RIGHT);
        state.setWall(RobotsState.square(6, 7), RobotsState.RIGHT);
        state.setWall(RobotsState.square(8, 7), RobotsState.RIGHT);
        state.setWall(RobotsState.square(10, 7), RobotsState.RIGHT);
        state.setWall(RobotsState.square(6, 3), RobotsState.UP);

        state.setWall(RobotsState.square(7, 8), RobotsState.LEFT);
        state.setWall(RobotsState.square(7, 8), RobotsState.UP);
        state.setWall(RobotsState.square(8, 8), RobotsState.RIGHT);
        state.setWall(RobotsState.square(8, 8), RobotsState.UP);
        state.setWall(RobotsState.square(3, 9), RobotsState.DOWN);
        state.setWall(RobotsState.square(3, 9), RobotsState.RIGHT);
        state.setWall(RobotsState.square(14, 9), RobotsState.LEFT);
        state.setWall(RobotsState.square(14, 9), RobotsState.DOWN);
        state.setWall(RobotsState.square(11, 10), RobotsState.UP);
        state.setWall(RobotsState.square(11, 10), RobotsState.LEFT);
        state.setWall(RobotsState.square(1, 11), RobotsState.UP);
        state.setWall(RobotsState.square(1, 11), RobotsState.LEFT);

        state.setWall(RobotsState.square(2, 14), RobotsState.LEFT);
        state.setWall(RobotsState.square(2, 14), RobotsState.DOWN);
        state.setWall(RobotsState.square(0, 14), RobotsState.DOWN);
        state.setWall(RobotsState.square(6, 15), RobotsState.LEFT);
        state.setWall(RobotsState.square(11, 15), RobotsState.RIGHT);
        state.setWall(RobotsState.square(13, 14), RobotsState.UP);
        state.setWall(RobotsState.square(13, 14), RobotsState.RIGHT);
        state.setWall(RobotsState.square(9, 12), RobotsState.RIGHT);
        state.setWall(RobotsState.square(9, 12), RobotsState.DOWN);
        state.setWall(RobotsState.square(15, 10), RobotsState.UP);
        state.setWall(RobotsState.square(6, 12), RobotsState.UP);
        state.setWall(RobotsState.square(6, 12), RobotsState.RIGHT);

        return RobotsState.square(9, 12);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new JFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
