package edu.elte.thesis.graph.generator;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.cell.MazeCell;
import edu.elte.thesis.model.graph.CellNode;
import edu.elte.thesis.model.graph.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Maze generator that implements the random walk algorithm.
 *
 * @author Viktoria Sinkovics
 */
public class RandomWalkGenerator extends MazeGenerator {

    private static final Logger LOGGER = LogManager.getLogger(RandomWalkGenerator.class);

    @Override
    public Maze generate(int columns, int rows) {
        Maze maze = super.generate(columns, rows);

        CellNode currentCell = maze.selectStartPoint(true);
        currentCell.makeRoot();
        currentCell.markAsVisited();

        CellNode nextCell;
        Random randomNeighbourIndex = new SecureRandom();
        boolean isMazeReady;

        do {
            List<CellNode> unvisitedNeighbours = maze.findUnvisitedNeighboursOf(currentCell.getEntity());

            Optional<CellNode> anyUnvisitedNeighbour = unvisitedNeighbours.isEmpty()
                    ? Optional.empty()
                    : Optional.ofNullable(unvisitedNeighbours.get(randomNeighbourIndex.nextInt(unvisitedNeighbours.size())));

            if (anyUnvisitedNeighbour.isPresent()) {
                nextCell = anyUnvisitedNeighbour.get();

                nextCell.setParent(currentCell);
                currentCell.addChild(nextCell);

                removeWalls(currentCell, nextCell);

                currentCell = nextCell;
                currentCell.markAsVisited();
            } else {
                Optional<Node<MazeCell>> parent = currentCell.getParent();
                if (parent.isPresent()) {
                    CellNode parentCell = (CellNode) parent.get();
                    currentCell = maze.getCellNodeByCoordinates(parentCell.getColumn(), parentCell.getRow());
                }
            }

            isMazeReady = currentCell.isRoot() && unvisitedNeighbours.isEmpty();
        } while (!isMazeReady);

        return maze;
    }
}
