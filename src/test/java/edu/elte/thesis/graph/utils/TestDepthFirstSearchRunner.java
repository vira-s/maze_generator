package edu.elte.thesis.graph.utils;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.utils.DummyFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDepthFirstSearchRunner {

    private DepthFirstSearchRunner depthFirstSearchRunnerUT;

    private Maze cyclicMaze;

    private Maze acyclicMaze;

    private Maze disconnectedMaze_multipleRoots;

    private Maze disconnectedMaze_singleRoot;

    @Before
    public void setUp() throws Exception {
        depthFirstSearchRunnerUT = new DepthFirstSearchRunner();

        cyclicMaze = new Maze(2, 2, DummyFactory.getCyclicNodes());
        acyclicMaze = new Maze(2, 2, DummyFactory.getAcyclicNodes());
        disconnectedMaze_multipleRoots = new Maze(2, 2, DummyFactory.getDisconnectedNodes(true));
        disconnectedMaze_singleRoot = new Maze(2, 2, DummyFactory.getDisconnectedNodes(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRun_nullMaze() {
        boolean result = depthFirstSearchRunnerUT.run(null);
    }

    @Test
    public void testRun_cyclic() {
        boolean result = depthFirstSearchRunnerUT.run(cyclicMaze);

        Assert.assertFalse("The maze should contain a cycle " + cyclicMaze, result);
    }

    @Test
    public void testRun_acyclic_disconnected_singleRoot() {
        boolean result = depthFirstSearchRunnerUT.run(disconnectedMaze_singleRoot);

        Assert.assertFalse("The maze should be disconnected " + disconnectedMaze_singleRoot, result);
    }

    @Test
    public void testRun_acyclic_disconnected_multipleRoots() {
        boolean result = depthFirstSearchRunnerUT.run(disconnectedMaze_multipleRoots);

        Assert.assertFalse("The maze should be disconnected " + disconnectedMaze_singleRoot, result);
    }

    @Test
    public void testRun_acyclic() {
        boolean result = depthFirstSearchRunnerUT.run(acyclicMaze);

        Assert.assertTrue("The maze should not contain a cycle " + acyclicMaze, result);
    }
}
