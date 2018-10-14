package graph.generator;

import model.Maze;
import model.cell.WallPosition;
import model.graph.CellNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

/**
 * Maze generator that implements the recursive backtracker algorithm.
 *
 * @author Viktoria Sinkovics
 */
public class RecursiveBacktrackerGenerator extends MazeGenerator {

    private static final Logger LOGGER = LogManager.getLogger(RecursiveBacktrackerGenerator.class);

    private Stack<CellNode> cells;

    public RecursiveBacktrackerGenerator() {
        cells = new Stack<>();
    }

    @Override
    public Maze generate(int columns, int rows) {
        Assert.isTrue(columns > 0, "Columns must be positive.");
        Assert.isTrue(rows > 0, "Rows must be positive.");

        Maze maze = new Maze(columns, rows);

        CellNode currentCell = maze.selectStartPoint(true);
        currentCell.setParent(null);
        currentCell.makeRoot();

        CellNode nextCell;
        Random randomNeighbourIndex = new SecureRandom();
        do {
            List<CellNode> unvisitedNeighbours = maze.findUnvisitedNeighboursOf(currentCell.getEntity());
            Optional<CellNode> anyUnvisitedNeighbour = unvisitedNeighbours.isEmpty()
                    ? Optional.empty()
                    : Optional.ofNullable(unvisitedNeighbours.get(randomNeighbourIndex.nextInt(unvisitedNeighbours.size())));

            currentCell.markAsVisited();
            if (anyUnvisitedNeighbour.isPresent()) {
                nextCell = anyUnvisitedNeighbour.get();
                nextCell.setParent(currentCell);
                currentCell.addChild(nextCell);

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

                cells.push(currentCell);
                currentCell = nextCell;
            } else {
                currentCell = cells.pop();
            }
        } while (!cells.isEmpty());

        LOGGER.debug(generateMazeText(maze));

        return maze;
    }

}
