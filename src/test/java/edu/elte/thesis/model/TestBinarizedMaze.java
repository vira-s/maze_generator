package edu.elte.thesis.model;

import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;
import edu.elte.thesis.utils.DummyFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestBinarizedMaze {
    private static final Logger LOGGER = LogManager.getLogger(TestBinarizedMaze.class);

    private BinarizedMaze binarizedMaze;

    private Maze acyclicMaze;
    private Maze cyclicMaze;
    private Maze disconnectedMaze;

    @Before
    public void setUp() {
        binarizedMaze = new BinarizedMaze();
        acyclicMaze = new Maze(2, 2, DummyFactory.getAcyclicNodes());
        cyclicMaze = new Maze(2, 2, DummyFactory.getCyclicNodes());
        disconnectedMaze = new Maze(2, 2, DummyFactory.getDisconnectedNodes());
    }

    @Test
    public void testCreateGraphFromBinarizedMaze_acyclicMaze() {
        Maze result = binarizedMaze.createGraphFromBinarizedMaze(DummyFactory.ACYCLIC_MAZE);
        assertEqualsMazesParentInsensitive(acyclicMaze, result);
    }

    @Test
    public void testCreateGraphFromBinarizedMaze_cyclicMaze() {
        Maze result = binarizedMaze.createGraphFromBinarizedMaze(DummyFactory.CYCLIC_MAZE);
        assertEqualsMazesParentInsensitive(cyclicMaze, result);
    }

    @Ignore
    @Test
    public void testCreateGraphFromBinarizedMaze_disconnectedMaze() {
        Maze result = binarizedMaze.createGraphFromBinarizedMaze(DummyFactory.DISCONNECTED_MAZE);
        assertEqualsMazesParentInsensitive(disconnectedMaze, result);
    }

    private void assertEqualsMazesParentInsensitive(Maze expected, Maze result) {
        Assert.assertNotNull(result);

        Assert.assertEquals("Rows mismatch. "
                        + "Expected: " + expected.getRows()
                        + " Actual: " + result.getRows(),
                expected.getRows(), result.getRows());

        Assert.assertEquals("Columns mismatch. "
                        + "Expected: " + expected.getColumns()
                        + " Actual: " + result.getColumns(),
                expected.getColumns(), result.getColumns());

        Assert.assertEquals("Maze node size mismatch. "
                        + "Expected: " + expected.getNodes().size()
                        + " Actual: " + result.getNodes().size(),
                expected.getNodes().size(), result.getNodes().size());

        assertEqualsChildrenCoordinates(expected, result);
    }

    private void assertEqualsChildrenCoordinates(Maze expected, Maze result) {
        for (int row = 0; row < expected.getRows(); row++) {
            for (int column = 0; column < expected.getColumns(); column++) {
                CellNode expectedCell = expected.getCellNodeByCoordinates(column, row);
                CellNode resultCell = result.getCellNodeByCoordinates(column, row);
                Assert.assertEquals("Root mismatch at (" + column + "," + row + ").",
                        expectedCell.isRoot(), resultCell.isRoot());

                assertEqualsCellWalls(expectedCell, resultCell, column, row);

                List<String> expectedChildren = expectedCell.getChildren().stream()
                        .map(child -> ((CellNode)child).getColumn() + "," + ((CellNode)child).getRow())
                        .collect(Collectors.toList());
                List<String> resultChildren = resultCell.getChildren().stream()
                        .map(child -> ((CellNode)child).getColumn() + "," + ((CellNode)child).getRow())
                        .collect(Collectors.toList());

                Assert.assertEquals("Children coordinate mismatch at (" + column + "," + row + ").",
                        expectedChildren, resultChildren);
            }
        }
    }

    private void assertEqualsCellWalls(CellNode expectedCell, CellNode resultCell, int column, int row) {
        Assert.assertEquals("Wall mismatch at (" + column + "," + row + ").",
                expectedCell.getWallByPosition(WallPosition.NORTH), resultCell.getWallByPosition(WallPosition.NORTH));

        Assert.assertEquals("Wall mismatch at (" + column + "," + row + ").",
                expectedCell.getWallByPosition(WallPosition.EAST), resultCell.getWallByPosition(WallPosition.EAST));

        Assert.assertEquals("Wall mismatch at (" + column + "," + row + ").",
                expectedCell.getWallByPosition(WallPosition.SOUTH), resultCell.getWallByPosition(WallPosition.SOUTH));

        Assert.assertEquals("Wall mismatch at (" + column + "," + row + ").",
                expectedCell.getWallByPosition(WallPosition.WEST), resultCell.getWallByPosition(WallPosition.WEST));
    }

}
