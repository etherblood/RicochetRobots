package ricochetrobots.generation;

/**
 *
 * @author Philipp
 */
public class DefaultQuadrants {

    public static final int GOAL_CIRCLE = 0;
    public static final int GOAL_TRIANGLE = 1;
    public static final int GOAL_SQUARE = 2;
    public static final int GOAL_HEXAGON = 3;
    public static final int GOAL_VORTEX = 4;

    public static final QuadrantBuilder[] QUADRANTS = new QuadrantBuilder[16];

    static {
        QUADRANTS[0] = new QuadrantBuilder() // 1A
                .addWall(1, 0, "E")
                .addWall(4, 1, "NW").addGoal(4, 1, 0, GOAL_CIRCLE) // R
                .addWall(1, 2, "NE").addGoal(1, 2, 1, GOAL_TRIANGLE) // G
                .addWall(6, 3, "SE").addGoal(6, 3, 3, GOAL_HEXAGON) // Y
                .addWall(0, 5, "S")
                .addWall(3, 6, "SW").addGoal(3, 6, 2, GOAL_SQUARE) // B
                .addWall(7, 7, "SW");
        QUADRANTS[1] = new QuadrantBuilder() // 2A
                .addWall(3, 0, "E")
                .addWall(5, 1, "SE").addGoal(5, 1, 1, GOAL_HEXAGON) // G
                .addWall(1, 2, "SW").addGoal(1, 2, 0, GOAL_SQUARE) // R
                .addWall(0, 3, "S")
                .addWall(6, 4, "NW").addGoal(6, 4, 3, GOAL_CIRCLE) // Y
                .addWall(2, 6, "NE").addGoal(2, 6, 2, GOAL_TRIANGLE) // B
                .addWall(7, 7, "SW");
        QUADRANTS[2] = new QuadrantBuilder() // 3A
                .addWall(3, 0, "E")
                .addWall(5, 2, "SE").addGoal(5, 2, 2, GOAL_HEXAGON) // B
                .addWall(0, 4, "S")
                .addWall(2, 4, "NE").addGoal(2, 4, 1, GOAL_CIRCLE) // G
                .addWall(7, 5, "SW").addGoal(7, 5, 0, GOAL_TRIANGLE) // R
                .addWall(1, 6, "NW").addGoal(1, 6, 3, GOAL_SQUARE) // Y
                .addWall(7, 7, "SW");
        QUADRANTS[3] = new QuadrantBuilder() // 4A
                .addWall(3, 0, "E")
                .addWall(6, 1, "SW").addGoal(6, 1, 2, GOAL_CIRCLE) // B
                .addWall(1, 3, "NE").addGoal(1, 3, 3, GOAL_TRIANGLE) // Y
                .addWall(5, 4, "NW").addGoal(5, 4, 1, GOAL_SQUARE) // G
                .addWall(2, 5, "SE").addGoal(2, 5, 0, GOAL_HEXAGON) // R
                .addWall(7, 5, "SE").addGoal(7, 5, 4, GOAL_VORTEX) // W*
                .addWall(0, 6, "S")
                .addWall(7, 7, "SW");
        QUADRANTS[4] = new QuadrantBuilder() // 1B
                .addWall(4, 0, "E")
                .addWall(6, 1, "SE").addGoal(6, 1, 3, GOAL_HEXAGON) // Y
                .addWall(1, 2, "NW").addGoal(1, 2, 1, GOAL_TRIANGLE) // G
                .addWall(0, 5, "S")
                .addWall(6, 5, "NE").addGoal(6, 5, 2, GOAL_SQUARE) // B
                .addWall(3, 6, "SW").addGoal(3, 6, 0, GOAL_CIRCLE) // R
                .addWall(7, 7, "SW");
        QUADRANTS[5] = new QuadrantBuilder() // 2B
                .addWall(4, 0, "E")
                .addWall(2, 1, "NW").addGoal(2, 1, 3, GOAL_CIRCLE) // Y
                .addWall(6, 3, "SW").addGoal(6, 3, 2, GOAL_TRIANGLE) // B
                .addWall(0, 4, "S")
                .addWall(4, 5, "NE").addGoal(4, 5, 0, GOAL_SQUARE) // R
                .addWall(1, 6, "SE").addGoal(1, 6, 1, GOAL_HEXAGON) // G
                .addWall(7, 7, "SW");
        QUADRANTS[6] = new QuadrantBuilder() // 3B
                .addWall(3, 0, "E")
                .addWall(1, 1, "SW").addGoal(1, 1, 0, GOAL_TRIANGLE) // R
                .addWall(6, 2, "NE").addGoal(6, 2, 1, GOAL_CIRCLE) // G
                .addWall(2, 4, "SE").addGoal(2, 4, 2, GOAL_HEXAGON) // B
                .addWall(0, 5, "S")
                .addWall(7, 5, "NW").addGoal(7, 5, 3, GOAL_SQUARE) // Y
                .addWall(7, 7, "SW");
        QUADRANTS[7] = new QuadrantBuilder() // 4B
                .addWall(4, 0, "E")
                .addWall(2, 1, "SE").addGoal(2, 1, 0, GOAL_HEXAGON) // R
                .addWall(1, 3, "SW").addGoal(1, 3, 1, GOAL_SQUARE) // G
                .addWall(0, 4, "S")
                .addWall(6, 4, "NW").addGoal(6, 4, 3, GOAL_TRIANGLE) // Y
                .addWall(5, 6, "NE").addGoal(5, 6, 2, GOAL_CIRCLE) // B
                .addWall(3, 7, "SE").addGoal(3, 7, 4, GOAL_VORTEX) // W*
                .addWall(7, 7, "SW");
        QUADRANTS[8] = new QuadrantBuilder() // 1C
                .addWall(1, 0, "E")
                .addWall(3, 1, "NW").addGoal(3, 1, 1, GOAL_TRIANGLE) // G
                .addWall(6, 3, "SE").addGoal(6, 3, 3, GOAL_HEXAGON) // Y
                .addWall(1, 4, "SW").addGoal(1, 4, 0, GOAL_CIRCLE) // R
                .addWall(0, 6, "S")
                .addWall(4, 6, "NE").addGoal(4, 6, 2, GOAL_SQUARE) // B
                .addWall(7, 7, "SW");
        QUADRANTS[9] = new QuadrantBuilder() // 2C
                .addWall(5, 0, "E")
                .addWall(3, 2, "NW").addGoal(3, 2, 3, GOAL_CIRCLE) // Y
                .addWall(0, 3, "S")
                .addWall(5, 3, "SW").addGoal(5, 3, 2, GOAL_TRIANGLE) // B
                .addWall(2, 4, "NE").addGoal(2, 4, 0, GOAL_SQUARE) // R
                .addWall(4, 5, "SE").addGoal(4, 5, 1, GOAL_HEXAGON) // G
                .addWall(7, 7, "SW");
        QUADRANTS[10] = new QuadrantBuilder() // 3C
                .addWall(1, 0, "E")
                .addWall(4, 1, "NE").addGoal(4, 1, 1, GOAL_CIRCLE) // G
                .addWall(1, 3, "SW").addGoal(1, 3, 0, GOAL_TRIANGLE) // R
                .addWall(0, 5, "S")
                .addWall(5, 5, "NW").addGoal(5, 5, 3, GOAL_SQUARE) // Y
                .addWall(3, 6, "SE").addGoal(3, 6, 2, GOAL_HEXAGON) // B
                .addWall(7, 7, "SW");
        QUADRANTS[11] = new QuadrantBuilder() // 4C
                .addWall(2, 0, "E")
                .addWall(5, 1, "SW").addGoal(5, 1, 2, GOAL_CIRCLE) // B
                .addWall(7, 2, "SE").addGoal(7, 2, 4, GOAL_VORTEX) // W*
                .addWall(0, 3, "S")
                .addWall(3, 4, "SE").addGoal(3, 4, 0, GOAL_HEXAGON) // R
                .addWall(6, 5, "NW").addGoal(6, 5, 1, GOAL_SQUARE) // G
                .addWall(1, 6, "NE").addGoal(1, 6, 3, GOAL_TRIANGLE) // Y
                .addWall(7, 7, "SW");
        QUADRANTS[12] = new QuadrantBuilder() // 1D
                .addWall(5, 0, "E")
                .addWall(1, 3, "NW").addGoal(1, 3, 0, GOAL_CIRCLE) // R
                .addWall(6, 4, "SE").addGoal(6, 4, 3, GOAL_HEXAGON) // Y
                .addWall(0, 5, "S")
                .addWall(2, 6, "NE").addGoal(2, 6, 1, GOAL_TRIANGLE) // G
                .addWall(3, 6, "SW").addGoal(3, 6, 2, GOAL_SQUARE) // B
                .addWall(7, 7, "SW");
        QUADRANTS[13] = new QuadrantBuilder() // 2D
                .addWall(2, 0, "E")
                .addWall(5, 2, "SE").addGoal(5, 2, 1, GOAL_HEXAGON) // G
                .addWall(6, 2, "NW").addGoal(6, 2, 3, GOAL_CIRCLE) // Y
                .addWall(1, 5, "SW").addGoal(1, 5, 0, GOAL_SQUARE) // R
                .addWall(0, 6, "S")
                .addWall(4, 7, "NE").addGoal(4, 7, 2, GOAL_TRIANGLE) // B
                .addWall(7, 7, "SW");
        QUADRANTS[14] = new QuadrantBuilder() // 3D
                .addWall(4, 0, "E")
                .addWall(0, 2, "S")
                .addWall(6, 2, "SE").addGoal(6, 2, 2, GOAL_HEXAGON) // B
                .addWall(2, 4, "NE").addGoal(2, 4, 1, GOAL_CIRCLE) // G
                .addWall(3, 4, "SW").addGoal(3, 4, 0, GOAL_TRIANGLE) // R
                .addWall(5, 6, "NW").addGoal(5, 6, 3, GOAL_SQUARE) // Y
                .addWall(7, 7, "SW");
        QUADRANTS[15] = new QuadrantBuilder() // 4D
                .addWall(4, 0, "E")
                .addWall(6, 2, "NW").addGoal(6, 2, 3, GOAL_TRIANGLE) // Y
                .addWall(2, 3, "NE").addGoal(2, 3, 2, GOAL_CIRCLE) // B
                .addWall(3, 3, "SW").addGoal(3, 3, 1, GOAL_SQUARE) // G
                .addWall(1, 5, "SE").addGoal(1, 5, 0, GOAL_HEXAGON) // R
                .addWall(0, 6, "S")
                .addWall(5, 7, "SE").addGoal(5, 7, 4, GOAL_VORTEX) // W*
                .addWall(7, 7, "SW");
    }
}
