package edu.elte.thesis.graph.generator;

import edu.elte.thesis.graph.utils.DepthFirstSearchRunner;
import edu.elte.thesis.model.Maze;
import edu.elte.thesis.testutils.DummyFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestRandomWalkGenerator {

    private RandomWalkGenerator randomWalkGeneratorUT;

    private DepthFirstSearchRunner depthFirstSearchRunnerUT;

    @Before
    public void setUp() throws Exception {
        randomWalkGeneratorUT = new RandomWalkGenerator();
        depthFirstSearchRunnerUT = new DepthFirstSearchRunner();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_negativeColumn() {
        Maze result = randomWalkGeneratorUT.generate(-1, DummyFactory.DEFAULT_ROWS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_zeroColumn() {
        Maze result = randomWalkGeneratorUT.generate(0, DummyFactory.DEFAULT_ROWS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_negativeRow() {
        Maze result = randomWalkGeneratorUT.generate(DummyFactory.DEFAULT_COLUMNS, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerate_zeroRow() {
        Maze result = randomWalkGeneratorUT.generate(DummyFactory.DEFAULT_COLUMNS, 0);
    }

    @Test
    public void testGenerate() {
        Maze result = randomWalkGeneratorUT.generate(DummyFactory.DEFAULT_COLUMNS, DummyFactory.DEFAULT_ROWS);

        Assert.assertTrue("The maze should be acyclic and connected", depthFirstSearchRunnerUT.run(result));

    }
}
