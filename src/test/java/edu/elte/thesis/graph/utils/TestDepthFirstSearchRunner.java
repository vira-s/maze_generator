package edu.elte.thesis.graph.utils;

import edu.elte.thesis.model.Maze;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import edu.elte.thesis.utils.DummyFactory;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestDepthFirstSearchRunner {

    private DepthFirstSearchRunner depthFirstSearchRunnerUT;

    private Maze cyclicMaze;

    private Maze acyclicMaze;

    private Maze disconnectedMaze;

    @Before
    public void setUp() throws Exception {
        depthFirstSearchRunnerUT = new DepthFirstSearchRunner();

        cyclicMaze = new Maze(2, 2, DummyFactory.getCyclicNodes());
        acyclicMaze = new Maze(2, 2, DummyFactory.getAcyclicNodes());
        disconnectedMaze = new Maze(2, 2, DummyFactory.getDisconnectedNodes());
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
    public void testRun_acyclic_disconnected() {
        boolean result = depthFirstSearchRunnerUT.run(disconnectedMaze);

        Assert.assertFalse("The maze should be disconnected " + disconnectedMaze, result);
    }

    @Test
    public void testRun_acyclic() {
        boolean result = depthFirstSearchRunnerUT.run(acyclicMaze);

        Assert.assertTrue("The maze should not contain a cycle " + acyclicMaze, result);
    }
}
