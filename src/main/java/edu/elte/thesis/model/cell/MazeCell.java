package edu.elte.thesis.model.cell;

import edu.elte.thesis.model.Maze;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a cell in the {@link Maze}. A cell has a fixed position, four walls and is either visited or not.
 *
 * @author Viktoria Sinkovics
 */
public class MazeCell {

    private static final Logger LOGGER = LogManager.getLogger(MazeCell.class);

    private final int column;

    private final int row;

    private boolean visited;

    private List<Wall> walls;

    public MazeCell(int column, int row) {
        Assert.isTrue(column >= 0, "Column must be non-negative.");
        Assert.isTrue(row >= 0, "Row must be non-negative.");

        this.column = column;
        this.row = row;
        this.visited = false;
        this.walls = Arrays.asList(
                new Wall(WallPosition.NORTH),
                new Wall(WallPosition.EAST),
                new Wall(WallPosition.SOUTH),
                new Wall(WallPosition.WEST));
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public boolean isVisited() {
        return visited;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    /**
     * Retrieves a {@link Wall} by its {@link WallPosition}.
     *
     * @param position The position of the wall
     *
     * @return The {@link Wall} at the matching {@link WallPosition}
     */
    public Wall getWallByPosition(WallPosition position) {
        Assert.notNull(position, "Position must not be null.");

        List<Wall> filteredWalls = this.walls.stream()
                .filter(wall -> wall.getPosition().equals(position))
                .collect(Collectors.toList());
        Assert.isTrue(filteredWalls.size() == 1, "Exactly one wall must be matching. " + filteredWalls);

        return filteredWalls.get(0);
    }

    /**
     * Removes a {@link Wall} from the cell.
     *
     * @param position The position of the {@link Wall} to remove
     */
    public void removeWall(final WallPosition position) {
        Assert.notNull(position, "Position should not be null");

        LOGGER.debug("Removing wall from {} side.", position);

        this.walls = this.walls.stream()
                .map(wall -> wall.getPosition().equals(position)
                        ? wall.setInvisible()
                        : wall)
                .collect(Collectors.toList());
    }

    /**
     * Marks the cell as already visited.
     */
    public void markAsVisited() {
        LOGGER.debug("Marking cell as visited ({},{})", this.column, this.row);

        this.visited = true;
    }

    /**
     * Decides if this cell is a neighbour of the provided {@link MazeCell}.
     *
     * @param mazeCell The cell
     *
     * @return True if the current {@link MazeCell} is a neighbour of the provided one.
     */
    public boolean isNeighbourOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return isVerticalNeighbourOf(mazeCell) || isHorizontalNeighbourOf(mazeCell);
    }

    /**
     * Decides if this cell is a vertical neighbour of the provided {@link MazeCell}.
     *
     * @param mazeCell The cell
     *
     * @return True if the current {@link MazeCell} is a vertical neighbour of the provided one.
     */
    public boolean isVerticalNeighbourOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return isLowerNeighbourOf(mazeCell) || isUpperNeighbourOf(mazeCell);
    }

    /**
     * Decides if this cell is the south-side neighbour of the provided {@link MazeCell}.
     *
     * @param mazeCell The cell
     *
     * @return True if the current {@link MazeCell} is the south-side neighbour of the provided one.
     */
    public boolean isLowerNeighbourOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return this.column == mazeCell.getColumn() && this.row == (mazeCell.getRow() + 1);
    }

    /**
     * Decides if this cell is the north-side neighbour of the provided {@link MazeCell}.
     *
     * @param mazeCell The cell
     *
     * @return True if the current {@link MazeCell} is the north-side neighbour of the provided one.
     */
    public boolean isUpperNeighbourOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return this.column == mazeCell.getColumn() && this.row == (mazeCell.getRow() - 1);
    }

    /**
     * Decides if this cell is a horizontal neighbour of the provided {@link MazeCell}.
     *
     * @param mazeCell The cell
     *
     * @return True if the current {@link MazeCell} is a horizontal neighbour of the provided one.
     */
    public boolean isHorizontalNeighbourOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return isLeftNeighbourOf(mazeCell) || isRightNeighbourOf(mazeCell);
    }

    /**
     * Decides if this cell is the west-side neighbour of the provided {@link MazeCell}.
     *
     * @param mazeCell The cell
     *
     * @return True if the current {@link MazeCell} is the west-side neighbour of the provided one.
     */
    public boolean isLeftNeighbourOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return this.row == mazeCell.getRow() && this.column == (mazeCell.getColumn() - 1);
    }

    /**
     * Decides if this cell is the east-side neighbour of the provided {@link MazeCell}.
     *
     * @param mazeCell The cell
     *
     * @return True if the current {@link MazeCell} is the east-side neighbour of the provided one.
     */
    public boolean isRightNeighbourOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return this.row == mazeCell.getRow() && this.column == (mazeCell.getColumn() + 1);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        MazeCell cell = (MazeCell) other;
        return  column == cell.column
                && row == cell.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    @Override
    public String toString() {
        return "MazeCell{"
                + "column=" + column
                + ", row=" + row
                + ", visited=" + visited
                + ", walls=" + walls
                + '}';
    }
}
