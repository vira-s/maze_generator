package model;

import model.cell.MazeCell;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Viktoria Sinkovics on 10/1/2018
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
        MazeCell result = maze.getCellByCoordinates(COLUMN - 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCellByCoordinates_negativeRow() {
        MazeCell result = maze.getCellByCoordinates(-1, ROW - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCellByCoordinates_biggerColumnPositionThanAllowed() {
        MazeCell result = maze.getCellByCoordinates(COLUMN + 1, ROW - 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCellByCoordinates_biggerRowPositionThanAllowed() {
        MazeCell result = maze.getCellByCoordinates(COLUMN - 1, ROW + 1);
    }

    @Test
    public void testGetCellByCoordinates() {
        MazeCell expected = new MazeCell(COLUMN - 1, ROW - 1);
        MazeCell result = maze.getCellByCoordinates(COLUMN - 1, ROW - 1);

        Assert.assertNotNull("Result must not be null.", result);
        Assert.assertEquals(expected, result);
    }

}
