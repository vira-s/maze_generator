package model;

import model.cell.MazeCell;
import model.graph.CellNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import utils.DummyFactory;

import java.util.Collections;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestCellNode {

    private static final MazeCell MAZE_CELL = new MazeCell(DummyFactory.DEFAULT_COLUMNS, DummyFactory.DEFAULT_ROWS);
    
    private CellNode cellNodeUT;

    @Before
    public void setUp() throws Exception {
        cellNodeUT = new CellNode(MAZE_CELL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCellNode_singleArgConstructor_nullEntity() {
        CellNode result = new CellNode(null);
    }

    @Test
    public void testCellNode_singleArgConstructor() {
        CellNode result = new CellNode(MAZE_CELL);

        Assert.assertEquals(cellNodeUT, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCellNode_twoArgConstructor_nullEntity() {
        CellNode result = new CellNode(
                null,
                null);
    }

    @Test
    public void testCellNode_twoArgConstructor() {
        CellNode result = new CellNode(
                null,
                MAZE_CELL);

        Assert.assertEquals(cellNodeUT, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCellNode_threeArgConstructor_nullEntity() {
        CellNode result = new CellNode(
                null,
                Collections.emptyList(),
                null);}

    @Test
    public void testCellNode_threeArgConstructor() {
        CellNode result = new CellNode(
                null,
                Collections.emptyList(),
                MAZE_CELL);

        Assert.assertEquals(cellNodeUT, result);
    }

    @Test
    public void testMarkAsVisited() {
        cellNodeUT.markAsVisited();

        Assert.assertTrue("Entity should be visited", cellNodeUT.getEntity().isVisited());
    }

    @Test
    public void testIsNeighbourOf() {
        boolean result = cellNodeUT.isNeighbourOf(new MazeCell(DummyFactory.DEFAULT_COLUMNS, DummyFactory.DEFAULT_ROWS + 1));

        Assert.assertTrue("Entity should be neighbour", result);
    }

    @Test
    public void testIsUpperNeighbourOf() {
        CellNode cellNode = new CellNode(new MazeCell(DummyFactory.DEFAULT_COLUMNS, DummyFactory.DEFAULT_ROWS + 1));
        boolean result = cellNodeUT.isUpperNeighbourOf(cellNode);

        Assert.assertTrue("Entity should be upper neighbour", result);
    }

    @Test
    public void testIsUpperNeighbourOfItself() {
        boolean result = cellNodeUT.isUpperNeighbourOf(new CellNode(MAZE_CELL));

        Assert.assertFalse("Entity should not be upper neighbour", result);
    }

    @Test
    public void testIsLowerNeighbourOf() {
        CellNode cellNode = new CellNode(new MazeCell(DummyFactory.DEFAULT_COLUMNS, DummyFactory.DEFAULT_ROWS - 1));
        boolean result = cellNodeUT.isLowerNeighbourOf(cellNode);

        Assert.assertTrue("Entity should be lower neighbour", result);

    }

    @Test
    public void testIsLowerNeighbourOfItself() {
        boolean result = cellNodeUT.isLowerNeighbourOf(new CellNode(MAZE_CELL));

        Assert.assertFalse("Entity should not be lower neighbour", result);
    }

    @Test
    public void testIsLeftNeighbourOf() {
        CellNode cellNode = new CellNode(new MazeCell(DummyFactory.DEFAULT_COLUMNS + 1, DummyFactory.DEFAULT_ROWS));
        boolean result = cellNodeUT.isLeftNeighbourOf(cellNode);

        Assert.assertTrue("Entity should be left neighbour", result);

    }

    @Test
    public void testIsLeftNeighbourOfItself() {
        boolean result = cellNodeUT.isLeftNeighbourOf(new CellNode(MAZE_CELL));

        Assert.assertFalse("Entity should not be left neighbour", result);
    }

    @Test
    public void testIsRightNeighbourOf() {
        CellNode cellNode = new CellNode(new MazeCell(DummyFactory.DEFAULT_COLUMNS - 1, DummyFactory.DEFAULT_ROWS));
        boolean result = cellNodeUT.isRightNeighbourOf(cellNode);

        Assert.assertTrue("Entity should be right neighbour", result);

    }

    @Test
    public void testIsRightNeighbourOfItself() {
        boolean result = cellNodeUT.isRightNeighbourOf(new CellNode(MAZE_CELL));

        Assert.assertFalse("Entity should not be right neighbour", result);
    }

}
