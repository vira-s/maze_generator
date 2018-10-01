package model.cell;

import model.Maze;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a cell in the {@link Maze}. A cell has a fixed position, four walls and is either visited or not.
 *
 * @author Viktoria Sinkovics on 10/1/2018
 */
public class MazeCell {

    private static final Logger LOGGER = LogManager.getLogger(MazeCell.class);

    private final int row;

    private final int column;

    private boolean visited;

    private List<Wall> walls;

    public MazeCell(int row, int column) {
        Assert.isTrue(row >= 0, "Row must be non-negative.");
        Assert.isTrue(column >= 0, "Column must be non-negative.");

        this.row = row;
        this.column = column;
        this.visited = false;
        this.walls = Arrays.asList(
                new Wall(WallPosition.NORTH),
                new Wall(WallPosition.EAST),
                new Wall(WallPosition.SOUTH),
                new Wall(WallPosition.WEST));
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
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
     * @param position
     *
     * @return
     */
    public Wall getWallByPosition(WallPosition position) {
        Assert.notNull(position, "Position must not be null.");

        List<Wall> filteredWalls = this.walls.stream()
                .filter(wall -> wall.getPosition().equals(WallPosition.SOUTH))
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

        LOGGER.info("Removing wall from " + position + " side.");

        this.walls = this.walls.stream()
                .map(wall -> wall.getPosition().equals(position)
                        ? wall.setInvisible()
                        : wall)
                .collect(Collectors.toList());

        LOGGER.debug("Walls: " + walls);
    }

    /**
     * Marks the cell as already visited.
     */
    public void markAsVisited() {
        LOGGER.info("Marking cell " + this + " visited.");

        Assert.isTrue(!this.visited, "Cell is already visited. " + this);

        this.visited = true;
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
        return row == cell.row &&
                column == cell.column
                && visited == cell.visited
                && Objects.equals(walls, cell.walls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column, visited, walls);
    }

    @Override
    public String toString() {
        return "MazeCell{"
                + "row=" + row
                + ", column=" + column
                + ", visited=" + visited
                + ", walls=" + walls
                + '}';
    }
}
