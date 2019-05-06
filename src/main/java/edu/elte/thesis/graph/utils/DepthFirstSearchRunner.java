package edu.elte.thesis.graph.utils;

import edu.elte.thesis.exception.MultipleRootsException;
import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.graph.CellNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * A modified implementation of the DFS algorithm to detect cycles and verify connectivity.
 *
 * @author Viktoria Sinkovics
 */
public class DepthFirstSearchRunner {

    private static final Logger LOGGER = LogManager.getLogger(DepthFirstSearchRunner.class);

    /**
     * Runs a modified depth-first-search on the provided {@link Maze}.
     *
     * @param maze The maze
     *
     * @return True if the maze is acyclic and connected, false otherwise.
     */
    public boolean run(Maze maze) {
        Assert.notNull(maze, "maze should not be null.");
        LOGGER.info("Validating maze");

        List<CellNode> visitedCells = new ArrayList<>();
        Stack<CellNode> stack = new Stack<>();

        CellNode root;
        try {
            root = maze.findRoot();
        } catch (MultipleRootsException exception) {
            LOGGER.error(exception.getMessage());
            return false;
        }

        stack.push(root);

        while (!stack.isEmpty()) {
            CellNode currentCell = stack.pop();
            if (visitedCells.contains(currentCell)) {
                LOGGER.warn("Found a cycle in the maze.");
                return false;
            }

            visitedCells.add(currentCell);
            currentCell.getChildren().forEach(child -> stack.push((CellNode) child));
        }

        LOGGER.info("No cycles were found in the maze.");

        if (maze.getNodes().size() != visitedCells.size()) {
            LOGGER.warn("Some of the cells are disconnected from the 'main' graph {}", maze);
            return false;
        }

        LOGGER.info("The maze is connected.");

        // TODO only use in debug mode
        // maze.printMazeGraph();
        return true;
    }

    public static Set<CellNode> collectAllNodesForRoot(CellNode root) {
        Set<CellNode> visitedCells = new HashSet<>();
        Stack<CellNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            CellNode currentCell = stack.pop();
            if (!visitedCells.contains(currentCell)) {
                visitedCells.add(currentCell);
                currentCell.getChildren().forEach(child -> stack.push((CellNode) child));
            }
        }

        return visitedCells;
    }


}
