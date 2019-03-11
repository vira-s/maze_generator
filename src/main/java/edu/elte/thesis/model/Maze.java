package edu.elte.thesis.model;


import edu.elte.thesis.exception.MultipleRootsException;
import edu.elte.thesis.model.cell.MazeCell;
import edu.elte.thesis.model.graph.CellNode;
import edu.elte.thesis.model.graph.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents a maze object.
 *
 * @author Viktoria Sinkovics
 */
public class Maze {

    private static final Logger LOGGER = LogManager.getLogger(Maze.class);

    private static final int DEFAULT_START_COLUMN = 0;

    private static final int DEFAULT_START_ROW = 0;

    private final int columns;

    private final int rows;

    private List<CellNode> nodes;

    public Maze(int columns, int rows) {
        Assert.isTrue(columns > 0, "Columns must be positive.");
        Assert.isTrue(rows > 0, "Rows must be positive.");

        this.columns = columns;
        this.rows = rows;
        this.nodes = initializeNodes(columns, rows);
    }

    public Maze(int rows, int columns, List<CellNode> nodes) {
        Assert.isTrue(columns > 0, "Columns must be positive.");
        Assert.isTrue(rows > 0, "Rows must be positive.");
        Assert.notEmpty(nodes, "Grid must not be empty.");
        Assert.isTrue(nodes.size() == columns * rows,
                "Unexpected nodes size (" + nodes.size() + "), should be (" + columns * rows + ").");

        this.rows = rows;
        this.columns = columns;
        this.nodes = nodes;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public List<CellNode> getNodes() {
        return nodes;
    }

    /**
     * Retrieves a {@link MazeCell} by the given coordinates.
     *
     * @param column The columns of the {@link MazeCell}
     * @param row    The rows of the {@link MazeCell}
     *
     * @return The {@link MazeCell} with the matching position
     */
    public CellNode getCellNodeByCoordinates(int column, int row) {
        Assert.isTrue(column >= 0, "Column must be non-negative.");
        Assert.isTrue(this.columns > column, "Column must be smaller than " + this.columns + ".");
        Assert.isTrue(row >= 0, "Row must be non-negative.");
        Assert.isTrue(this.rows > row, "Row must be smaller than " + this.rows + ".");

        List<CellNode> cellsAtCoordinate = nodes.stream()
                .filter(cellNode -> cellNode.getColumn() == column && cellNode.getRow() == row)
                .collect(Collectors.toList());

        Assert.isTrue(cellsAtCoordinate.size() == 1, "Exactly one cell must be found: " + cellsAtCoordinate);

        return cellsAtCoordinate.get(0);
    }

    /**
     * Retrieves the neighbours of the provided {@link MazeCell}.
     *
     * @param mazeCell The {@link MazeCell}
     *
     * @return The neighbours of the provided {@link MazeCell}
     */
    public List<CellNode> findNeighboursOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        return nodes.stream()
                .filter(cellNode -> cellNode.isNeighbourOf(mazeCell))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the given {@link MazeCell}'s unvisited neighbours.
     *
     * @param mazeCell The {@link MazeCell}
     *
     * @return The unvisited neighbours or an empty {@link List}
     */
    public List<CellNode> findUnvisitedNeighboursOf(MazeCell mazeCell) {
        Assert.notNull(mazeCell, "mazeCell should not be null.");

        LOGGER.info("Looking for unvisited neighbours of ({},{})", mazeCell.getColumn(), mazeCell.getRow());

        return findNeighboursOf(mazeCell).stream()
                .filter(cellNode -> !cellNode.isVisited())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the starting point/root of the maze.
     *
     * @return The root
     */
    public CellNode findRoot() throws MultipleRootsException {
        List<CellNode> rootCells = this.nodes.stream()
                .filter(Node::isRoot)
                .collect(Collectors.toList());

        if (rootCells.size() != 1) {
            throw new MultipleRootsException("Exactly one root cell is required/allowed. " + rootCells);
        }

        return rootCells.get(0);
    }

    /**
     * Selects a start point for the {@link edu.elte.thesis.graph.generator.MazeGenerator}.
     *
     * @param random If the start point should be selected at random, or use the default (0,0) point
     *
     * @return The {@link CellNode} to start from
     */
    public CellNode selectStartPoint(boolean random) {
        CellNode startCellNode;

        if (random) {
            Random randomNumber = new SecureRandom();

            int randomColumn = randomNumber.nextInt(columns);
            int randomRow = randomNumber.nextInt(rows);

            startCellNode = getCellNodeByCoordinates(randomColumn, randomRow);
        } else {
            startCellNode = getCellNodeByCoordinates(DEFAULT_START_COLUMN, DEFAULT_START_ROW);
        }

        return startCellNode;
    }

    /**
     * @return True if each {@link CellNode} is visited, false otherwise.
     */
    public boolean isComplete() {
        return this.nodes.stream()
                .allMatch(CellNode::isVisited);
    }

    /**
     * Prints the maze to the console in a vertical graph format.
     */
    public void printMazeGraph() throws MultipleRootsException {
        this.findRoot().print("", true);
    }

    private List<CellNode> initializeNodes(int columns, int rows) {
        List<CellNode> nodes = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                MazeCell cell = new MazeCell(column, row);
                CellNode cellNode = new CellNode(cell);
                nodes.add(cellNode);
            }
        }

        return nodes;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Maze maze = (Maze) other;
        return rows == maze.rows
                && columns == maze.columns
                && Objects.equals(nodes, maze.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rows, columns, nodes);
    }

    @Override
    public String toString() {
        return "Maze{"
                + "rows=" + rows
                + ", columns=" + columns
                + ", nodes=" + nodes
                + '}';
    }
}
