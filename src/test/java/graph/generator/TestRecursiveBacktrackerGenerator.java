package graph.generator;

import graph.utils.DepthFirstSearchRunner;
import model.Maze;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Viktoria Sinkovics
 */
public class TestRecursiveBacktrackerGenerator {

    private static final int DEFAULT_COLUMNS = 10;

    private static final int DEFAULT_ROWS = 10;

    private DepthFirstSearchRunner depthFirstSearchRunnerUT;

    private RecursiveBacktrackerGenerator recursiveBacktrackerGeneratorUT;

    @Before
    public void setUp() throws Exception {
        recursiveBacktrackerGeneratorUT = new RecursiveBacktrackerGenerator();
        depthFirstSearchRunnerUT = new DepthFirstSearchRunner();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_negativeColumn() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(-1, DEFAULT_ROWS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_zeroColumn() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(0, DEFAULT_ROWS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_negativeRow() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(DEFAULT_COLUMNS, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_zeroRow() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(DEFAULT_COLUMNS, 0);
    }

    @Test
    public void testGenerate() {
        Maze result = recursiveBacktrackerGeneratorUT.generate(DEFAULT_COLUMNS, DEFAULT_ROWS);

        Assert.assertTrue("The maze should be acyclic and connected", depthFirstSearchRunnerUT.run(result));
    }
}
