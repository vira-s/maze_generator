package graph.generator;

import model.Maze;
import model.cell.WallPosition;
import model.graph.CellNode;

/**
 * Interface to collect the common functionality of the generators.
 *
 * @author Viktoria Sinkovics
 */
public abstract class MazeGenerator {

    abstract Maze generate(int columns, int rows);

    public static String generateMazeText(Maze maze) {
        int rows = maze.getRows();
        int columns = maze.getColumns();

        StringBuilder builder = new StringBuilder("\n");

        for (int row = 0; row < rows; row++) {

            builder.append(generateTextForTop(maze, columns, row));
            builder.append(generateTextForMiddle(maze, columns, row));
            builder.append(generateTextForBottom(maze, columns, row));
        }

        return builder.toString();
    }

    private static String generateTextForTop(Maze maze, int columns, int row) {
        StringBuilder builder = new StringBuilder();

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);
            if (current.getWallByPosition(WallPosition.NORTH).isVisible()) {
                builder.append("###");
            } else {
                builder.append("#.#");
            }
        }
        builder.append("\n");
        return builder.toString();
    }

    private static String generateTextForMiddle(Maze maze, int columns, int row) {
        StringBuilder builder = new StringBuilder();

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);

            if (current.getWallByPosition(WallPosition.EAST).isVisible()
                    && current.getWallByPosition(WallPosition.WEST).isVisible()) {
                builder.append("#.#");
            } else if (!current.getWallByPosition(WallPosition.EAST).isVisible()
                    && current.getWallByPosition(WallPosition.WEST).isVisible()) {
                builder.append("#..");
            } else if (current.getWallByPosition(WallPosition.EAST).isVisible()
                    && !current.getWallByPosition(WallPosition.WEST).isVisible()) {
                builder.append("..#");
            } else if (!current.getWallByPosition(WallPosition.EAST).isVisible()
                    && !current.getWallByPosition(WallPosition.WEST).isVisible()) {
                builder.append("...");
            }
        }
        builder.append("\n");
        return builder.toString();
    }

    private static String generateTextForBottom(Maze maze, int columns, int row) {
        StringBuilder builder = new StringBuilder();

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);

            if (current.getWallByPosition(WallPosition.SOUTH).isVisible()) {
                builder.append("###");
            } else {
                builder.append("#.#");
            }
        }
        builder.append("\n");
        return builder.toString();
    }

}
