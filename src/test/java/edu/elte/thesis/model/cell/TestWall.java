package edu.elte.thesis.model.cell;

import edu.elte.thesis.model.cell.Wall;
import edu.elte.thesis.model.cell.WallPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestWall {

    private static final WallPosition WALL_POSITION = WallPosition.NORTH;

    private Wall wall;

    @Before
    public void setUp() {
        wall = new Wall(WALL_POSITION);
    }

    @Test
    public void testSetInvisible() {
        Assert.assertTrue("Wall must be visible. " + wall, wall.isVisible());

        wall.setInvisible();

        Assert.assertFalse("Wall must not be visible. " + wall, wall.isVisible());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInvisible_alreadyInvisible() {
        Assert.assertTrue("Wall must be visible. " + wall, wall.isVisible());

        wall.setInvisible();
        Assert.assertFalse("Wall must not be visible. " + wall, wall.isVisible());

        wall.setInvisible();
    }

}
