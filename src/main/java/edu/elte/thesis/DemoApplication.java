package edu.elte.thesis;

import edu.elte.thesis.utils.MazeGeneratorAlgorithm;
import edu.elte.thesis.utils.MazeGeneratorRunner;

import java.io.File;

/**
 * @author Viktoria Sinkovics
 */
public class DemoApplication {

    private static final String BASE_DIR = System.getProperty("user.home") + "\\maze_generator";

    private static final String MAZE_FILE_FOLDER = "\\generated\\";

    private static final String STATISTICS_FILE_FOLDR = "\\statistics\\";

    private static final String STATISTICS_FILE_NAME = "algorithm_statistics.txt";

    private static final String STATISTICS_FILE_WITH_PATH = BASE_DIR + STATISTICS_FILE_FOLDR + STATISTICS_FILE_NAME;

    private static final String DEFAULT_MAZE_FILE_NAME = "generated_algorithm_mazes.txt";

    private static final String DEFAULT_MAZE_FILE_WITH_PATH = BASE_DIR + MAZE_FILE_FOLDER + DEFAULT_MAZE_FILE_NAME;


    public static void main(String[] args) {

        int size = 10;
        int numberOfRuns = 50000;

        MazeGeneratorRunner.generate(
                MazeGeneratorAlgorithm.HUNT_AND_KILL,
                numberOfRuns,
                size,
                new File(DEFAULT_MAZE_FILE_WITH_PATH.replace(".txt", "_hk.txt")),
                new File(STATISTICS_FILE_WITH_PATH));

        MazeGeneratorRunner.generate(
                MazeGeneratorAlgorithm.GROWING_TREE,
                numberOfRuns,
                size,
                new File(DEFAULT_MAZE_FILE_WITH_PATH.replace(".txt", "_gt.txt")),
                new File(STATISTICS_FILE_WITH_PATH));

        MazeGeneratorRunner.generate(
                MazeGeneratorAlgorithm.RANDOM_WALK,
                numberOfRuns,
                size,
                new File(DEFAULT_MAZE_FILE_WITH_PATH.replace(".txt", "_rw.txt")),
                new File(STATISTICS_FILE_WITH_PATH));

        MazeGeneratorRunner.generate(
                MazeGeneratorAlgorithm.SIDEWINDER,
                numberOfRuns,
                size,
                new File(DEFAULT_MAZE_FILE_WITH_PATH.replace(".txt", "_sw.txt")),
                new File(STATISTICS_FILE_WITH_PATH));

        MazeGeneratorRunner.generate(
                MazeGeneratorAlgorithm.RECURSIVE_BACKTRACKER,
                numberOfRuns,
                size,
                new File(DEFAULT_MAZE_FILE_WITH_PATH.replace(".txt", "_rb.txt")),
                new File(STATISTICS_FILE_WITH_PATH));

    }
}
