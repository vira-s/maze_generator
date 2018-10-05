package model;

import model.cell.MazeCell;
import model.cell.Wall;
import model.cell.WallPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Viktoria Sinkovics on 10/1/2018
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestMazeCell {

    private static final int COLUMN = 2;

    private static final int ROW = 6;

    private MazeCell cell;

    @Before
    public void setUp() {
        cell = new MazeCell(COLUMN, ROW);
    }

    @Test
    public void testRemoveWall() {
        Assert.assertTrue("All walls must be visible " + cell.getWalls(), cell.getWalls().stream().allMatch(Wall::isVisible));

        cell.removeWall(WallPosition.SOUTH);

        Assert.assertEquals("Three walls must be visible " + cell.getWalls(), 3, cell.getWalls().stream().filter(Wall::isVisible).count());
        Assert.assertFalse("Wall must be invisible " + cell.getWallByPosition(WallPosition.SOUTH), cell.getWallByPosition(WallPosition.SOUTH).isVisible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWall_nullPosition() {
        Assert.assertTrue("All walls must be visible " + cell.getWalls(), cell.getWalls().stream().allMatch(Wall::isVisible));

        cell.removeWall(null);
    }

    @Test
    public void testMarkAsVisited() {
        Assert.assertFalse("Cell must not be visited " + cell, cell.isVisited());

        cell.markAsVisited();

        Assert.assertTrue("Cell must be visited " + cell, cell.isVisited());
    }
}
