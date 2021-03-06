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

/**
 * Maze generator that implements the 'hunt and kill' algorithm.
 *
 * @author Viktoria Sinkovics
 */
public class HuntAndKillGenerator extends MazeGenerator {

    private static final Logger LOGGER = LogManager.getLogger(HuntAndKillGenerator.class);

    @Override
    public Maze generate(int columns, int rows) {
        Maze maze = super.generate(columns, rows);

        CellNode currentCell = maze.selectStartPoint(true);
        currentCell.makeRoot();
        currentCell.markAsVisited();

        CellNode nextCell;
        Random randomNeighbourIndex = new SecureRandom();

        while (!maze.isComplete()) {
            List<CellNode> unvisitedNeighbours = maze.findUnvisitedNeighboursOf(currentCell.getEntity());
            Optional<CellNode> anyUnvisitedNeighbour = unvisitedNeighbours.isEmpty()
                    ? Optional.empty()
                    : Optional.ofNullable(unvisitedNeighbours.get(randomNeighbourIndex.nextInt(unvisitedNeighbours.size())));

            if (anyUnvisitedNeighbour.isPresent()) {
                nextCell = anyUnvisitedNeighbour.get();
            } else {
                Optional<CellNode> huntResult = hunt(maze);

                nextCell = huntResult.orElseThrow(() ->
                        new IllegalArgumentException("Maze is not complete, but hunting didn't find any unvisited cells."));
                LOGGER.debug("Found next cell: ({},{})", nextCell.getColumn(), nextCell.getRow());

                currentCell = maze.findNeighboursOf(nextCell.getEntity())
                        .stream()
                        .filter(CellNode::isVisited)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("No visited neighbour was found for cell during hunt."));
            }

            nextCell.setParent(currentCell);
            currentCell.addChild(nextCell);

            removeWalls(currentCell, nextCell);

            currentCell = nextCell;

            currentCell.markAsVisited();

        }

        return maze;

    }

    private Optional<CellNode> hunt(Maze maze) {
        Assert.notNull(maze, "maze should not be null.");
        LOGGER.info("Hunting..");

        Optional<CellNode> unvisitedCellWithVisitedNeighbour = maze.getNodes().stream()
                .filter(cell -> !cell.isVisited()
                        && maze.findNeighboursOf(cell.getEntity()).stream()
                        .anyMatch(CellNode::isVisited))
                .findAny();

        unvisitedCellWithVisitedNeighbour.ifPresent(cell -> LOGGER.info("Found an unvisited cell with a visited neighbour: ({},{})", cell.getColumn(), cell.getRow()));

        return unvisitedCellWithVisitedNeighbour;
    }
}
