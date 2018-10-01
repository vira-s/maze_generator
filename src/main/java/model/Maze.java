package model;


import model.cell.MazeCell;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a maze object.
 *
 * @author Viktoria Sinkovics on 10/1/2018
 */
public class Maze {

    private final int column;

    private final int row;

    private List<MazeCell> grid;

    public Maze(int column, int row) {
        this.column = column;
        this.row = row;
        // TODO initialize maze with Cells
        this.grid = new ArrayList<>(this.row * this.column);
    }

    public Maze(int row, int column, List<MazeCell> grid) {
        this.row = row;
        this.column = column;
        this.grid = grid;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public List<MazeCell> getGrid() {
        return grid;
    }

    /**
     * Retrieves a {@link MazeCell} by the given coordinates.
     *
     * @param column The column of the {@link MazeCell}
     * @param row The row of the {@link MazeCell}
     *
     * @return The {@link MazeCell} with the matching position
     */
    public MazeCell getCellByCoordinates(int column, int row) {
        Assert.isTrue(column >= 0, "Column must be non-negative.");
        Assert.isTrue(this.column > column, "Column must be smaller than " + this.column + ".");
        Assert.isTrue(row >= 0, "Row must be non-negative.");
        Assert.isTrue(this.row > row, "Row must be smaller than " + this.row + ".");

        List<MazeCell> cellsAtCoordinate = grid.stream()
                .filter(cell -> cell.getColumn() == column && cell.getRow() == row)
                .collect(Collectors.toList());

        Assert.isTrue(cellsAtCoordinate.size() == 1, "Exactly one cell must be found: " + cellsAtCoordinate);

        return cellsAtCoordinate.get(0);
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
        return row == maze.row
                && column == maze.column
                && Objects.equals(grid, maze.grid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, grid);
    }

    @Override
    public String toString() {
        return "Maze{"
                + "row=" + row
                + ", column=" + column
                + ", grid=" + grid
                + '}';
    }
}
