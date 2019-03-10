package edu.elte.thesis.model;

import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
@XmlRootElement(name = "maze")
@XmlAccessorType(XmlAccessType.FIELD)
public class BinarizedMaze {

    private static final Logger LOGGER = LogManager.getLogger(BinarizedMaze.class);

    private static final Character WALL = '1';

    private static final String WALL_WALL = "11";

    private static final String GAP_WALL = "01";

    private static final String GAP_GAP = "00";

    @XmlElement(name="rows")
    private List<String> binarizedMaze;

    public BinarizedMaze() {
        // Default NOOP constructor for JSON (un)marshaller
    }

    /**
     * Creates a binarized representation of a {@link Maze).
     *
     * Walls and openings are defined as '1' and '0' respectively.
     * The maze's first row is a fixed wall, also each row has a wall as a prefix.
     * Only {@link WallPosition#EAST} and {@link WallPosition#SOUTH} are checked to avoid "double-walls" between cells.
     * Cells are represented as follows, where 'x' can either be a '1' or a '0':
     *  0x
     *  x1
     *
     * Example binarizedMaze:
     *      11111111
     *      11111111
     *      10100011
     *      10111011
     *      10001011
     *      10101011
     *      10100011
     *      11111111
     * Note: The double walls on the sides are necessary for the correct functioning of the neural network,
     * which requires the width and height to be dividable by 4.
     *
     * @param maze The maze
     *
     * @return The representation of the maze
     */
    public BinarizedMaze binarizeMaze(Maze maze) {
        Assert.notNull(maze, "binarizedMaze should not be null.");
        this.binarizedMaze = new ArrayList<>();

        int rows = maze.getRows();
        int columns = maze.getColumns();

        int headerElementCount = 1 + 2 * rows;
        List<Integer> extraElementsToTopRightBottomLeftHeader = calculateExtraElements(headerElementCount);

        this.binarizedMaze.add(StringUtils.repeat(WALL, extraElementsToTopRightBottomLeftHeader.get(4)));
        if (extraElementsToTopRightBottomLeftHeader.get(0) > 0) {
            for (int i = 0; i < extraElementsToTopRightBottomLeftHeader.get(0); ++i) {
                this.binarizedMaze.add(StringUtils.repeat(WALL, extraElementsToTopRightBottomLeftHeader.get(4)));
            }
        }

        for (int row = 0; row < rows; row++) {
            this.binarizedMaze.add(generateRowsTopPart(maze, columns, row, extraElementsToTopRightBottomLeftHeader.get(3), extraElementsToTopRightBottomLeftHeader.get(1)));
            this.binarizedMaze.add(generateRowsBottomPart(maze, columns, row, extraElementsToTopRightBottomLeftHeader.get(3), extraElementsToTopRightBottomLeftHeader.get(1)));
        }

        if (extraElementsToTopRightBottomLeftHeader.get(2)  > 0) {
            for (int i = 0; i < extraElementsToTopRightBottomLeftHeader.get(2); ++i) {
                this.binarizedMaze.add(StringUtils.repeat(WALL, extraElementsToTopRightBottomLeftHeader.get(4)));
            }
        }

        return this;
    }

    /**
     * Creates a {@link Maze} graph from the provided binary representation.
     * Since it isn't ensured that the mazes need to be acyclic and/or connected, 
     * the cell's parent field won't be populated.
     *
     * TODO Refactor BinarizedMaze to handle disconnected mazes.
     *  So far if any part of the maze is not reachable from (0,0), it will be left untouched
     *  and the connections between any disconnected cell (from the 'main' graph) won't be saved.
     *
     * Note: The shape of the maze can be validated with the {@link edu.elte.thesis.graph.utils.DepthFirstSearchRunner}.
     *
     * @param binaryMaze The binary representation of a maze
     *
     * @return The graph representation of a maze
     */
    public Maze createGraphFromBinarizedMaze(List<String> binaryMaze) {
        Assert.notNull(binaryMaze, "binaryMaze should not be null.");
        Assert.isTrue(!binaryMaze.isEmpty(), "binaryMaze should not be empty.");

        binarizedMaze = extractCoreBinarizedMaze(binaryMaze);
        LOGGER.debug("Extracted core binary maze: {}", binarizedMaze);

        int columns = (binarizedMaze.size() - 1) / 2;
        int rows = (binarizedMaze.get(0).length() - 1) / 2;

        Maze maze = createMazeTemplate(columns, rows);
        setUpGraph(maze);
        return maze;
    }

