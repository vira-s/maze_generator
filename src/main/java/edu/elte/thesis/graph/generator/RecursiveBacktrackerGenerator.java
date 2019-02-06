package edu.elte.thesis.graph.generator;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.graph.CellNode;
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

                // TODO Rethink cell representation
                removeWalls(currentCell, nextCell);

                cells.push(currentCell);
                currentCell = nextCell;
            } else {
                currentCell = cells.pop();
            }
        } while (!cells.isEmpty());

        // TODO Rethink cell representation
         LOGGER.debug(generateMazeText(maze));

        return maze;
    }

}
