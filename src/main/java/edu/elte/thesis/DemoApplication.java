package edu.elte.thesis;

import edu.elte.thesis.graph.generator.GrowingTreeGenerator;
import edu.elte.thesis.graph.generator.HuntAndKillGenerator;
import edu.elte.thesis.graph.generator.RandomWalkGenerator;
import edu.elte.thesis.graph.generator.RecursiveBacktrackerGenerator;
import edu.elte.thesis.graph.generator.SidewinderGenerator;
import edu.elte.thesis.messaging.json.JsonObjectMarshaller;
import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.SimplifiedMaze;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * RecursiveBacktracker 3x3     1.000.000   6456.34237576 seconds
 * GrowingTree          3x3     1.000.000   7089.191206233 seconds
 *
 * Sidewinder           3x3     100.000     664.806917414 seconds
 * HuntAndKill          3x3     100.000     842.04610216 seconds
 * RandomWalk           3x3     100.000     1030.228515462 seconds
 *
 *
 * HuntAndKill          4x4     200.000     1427.548865369 seconds
 *
 *
 * @author Viktoria Sinkovics
 */
public class DemoApplication {
    private static final Logger LOGGER = LogManager.getLogger(DemoApplication.class);

    private static final String fileNamePrefix = new File("").getAbsolutePath() + "\\src\\main\\resources\\edu\\elte\\thesis\\generated\\huntAndKill_";
    private static final String runTimesFileName = new File("").getAbsolutePath() + "\\src\\main\\resources\\edu\\elte\\thesis\\generated\\runTimes.txt";

    private static final Object ALGORITHM_NAME =
            "huntAndKill";
            // "growingTree";
            // "randomWalk";
            // "sidewinder";
            // "recursiveBacktracker";

    private static HuntAndKillGenerator huntAndKillGenerator;
    private static GrowingTreeGenerator growingTreeGenerator;
    private static RandomWalkGenerator randomWalkGenerator;
    private static SidewinderGenerator sidewinderGenerator;
    private static RecursiveBacktrackerGenerator recursiveBacktrackerGenerator;

    public static void main(String[] args) throws IOException {
        huntAndKillGenerator = new HuntAndKillGenerator();
        growingTreeGenerator = new GrowingTreeGenerator();
        randomWalkGenerator = new RandomWalkGenerator();
        sidewinderGenerator = new SidewinderGenerator();
        recursiveBacktrackerGenerator = new RecursiveBacktrackerGenerator();

        StopWatch stopWatch = new StopWatch();
        int size = 3;
        int numberOfRuns = 1000000;
        int repeatCount = 5;

        run(stopWatch, size, numberOfRuns, repeatCount);
    }

    private static void run(StopWatch stopWatch, int size, int numberOfRuns, int repeatCount) throws IOException {
        File file = new File(runTimesFileName);
        double timeInSec;

        for (int counter = 0; counter < repeatCount; ++counter) {
            stopWatch.start();
            generate(size, numberOfRuns, counter);
            stopWatch.stop();

            timeInSec = ((double) stopWatch.getNanoTime()) / 1E9;
            String data = "Run finished in " + timeInSec + " seconds. Algorithm=" + ALGORITHM_NAME + ", size=" + size + ", numberOfRuns=" + numberOfRuns + ".";
            FileUtils.writeStringToFile(file, data + "\n", StandardCharsets.UTF_8, true);

            stopWatch.reset();
        }
    }

    private static void generate(int size, int numberOfRuns, int currentRun) throws IOException {
        File file = new File(fileNamePrefix + currentRun + ".txt");
        JsonObjectMarshaller jsonObjectMarshaller = new JsonObjectMarshaller(SimplifiedMaze.class);

        for (int counter = 0; counter < numberOfRuns; ++counter) {
            Maze maze = huntAndKillGenerator.generate(size, size);
                    // growingTreeGenerator.generate(size, size);
                    // randomWalkGenerator.generate(size, size);
                    // sidewinderGenerator.generate(size, size);
                    // recursiveBacktrackerGenerator.generate(size, size);

            String data = jsonObjectMarshaller.marshal(maze.getSimplifiedMaze());
            FileUtils.writeStringToFile(file, data + "\n", StandardCharsets.UTF_8, true);
        }
    }

}