    /**
     * Utility method for binarizing the maze.
     * Creates the string representation of a row's upper part based on the visibility of the {@link WallPosition#EAST} wall of the cells.
     * 
     * @param maze The maze
     * @param columns The number of columns of the maze
     * @param row The current row
     * @param extraElemToLeft Number of extra 'walls' needed at the beginning of the string
     * @param extraElemToRight Number of extra 'walls' needed at the end of the string
     * 
     * @return The string representation of a row's upper part 
     */
    private String generateRowsTopPart(Maze maze, int columns, int row, int extraElemToLeft, int extraElemToRight) {
        StringBuilder rowBuilder = new StringBuilder();

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);

            if (col == 0) {
                generateFirstColumn(rowBuilder, extraElemToLeft);
            }

            if (current.getWallByPosition(WallPosition.EAST).isVisible()) {
                rowBuilder.append(GAP_WALL);
            } else {
                rowBuilder.append(GAP_GAP);
            }

            if (col == columns - 1 && extraElemToRight > 0) {
                rowBuilder.append(StringUtils.repeat(WALL, extraElemToRight));

            }
        }

        return rowBuilder.toString();
    }

    /**
     * Utility method for binarizing the maze.
     * Creates the string representation of a row's lower part based on the visibility of the {@link WallPosition#SOUTH} wall of the cells.
     * 
     * @param maze The maze
     * @param columns The number of columns of the maze
     * @param row The current row
     * @param extraElemToLeft Number of extra 'walls' needed at the beginning of the string
     * @param extraElemToRight Number of extra 'walls' needed at the end of the string
     * 
     * @return The string representation of a row's upper part 
     * 
     */
    private String generateRowsBottomPart(Maze maze, int columns, int row, int extraElemToLeft, int extraElemToRight) {
        StringBuilder rowBuilder = new StringBuilder();

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);

            if (col == 0) {
                generateFirstColumn(rowBuilder, extraElemToLeft);
            }

            if (current.getWallByPosition(WallPosition.SOUTH).isVisible()) {
                rowBuilder.append(WALL_WALL);
            } else {
                rowBuilder.append(GAP_WALL);
            }

            if (col == columns - 1 && extraElemToRight > 0) {
                rowBuilder.append(StringUtils.repeat(WALL, extraElemToRight));

            }
        }

        return rowBuilder.toString();
    }

    /**
     * Utility method for binarizing the maze.
     * Adds the leftmost columns(s) of wall to the string representation of the maze.
     * 
     * @param rowBuilder The string builder
     * @param extraElemToLeft Number of extra 'walls' needed at the beginning of the string
     */
    private void generateFirstColumn(StringBuilder rowBuilder, int extraElemToLeft) {
        rowBuilder.append(WALL);
        if (extraElemToLeft > 0) {
            rowBuilder.append(StringUtils.repeat(WALL, extraElemToLeft));
        }
    }

    /**
     * Utility method for binarizing the maze.
     * Calculates how many extra wall(s) are needed to the four side of the maze.
     * The result is based on the 'core' (binary) maze's size and if it's divisible by 4.
     * Note: this is required for the correct functioning of the neural network.
     *
     * @param headerElementCount The number of elements in the topmost row of the maze
     *
     * @return The number of extra elements needed to the four sides of the maze and to its header
     */
    private List<Integer> calculateExtraElements(int headerElementCount) {
        List<Integer> extraElementsToTopRightBottomLeftHeader = new ArrayList<>();

        if (headerElementCount % 4 == 3) {
            extraElementsToTopRightBottomLeftHeader.addAll(Arrays.asList(0, 1, 0, 0, headerElementCount + 1));
        } else if (headerElementCount % 4 == 2) {
            extraElementsToTopRightBottomLeftHeader.addAll(Arrays.asList(1, 1, 1, 1, headerElementCount + 1));
        } else if (headerElementCount % 4 == 1) {
            extraElementsToTopRightBottomLeftHeader.addAll(Arrays.asList(2, 2, 1, 1, headerElementCount + 3));
        } else {
            extraElementsToTopRightBottomLeftHeader.addAll(Arrays.asList(0, 0, 0, 0, headerElementCount));
        }

        return extraElementsToTopRightBottomLeftHeader;
    }

    /**
     * Utility method to reverse a binary maze into a graph.
     * Creates the connections between the maze's cells based on the {@link this#binarizedMaze}.
     *
     * @param maze The maze
     */
    private void setUpGraph(Maze maze) {
        List<CellNode> unfinishedCells = new ArrayList<>();
        CellNode currentCell = maze.getCellNodeByCoordinates(0, 0);
        currentCell.makeRoot();
        unfinishedCells.add(currentCell);

        int columns = maze.getColumns();
        int rows = maze.getRows();

        int column;
        int row;

        while (!unfinishedCells.isEmpty()) {
            currentCell = unfinishedCells.remove(0);
            column = currentCell.getColumn();
            row = currentCell.getRow();

            if (!currentCell.getWallByPosition(WallPosition.EAST).isVisible() && column + 1 < columns) {
                handleNextCell(currentCell, maze.getCellNodeByCoordinates(column + 1, row), unfinishedCells);
            }

            if (!currentCell.getWallByPosition(WallPosition.SOUTH).isVisible() && row + 1 < rows) {
                handleNextCell(currentCell, maze.getCellNodeByCoordinates(column, row + 1), unfinishedCells);
            }

            if (!currentCell.getWallByPosition(WallPosition.WEST).isVisible() && column - 1 >= 0) {
                handleNextCell(currentCell, maze.getCellNodeByCoordinates(column - 1, row), unfinishedCells);
            }

            if (!currentCell.getWallByPosition(WallPosition.NORTH).isVisible() && row - 1 >= 0) {
                handleNextCell(currentCell, maze.getCellNodeByCoordinates(column, row - 1), unfinishedCells);
            }
        }
    }

    /**
     * Utility method to reverse a binary maze into a graph.
     * Creates the connections between two neighbour cells.
     *
     * @param currentCell The current cell
     * @param nextCell The neighbour cell
     * @param unfinishedCells The unfinished cells which have unhandled openings
     */
    private void handleNextCell(CellNode currentCell, CellNode nextCell, List<CellNode> unfinishedCells) {
        if (!currentCell.getChildren().contains(nextCell)
                && !nextCell.getChildren().contains(currentCell)) {
            // nextCell.setParent(currentCell);
            currentCell.addChild(nextCell);
            unfinishedCells.add(nextCell);
        }
    }

    /**
     * Utility method to reverse a binary maze into a graph.
     * Creates a template maze with the specified width and height. The cells have no connection,
     * but their walls are removed based on the {@link this#binarizedMaze}.
     *
     * @param columns The number of columns
     * @param rows The number of rows
     *
     * @return A maze with handled walls and unconnected cells
     */
    private Maze createMazeTemplate(int columns, int rows) {
        Maze maze = new Maze(columns, rows);

        int binaryRow;
        int binaryColumn;
        for (int row = 0; row < rows; row++) {
            binaryRow = row * 2 + 1;
            binaryColumn = 2;
            for (int column = 0; column < columns; column++) {

                if (binaryRow % 2 == 1 && binaryColumn % 2 == 0
                        && Objects.equals(binarizedMaze.get(binaryRow).charAt(binaryColumn), '0')) {
                    maze.getCellNodeByCoordinates(column, row).removeWall(WallPosition.EAST);
                    if (column + 1 < columns) {
                        maze.getCellNodeByCoordinates(column + 1, row).removeWall(WallPosition.WEST);
                    }
                }

                binaryColumn -= 1;
                binaryRow += 1;

                if (binaryRow % 2 == 0 && binaryColumn % 2 == 1
                        && Objects.equals(binarizedMaze.get(binaryRow).charAt(binaryColumn), '0')) {
                    maze.getCellNodeByCoordinates(column, row).removeWall(WallPosition.SOUTH);
                    if (row + 1 < rows) {
                        maze.getCellNodeByCoordinates(column, row + 1).removeWall(WallPosition.NORTH);
                    }
                }

                binaryRow -= 1;
                binaryColumn+=3;

            }
        }

        return maze;
    }

    /**
     * Utility method to reverse a binary maze into a graph.
     * Extracts the 'core' of the binarized maze which is the state of the binary maze before the extra columns and rows were appended
     * in order to have width/height divisible by 4.
     *
     * @param binaryMaze The binary maze with possible extra rows and/or columns
     *
     * @return The binary maze in its original state
     */
    private List<String> extractCoreBinarizedMaze(List<String> binaryMaze) {
        if (Objects.equals(binaryMaze.get(0), binaryMaze.get(1))
                && Objects.equals(binaryMaze.get(0), binaryMaze.get(2))) {
            return binaryMaze.subList(2, binaryMaze.size() - 1)
                    .stream()
                    .map(row -> row.substring(1, row.length() - 1))
                    .collect(Collectors.toList());

        } else if (Objects.equals(binaryMaze.get(0), binaryMaze.get(1))
                && Objects.equals(binaryMaze.get(binaryMaze.size() - 1), binaryMaze.get(binaryMaze.size() - 2))) {
            return binaryMaze.subList(1, binaryMaze.size() - 1)
                    .stream()
                    .map(row -> row.substring(1, row.length() - 1))
                    .collect(Collectors.toList());

        } else if (Objects.equals(binaryMaze.get(0), binaryMaze.get(1))) {
            return binaryMaze.subList(1, binaryMaze.size())
                    .stream()
                    .map(row -> row.substring(0, row.length() - 1))
                    .collect(Collectors.toList());
        }

        return binaryMaze;
    }
}
