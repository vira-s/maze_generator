package model;


import model.cell.MazeCell;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a maze object.
 *
 * @author Viktoria Sinkovics on 10/1/2018
 */
public class Maze {

    private static final Logger LOGGER = LogManager.getLogger(Maze.class);

    private static final int DEFAULT_START_COLUMN = 0;

    private static final int DEFAULT_START_ROW = 0;

    private final int columns;

    private final int rows;

    private List<MazeCell> grid;

    public Maze(int columns, int rows) {
        Assert.isTrue(columns > 0, "Columns must be positive.");
        Assert.isTrue(rows > 0, "Rows must be positive.");

        this.columns = columns;
        this.rows = rows;
        this.grid = initializeGrid(columns, rows);
    }

    public Maze(int rows, int columns, List<MazeCell> grid) {
        Assert.isTrue(columns > 0, "Columns must be positive.");
        Assert.isTrue(rows > 0, "Rows must be positive.");
        Assert.notEmpty(grid, "Grid must not be empty.");
        Assert.isTrue(grid.size() == columns * rows,
                "Unexpected grid size (" + grid.size() + "), should be (" + columns * rows + ").");

        this.rows = rows;
        this.columns = columns;
        this.grid = grid;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<MazeCell> getGrid() {
        return grid;
    }

    /**
     * Retrieves a {@link MazeCell} by the given coordinates.
     *
     * @param column The columns of the {@link MazeCell}
     * @param row    The rows of the {@link MazeCell}
     *
     * @return The {@link MazeCell} with the matching position
     */
    public MazeCell getCellByCoordinates(int column, int row) {
        Assert.isTrue(column >= 0, "Column must be non-negative.");
        Assert.isTrue(this.columns > column, "Column must be smaller than " + this.columns + ".");
        Assert.isTrue(row >= 0, "Row must be non-negative.");
        Assert.isTrue(this.rows > row, "Row must be smaller than " + this.rows + ".");

        LOGGER.info("Received coordinates: column=" + column + ", row=" + row + ".");

        List<MazeCell> cellsAtCoordinate = grid.stream()
                .filter(cell -> cell.getColumn() == column && cell.getRow() == row)
                .collect(Collectors.toList());

        Assert.isTrue(cellsAtCoordinate.size() == 1, "Exactly one cell must be found: " + cellsAtCoordinate);

        LOGGER.info("Found cell by coordinates (" + column + "," + row + "): " + cellsAtCoordinate.get(0));

        return cellsAtCoordinate.get(0);
    }

    /**
     * Retrieves the neighbours of the provided {@link MazeCell}.
     *
     * @param mazeCell The {@link MazeCell}
     *
     * @return The neighbours of the provided {@link MazeCell}
     */
    public List<MazeCell> findNeighboursOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        LOGGER.info("Looking for neighbours of " + mazeCell);

        return grid.stream()
                .filter(cell -> cell.isNeighbourOf(mazeCell))
                .collect(Collectors.toList());
    }

    public List<MazeCell> findUnvisitedNeighboursOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        LOGGER.info("Looking for unvisited neighbours of " + mazeCell);

        return findNeighboursOf(mazeCell).stream()
                .filter(cell -> !cell.isVisited())
                .collect(Collectors.toList());
    }

    public MazeCell getStartPoint(boolean random) {
        MazeCell startCell;

        if (random) {
            Random randomNumber = new Random();

            int randomColumn = randomNumber.nextInt(columns);
            int randomRow = randomNumber.nextInt(rows);

            startCell = getCellByCoordinates(randomColumn, randomRow);
        } else {
            startCell = getCellByCoordinates(DEFAULT_START_COLUMN, DEFAULT_START_ROW);
        }

        return startCell;
    }

    private List<MazeCell> initializeGrid(int columns, int rows) {
        List<MazeCell> grid = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                MazeCell cell = new MazeCell(column, row);
                grid.add(cell);
            }
        }

        return grid;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Maze maze = (Maze) other;
        return rows == maze.rows
                && columns == maze.columns
                && Objects.equals(grid, maze.grid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, columns, grid);
    }

    @Override
    public String toString() {
        return "Maze{"
                + "rows=" + rows
                + ", columns=" + columns
                + ", grid=" + grid
                + '}';
    }
}
