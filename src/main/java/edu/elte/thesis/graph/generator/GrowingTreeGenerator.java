package edu.elte.thesis.graph.generator;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.graph.CellNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Maze generator that implements the growing tree algorithm.
 *
 * @author Viktoria Sinkovics
 */
public class GrowingTreeGenerator extends MazeGenerator {

    private static final Logger LOGGER = LogManager.getLogger(GrowingTreeGenerator.class);

    List<CellNode> carvedCells;

    public GrowingTreeGenerator() {
        carvedCells = new ArrayList<>();
    }

    @Override
    public Maze generate(int columns, int rows) {
        Maze maze = super.generate(columns, rows);

        CellNode currentCell = maze.selectStartPoint(true);
        currentCell.makeRoot();
        currentCell.markAsVisited();
        carvedCells.add(currentCell);

        CellNode nextCell;
        Random randomNeighbourIndex = new SecureRandom();
        Random randomCarvedCellIndex = new SecureRandom();

        do {
            currentCell = carvedCells.get(randomCarvedCellIndex.nextInt(carvedCells.size()));

            List<CellNode> unvisitedNeighbours = maze.findUnvisitedNeighboursOf(currentCell.getEntity());
            Optional<CellNode> anyUnvisitedNeighbour = unvisitedNeighbours.isEmpty()
                    ? Optional.empty()
                    : Optional.ofNullable(unvisitedNeighbours.get(randomNeighbourIndex.nextInt(unvisitedNeighbours.size())));

            if (anyUnvisitedNeighbour.isPresent()) {
                nextCell = anyUnvisitedNeighbour.get();
                nextCell.setParent(currentCell);
                currentCell.addChild(nextCell);

                removeWalls(currentCell, nextCell);

                nextCell.markAsVisited();
                carvedCells.add(nextCell);
            } else {
                LOGGER.info("No unvisited neighbours were found for current cell=({},{}), removing from list.", currentCell.getColumn(), currentCell.getRow());
                carvedCells.remove(currentCell);
            }

        } while (!carvedCells.isEmpty());

        LOGGER.debug(generateMazeText(maze));

        return maze;
    }
}
