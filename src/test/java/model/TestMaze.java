package model;

import model.cell.MazeCell;
import model.graph.CellNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestMaze {

    private static final int COLUMN = 3;

    private static final int ROW = 4;

    private Maze maze;

    @Before
    public void setUp() {
        maze = new Maze(COLUMN, ROW);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCellByCoordinates_negativeColumn() {
        CellNode result = maze.getCellNodeByCoordinates(COLUMN - 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCellByCoordinates_negativeRow() {
        CellNode result = maze.getCellNodeByCoordinates(-1, ROW - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCellByCoordinates_biggerColumnPositionThanAllowed() {
        CellNode result = maze.getCellNodeByCoordinates(COLUMN + 1, ROW - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCellByCoordinates_biggerRowPositionThanAllowed() {
        CellNode result = maze.getCellNodeByCoordinates(COLUMN - 1, ROW + 1);
    }

    @Test
    public void testGetCellByCoordinates() {
        CellNode expected = new CellNode(new MazeCell(COLUMN - 1, ROW - 1));
        CellNode result = maze.getCellNodeByCoordinates(COLUMN - 1, ROW - 1);

        Assert.assertNotNull("Result must not be null.", result);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSelectStartPoint_random() {
        CellNode result = maze.selectStartPoint(true);

        Assert.assertNotNull("Result must not be null.", result);
    }

    @Test
    public void testSelectStartPoint_notRandom() {
        CellNode expected = new CellNode(new MazeCell(0, 0));
        CellNode result = maze.selectStartPoint(false);

        Assert.assertNotNull("Result must not be null.", result);
        Assert.assertEquals(expected, result);
    }

}
