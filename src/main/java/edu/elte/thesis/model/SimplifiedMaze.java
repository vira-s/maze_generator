package edu.elte.thesis.model;

import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktoria Sinkovics
 */
@XmlRootElement(name = "maze")
@XmlAccessorType(XmlAccessType.FIELD)
public class SimplifiedMaze {

    @XmlElement(name="rows")
    private List<String> simplifiedMaze = new ArrayList<>();

    public SimplifiedMaze() {
        // Default NOOP constructor for JSON (un)marshaller
    }

    public SimplifiedMaze(List<String> simplifiedMaze) {
        this.simplifiedMaze = simplifiedMaze;
    }

    /**
     * Creates a simplified representation of a simplifiedMaze.
     *
     * Walls and openings are defined as '1' and '0' respectively.
     * The simplifiedMaze's first row is a fixed wall, also each row has a wall as a prefix.
     * Only {@link WallPosition#EAST} and {@link WallPosition#SOUTH} are checked to avoid "double-walls" between cells.
     * Cells are represented as follows, where 'x' can either be a '1' or a '0':
     *  0x
     *  x1
     *
     * Example simplifiedMaze:
     *      1111111
     *      1010001
     *      1011101
     *      1000101
     *      1010101
     *      1010001
     *      1111111
     *
     * @param maze The maze
     * @return The representation of the maze
     */
    public SimplifiedMaze createSimplifiedMaze(Maze maze) {
        Assert.notNull(maze, "simplifiedMaze should not be null.");

        int rows = maze.getRows();
        int columns = maze.getColumns();

        int headerElementCount = 1 + 2 * rows;
        this.simplifiedMaze.add(StringUtils.repeat('1', headerElementCount));

        for (int row = 0; row < rows; row++) {
            this.simplifiedMaze.add(generateRowsTopPart(maze, columns, row));
            this.simplifiedMaze.add(generateRowsBottomPart(maze, columns, row));
        }

        return this;
    }

    private String generateRowsTopPart(Maze maze, int columns, int row) {
        StringBuilder rowBuilder = new StringBuilder();

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);
            if (col == 0) {
                rowBuilder.append('1');
            }

            if (current.getWallByPosition(WallPosition.EAST).isVisible()) {
                rowBuilder.append("01");
            } else {
                rowBuilder.append("00");
            }
        }

        return rowBuilder.toString();
    }

    private String generateRowsBottomPart(Maze maze, int columns, int row) {
        StringBuilder rowBuilder = new StringBuilder();

        for (int col = 0; col < columns; col++) {
            CellNode current = maze.getCellNodeByCoordinates(col, row);
            if (col == 0) {
                rowBuilder.append('1');
            }

            if (current.getWallByPosition(WallPosition.SOUTH).isVisible()) {
                rowBuilder.append("11");
            } else {
                rowBuilder.append("01");
            }
        }

        return rowBuilder.toString();
    }
}
