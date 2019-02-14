package edu.elte.thesis.graph.generator;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;
import org.apache.commons.lang3.StringUtils;

/**
 * Generates the string representation of {@link Maze}s.
 *
 * @author Viktoria Sinkovics
 */
public abstract class MazeTextGenerator {

    private static final char DEFAULT_WALL_MARKER = '1';

    /**
     * Walls and openings are defined as '1' and '0' respectively.
     * The maze's first row is a fixed wall, also each row has a wall as a prefix.
     * Only {@link WallPosition#EAST} and {@link WallPosition#SOUTH} are checked to avoid "double-walls" between cells.
     * Cells are represented as follows, where 'x' can either be a '1' or a '0':
     *  0x
     *  x1
     *
     * Example maze:
     *      1111111
     *      1010001
     *      1011101
     *      1000101
     *      1010101
     *      1010001
     *      1111111
     *
     * @param maze The maze
     * @return The text representation of the maze
     */
    public String generateMazeText(Maze maze) {
        int rows = maze.getRows();
        int columns = maze.getColumns();

        StringBuilder builder = new StringBuilder("\n");

        int headerElementCount = 1 + 2 * rows;
        builder.append(StringUtils.repeat(DEFAULT_WALL_MARKER, headerElementCount));

        for (int row = 0; row < rows; row++) {
            builder.append(generateTextForTop(maze, columns, row));
            builder.append(generateTextForBottom(maze, columns, row));
        }

        return builder.toString();
    }

    private String generateTextForTop(Maze maze, int columns, int row) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);
            if (col == 0) {
                builder.append(DEFAULT_WALL_MARKER);
            }

            if (current.getWallByPosition(WallPosition.EAST).isVisible()) {
                builder.append("01");
            } else {
                builder.append("00");
            }
        }

        return builder.toString();
    }

    private String generateTextForBottom(Maze maze, int columns, int row) {
        StringBuilder builder = new StringBuilder();
        builder.append("\n");

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);
            if (col == 0) {
                builder.append(DEFAULT_WALL_MARKER);
            }

            if (current.getWallByPosition(WallPosition.SOUTH).isVisible()) {
                builder.append("11");
            } else {
                builder.append("01");
            }
        }

        return builder.toString();
    }

}
