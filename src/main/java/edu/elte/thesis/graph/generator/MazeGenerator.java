package edu.elte.thesis.graph.generator;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;
import org.springframework.util.Assert;

/**
 * Interface to collect the common functionality of the generators.
 *
 * @author Viktoria Sinkovics
 */
public abstract class MazeGenerator {

    public Maze generate(int columns, int rows) {
        Assert.isTrue(columns > 0, "Columns must be positive.");
        Assert.isTrue(rows > 0, "Rows must be positive.");

        return new Maze(columns, rows);
    }

    protected void removeWalls(CellNode firstCell, CellNode secondCell) {
        if (firstCell.isUpperNeighbourOf(secondCell)) {
            firstCell.removeWall(WallPosition.SOUTH);
            secondCell.removeWall(WallPosition.SOUTH.opposite());

        } else if (firstCell.isLowerNeighbourOf(secondCell)) {
            firstCell.removeWall(WallPosition.NORTH);
            secondCell.removeWall(WallPosition.NORTH.opposite());

        } else if (firstCell.isLeftNeighbourOf(secondCell)) {
            firstCell.removeWall(WallPosition.EAST);
            secondCell.removeWall(WallPosition.EAST.opposite());

        } else if (firstCell.isRightNeighbourOf(secondCell)) {
            firstCell.removeWall(WallPosition.WEST);
            secondCell.removeWall(WallPosition.WEST.opposite());
        }
    }


}
