package edu.elte.thesis.utils.histogram;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author Viktoria Sinkovics
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class TestMazeDataExtractor {

    private static final String GENERATION_FOLDER = System.getProperty("user.home") + "\\maze_generator\\";

    private static final String CVAE_FOLDER = "cvae\\";

    private static final String SIZE_PLACEHOLDER = "%SIZE%";

    private static final String TRAINED_FILENAME = "cvae_mazes_" + SIZE_PLACEHOLDER + ".txt";

    /**
     * Remove @Ignore if a new file needs to be created.
     * Feel free to rename the file.
     */
    @Ignore
    @Test
    public void testCalculateLongestWalk_5x5() {
        String filename = GENERATION_FOLDER + CVAE_FOLDER
                // + "generated_cvae_mazes_5x5.txt";
                + TRAINED_FILENAME.replace(SIZE_PLACEHOLDER, "5x5");

        boolean result = MazeDataExtractor.calculateLongestWalksAndDisconnectedParts(filename, 5);
        Assert.assertTrue(result);
    }


    /**
     * Remove @Ignore if a new file needs to be created.
     * Feel free to rename the file.
     */
    @Ignore
    @Test
    public void testCalculateLongestWalk_10x10() {
        String filename = GENERATION_FOLDER + CVAE_FOLDER
                 // + "generated_cvae_mazes_10x10.txt";
                 + TRAINED_FILENAME.replace(SIZE_PLACEHOLDER, "10x10");

        boolean result = MazeDataExtractor.calculateLongestWalksAndDisconnectedParts(filename, 10);
        Assert.assertTrue(result);
    }


    /**
     * Remove @Ignore if a new file needs to be created.
     * Feel free to rename the file.
     */
    @Ignore
    @Test
    public void testCalculateLongestWalk_20x20() {
        String filename = GENERATION_FOLDER + CVAE_FOLDER
                // + "generated_cvae_mazes_20x20.txt";
                + TRAINED_FILENAME.replace(SIZE_PLACEHOLDER, "20x20");

        boolean result = MazeDataExtractor.calculateLongestWalksAndDisconnectedParts(filename, 20);
        Assert.assertTrue(result);
    }


    /**
     * Remove @Ignore if a new file needs to be created.
     * Feel free to rename the file.
     */
    @Ignore
    @Test
    public void testCalculateLongestWalk_50x50() {
        String filename = GENERATION_FOLDER + CVAE_FOLDER
                // + "generated_cvae_mazes_50x50.txt";
                + TRAINED_FILENAME.replace(SIZE_PLACEHOLDER, "50x50");

        boolean result = MazeDataExtractor.calculateLongestWalksAndDisconnectedParts(filename, 50);
        Assert.assertTrue(result);
    }
}
