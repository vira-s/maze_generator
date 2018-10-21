package graph.generator;

import graph.utils.DepthFirstSearchRunner;
import model.Maze;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import utils.DummyFactory;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestRecursiveBacktrackerGenerator {

    private DepthFirstSearchRunner depthFirstSearchRunnerUT;

    private RecursiveBacktrackerGenerator recursiveBacktrackerGeneratorUT;

    @Before
    public void setUp() throws Exception {
        recursiveBacktrackerGeneratorUT = new RecursiveBacktrackerGenerator();
        depthFirstSearchRunnerUT = new DepthFirstSearchRunner();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_negativeColumn() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(-1, DummyFactory.DEFAULT_ROWS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_zeroColumn() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(0, DummyFactory.DEFAULT_ROWS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_negativeRow() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(DummyFactory.DEFAULT_COLUMNS, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_zeroRow() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(DummyFactory.DEFAULT_COLUMNS, 0);
    }

    @Test
    public void testGenerate() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(DummyFactory.DEFAULT_COLUMNS, DummyFactory.DEFAULT_ROWS);

        Assert.assertTrue("The maze should be acyclic and connected", depthFirstSearchRunnerUT.run(result));
    }
}
