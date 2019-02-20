package edu.elte.thesis.graph.generator;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Maze generator that implements the sidewinder algorithm.
 *
 * @author Viktoria Sinkovics
 */
public class SidewinderGenerator extends MazeGenerator {

    private static final Logger LOGGER = LogManager.getLogger(SidewinderGenerator.class);

    private List<CellNode> runSet;

    public SidewinderGenerator() {
        runSet = new ArrayList<>();
    }

    @Override
    public Maze generate(int columns, int rows) {
        Maze maze = super.generate(columns, rows);

        CellNode currentCell;
        CellNode nextCell;
        Random shouldCarveToEast = new SecureRandom();
        Random indexOfCellToCarveNorth = new SecureRandom();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {

                currentCell = maze.getCellNodeByCoordinates(column, row);
                currentCell.markAsVisited();
                runSet.add(currentCell);

                if (column == 0 && row == 0) {
                    currentCell.makeRoot();
                }

                boolean shouldCarveEast = shouldCarveToEast.nextBoolean();

                if (row > 0 && (column == (columns - 1) || !shouldCarveEast)) {
                    currentCell = runSet.get(indexOfCellToCarveNorth.nextInt(runSet.size()));
                    LOGGER.debug("Carving passage to {} from cell=({},{})",
                            WallPosition.NORTH, currentCell.getColumn(), currentCell.getRow());

                    CellNode parentOfCurrentCell = maze.getCellNodeByCoordinates(currentCell.getColumn(), currentCell.getRow() - 1);
                    currentCell.setParent(parentOfCurrentCell);
                    parentOfCurrentCell.addChild(currentCell);

                    removeWalls(currentCell, parentOfCurrentCell);
                    setUpSubGraph(currentCell, runSet, maze);

                    runSet = new ArrayList<>();
                } else if ((row == 0 && column < (columns - 1))
                        || (row != 0 && column < (columns - 1))){

                    LOGGER.debug("Carving passage to {} from cell=({},{})",
                            WallPosition.EAST, currentCell.getColumn(), currentCell.getRow());

                    nextCell = maze.getCellNodeByCoordinates(column + 1, row);

                    removeWalls(currentCell, nextCell);
                } else {
                    setUpSubGraph(maze.getCellNodeByCoordinates(0, 0), runSet, maze);
                    runSet = new ArrayList<>();
                }
            }
        }

        return maze;
    }

    private void setUpSubGraph(CellNode startCell, List<CellNode> runSet, Maze maze) {
        if (runSet.size() == 1) {
            return;
        }

        handleNeighbours(maze, startCell, true);
        handleNeighbours(maze, startCell, false);
    }

    private void handleNeighbours(Maze maze, CellNode startCell, boolean right) {
        CellNode currentCell = startCell;
        int column = right
                ? currentCell.getColumn() + 1
                : currentCell.getColumn() - 1;
        boolean isNeighbourPresent = runSet.stream()
                .anyMatch(cellNode -> column == cellNode.getColumn());

        int col = column;
        CellNode nextCell;
        while (isNeighbourPresent) {
            nextCell = maze.getCellNodeByCoordinates(col, currentCell.getRow());

            nextCell.setParent(currentCell);
            currentCell.addChild(nextCell);

            currentCell = nextCell;
            int nextColumn = right
                    ? currentCell.getColumn() + 1
                    : currentCell.getColumn() - 1;
            isNeighbourPresent = runSet.stream()
                    .anyMatch(cellNode -> nextColumn == cellNode.getColumn());
            col = nextColumn;
        }
    }

}
