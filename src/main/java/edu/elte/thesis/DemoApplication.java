package edu.elte.thesis;

import edu.elte.thesis.graph.generator.GrowingTreeGenerator;
import edu.elte.thesis.graph.generator.HuntAndKillGenerator;
import edu.elte.thesis.graph.generator.MazeGenerator;
import edu.elte.thesis.graph.generator.RandomWalkGenerator;
import edu.elte.thesis.graph.generator.RecursiveBacktrackerGenerator;
import edu.elte.thesis.graph.generator.SidewinderGenerator;
import edu.elte.thesis.messaging.json.JsonObjectMarshaller;
import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.SimplifiedMaze;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Viktoria Sinkovics
 */
public class DemoApplication {

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private static final String FILE_PATH = new File("").getAbsolutePath() + "\\src\\main\\resources\\edu\\elte\\thesis\\generated\\";
    private static final String RUN_TIMES_FILE_NAME = FILE_PATH + "runTimes.txt";

    private static final String HUNT_AND_KILL = "huntAndKill";
    private static final String GROWING_TREE = "growingTree";
    private static final String RANDOM_WALK = "randomWalk";
    private static final String SIDEWINDER = "sidewinder";
    private static final String RECURSIVE_BACKTRACKER = "recursiveBacktracker";

    private static HuntAndKillGenerator huntAndKillGenerator;
    private static GrowingTreeGenerator growingTreeGenerator;
    private static RandomWalkGenerator randomWalkGenerator;
    private static SidewinderGenerator sidewinderGenerator;
    private static RecursiveBacktrackerGenerator recursiveBacktrackerGenerator;

    private static StopWatch stopWatch;
    private static JsonObjectMarshaller jsonObjectMarshaller;

    public static void main(String[] args) throws IOException {
        huntAndKillGenerator = new HuntAndKillGenerator();
        growingTreeGenerator = new GrowingTreeGenerator();
        randomWalkGenerator = new RandomWalkGenerator();
        sidewinderGenerator = new SidewinderGenerator();
        recursiveBacktrackerGenerator = new RecursiveBacktrackerGenerator();

        jsonObjectMarshaller = new JsonObjectMarshaller(SimplifiedMaze.class);
        stopWatch = new StopWatch();

        int size = 5;
        int numberOfRuns = 50000;
        int repeatCount = 10;

        run(size, numberOfRuns, repeatCount, HUNT_AND_KILL, huntAndKillGenerator);

        run(size, numberOfRuns, repeatCount, GROWING_TREE, growingTreeGenerator);

        run(size, numberOfRuns, repeatCount, RANDOM_WALK, randomWalkGenerator);

        run(size, numberOfRuns, repeatCount, SIDEWINDER, sidewinderGenerator);

        run(size, numberOfRuns, repeatCount, RECURSIVE_BACKTRACKER, recursiveBacktrackerGenerator);

    }

    private static void run(int size,
                            int numberOfRuns,
                            int repeatCount,
                            String algorithmName,
                            MazeGenerator mazeGenerator) throws IOException {
        File file = new File(RUN_TIMES_FILE_NAME);
        String time;

        for (int counter = 0; counter < repeatCount; ++counter) {
            File mazeFile = new File(FILE_PATH + algorithmName + "_" + counter + ".txt");

            stopWatch.start();
            runGenerate(numberOfRuns, size, mazeGenerator, mazeFile);
            stopWatch.stop();

            time = convertNanosecondsToReadableFormat(stopWatch.getNanoTime());
            String data = "[" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace("T", " ") + "]: "
                    + time + " - "
                    + algorithmName + " - "
                    + size + "x" + size + " - "
                    + numberOfRuns + " pcs.";
            FileUtils.writeStringToFile(file, data + "\n", StandardCharsets.UTF_8, true);

            stopWatch.reset();
        }
    }

    private static void runGenerate(int numberOfRuns, int size, MazeGenerator mazeGenerator, File mazeFile) throws IOException {
        for (int counter = 0; counter < numberOfRuns; ++counter) {
            Maze maze = mazeGenerator.generate(size, size);

            String data = jsonObjectMarshaller.marshal(maze.getSimplifiedMaze());
            FileUtils.writeStringToFile(mazeFile, data + "\n", StandardCharsets.UTF_8, true);
        }
    }

    private static String convertNanosecondsToReadableFormat(long nanoseconds) {
        double fullTimeInSeconds = ((double) nanoseconds) / 1E9;

        long hours = (long) (fullTimeInSeconds / SECONDS_PER_HOUR);
        int minutes = (int) ((fullTimeInSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
        int seconds = (int) (fullTimeInSeconds % SECONDS_PER_MINUTE);


        return String.format("%dh %dm %ds", hours , minutes, seconds);

    }
}
