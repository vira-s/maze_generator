package model.generator;

import model.Maze;
import model.cell.MazeCell;
import model.cell.WallPosition;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

/**
 * Maze generator that implements the recursive backtracker algorithm.
 *
 * @author Viktoria Sinkovics on 10/3/2018
 */
public class RecursiveBacktrackerGenerator extends MazeGenerator {

    private static final Logger LOGGER = LogManager.getLogger(RecursiveBacktrackerGenerator.class);

    private Stack<MazeCell> cells;

    public RecursiveBacktrackerGenerator() {
        cells = new Stack<>();
    }

    @Override
    public Maze generate(int columns, int rows) {

        Maze maze = new Maze(columns, rows);

        MazeCell currentCell = maze.getStartPoint(false);
        MazeCell nextCell;
        Random randomNeighbourIndex = new Random();
        do {
            List<MazeCell> unvisitedNeighbours = maze.findUnvisitedNeighboursOf(currentCell);
            Optional<MazeCell> anyUnvisitedNeighbour = unvisitedNeighbours.isEmpty()
                    ? Optional.empty()
                    : Optional.ofNullable(unvisitedNeighbours.get(randomNeighbourIndex.nextInt(unvisitedNeighbours.size())));

            currentCell.markAsVisited();
            if (anyUnvisitedNeighbour.isPresent()) {
                nextCell = anyUnvisitedNeighbour.get();

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

        LOGGER.info(generateMazeText(maze));

        return maze;
    }

}
