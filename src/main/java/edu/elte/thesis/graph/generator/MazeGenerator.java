package edu.elte.thesis.graph.generator;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;

/**
 * Interface to collect the common functionality of the generators.
 *
 * @author Viktoria Sinkovics
 */
public abstract class MazeGenerator extends MazeTextGenerator {

    abstract Maze generate(int columns, int rows);

    protected void removeWalls(CellNode currentCell, CellNode nextCell) {
        if (currentCell.isUpperNeighbourOf(nextCell)) {
            currentCell.removeWall(WallPosition.SOUTH);
            nextCell.removeWall(WallPosition.SOUTH.opposite());

        } else if (currentCell.isLowerNeighbourOf(nextCell)) {
            currentCell.removeWall(WallPosition.NORTH);
            nextCell.removeWall(WallPosition.NORTH.opposite());

        } else if (currentCell.isLeftNeighbourOf(nextCell)) {
            currentCell.removeWall(WallPosition.EAST);
            nextCell.removeWall(WallPosition.EAST.opposite());

        } else if (currentCell.isRightNeighbourOf(nextCell)) {
            currentCell.removeWall(WallPosition.WEST);
            nextCell.removeWall(WallPosition.WEST.opposite());
        }
    }


}
