package edu.elte.thesis.utils;

import edu.elte.thesis.graph.generator.GrowingTreeGenerator;
import edu.elte.thesis.graph.generator.HuntAndKillGenerator;
import edu.elte.thesis.graph.generator.MazeGenerator;
import edu.elte.thesis.graph.generator.RandomWalkGenerator;
import edu.elte.thesis.graph.generator.RecursiveBacktrackerGenerator;
import edu.elte.thesis.graph.generator.SidewinderGenerator;
import edu.elte.thesis.graph.utils.BinarizedMaze;
import edu.elte.thesis.messaging.json.JsonObjectMarshaller;
import edu.elte.thesis.model.Maze;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Viktoria Sinkovics
 */
public class MazeGeneratorRunner {
    private static final Logger LOGGER = LogManager.getLogger(MazeGeneratorRunner.class);

    private static final int SECONDS_PER_MINUTE = 60;

    private static final int MINUTES_PER_HOUR = 60;

    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private static final HuntAndKillGenerator HUNT_AND_KILL_GENERATOR = new HuntAndKillGenerator();

    private static final GrowingTreeGenerator GROWING_TREE_GENERATOR = new GrowingTreeGenerator();

    private static final RandomWalkGenerator RANDOM_WALK_GENERATOR = new RandomWalkGenerator();

    private static final SidewinderGenerator SIDEWINDER_GENERATOR = new SidewinderGenerator();

    private static final RecursiveBacktrackerGenerator RECURSIVE_BACKTRACKER_GENERATOR = new RecursiveBacktrackerGenerator();

    private static final StopWatch STOP_WATCH = new StopWatch();

    private static final BinarizedMaze BINARIZED_MAZE = new BinarizedMaze();

    private static final JsonObjectMarshaller JSON_OBJECT_MARSHALLER = new JsonObjectMarshaller(BinarizedMaze.class);

    public static Maze generate(MazeGeneratorAlgorithm algorithm,
                                int size,
                                File mazeFile,
                                File runTimesFile) {
        Assert.notNull(algorithm, "algorithm should not be null.");

        Maze maze;

        switch (algorithm) {
            case HUNT_AND_KILL:
                maze = run(size, HUNT_AND_KILL_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case GROWING_TREE:
                maze = run(size, GROWING_TREE_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case RANDOM_WALK:
                maze = run(size, RANDOM_WALK_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case SIDEWINDER:
                maze = run(size, SIDEWINDER_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case RECURSIVE_BACKTRACKER:
                maze = run(size, RECURSIVE_BACKTRACKER_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }

        return maze;
    }

    public static void generate(MazeGeneratorAlgorithm algorithm,
                                int count,
                                int size,
                                File mazeFile,
                                File runTimesFile) {

        switch (algorithm) {
            case HUNT_AND_KILL:
                run(count, size, HUNT_AND_KILL_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case GROWING_TREE:
                run(count, size, GROWING_TREE_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case RANDOM_WALK:
                run(count, size, RANDOM_WALK_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case SIDEWINDER:
                run(count, size, SIDEWINDER_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
            case RECURSIVE_BACKTRACKER:
                run(count, size, RECURSIVE_BACKTRACKER_GENERATOR, algorithm.getShortName(), mazeFile, runTimesFile);
                break;
        }

    }

    public static void generate(Collection<MazeGeneratorAlgorithm> algorithms,
                                int count,
                                int size,
                                File mazeFile,
                                File runTimesFile) {
        Assert.notNull(algorithms, "algorithms should not be null.");
        Assert.isTrue(!algorithms.isEmpty(), "algorithms should not be empty.");

        int algorithmCount = algorithms.size();
        int countPerAlgorithm = count / algorithmCount;
        int countForLastAlgorithm = countPerAlgorithm + (count % algorithmCount);

        List<MazeGeneratorAlgorithm> mazeGeneratorAlgorithms = new ArrayList<>(algorithms);
        mazeGeneratorAlgorithms.forEach(algorithm -> {
            if (mazeGeneratorAlgorithms.indexOf(algorithm) == algorithmCount - 1) {
                LOGGER.info("Generating " + countForLastAlgorithm + "pcs. with " + algorithm.getShortName());
                generate(algorithm, countForLastAlgorithm, size, mazeFile, runTimesFile);
            } else {
                LOGGER.info("Generating " + countPerAlgorithm + "pcs. with " + algorithm.getShortName());
                generate(algorithm, countPerAlgorithm, size, mazeFile, runTimesFile);
            }
        });
    }

    private static void run(int count,
                            int size,
                            MazeGenerator generator,
                            String algorithm,
                            File mazeFile,
                            File runTimesFile) {
        STOP_WATCH.start();
        runGenerate(count, size, generator, mazeFile);
        STOP_WATCH.stop();

        saveRunTimesToFile(algorithm, size, count, runTimesFile);
        STOP_WATCH.reset();
    }

    private static Maze run(int size,
                            MazeGenerator generator,
                            String algorithm,
                            File mazeFile,
                            File runTimesFile) {
        STOP_WATCH.start();
        Maze maze = runGenerate(size, generator, mazeFile);
        STOP_WATCH.stop();

        saveRunTimesToFile(algorithm, size, 0, runTimesFile);
        STOP_WATCH.reset();
        return maze;
    }

    private static void saveRunTimesToFile(String algorithm,
                                           int size,
                                           int count,
                                           File runTimesFile) {
        String time = convertNanosecondsToReadableFormat(STOP_WATCH.getNanoTime());

        StringBuilder data = new StringBuilder();
        data.append("[")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .replace("T", " "))
                .append("]: ")
                .append(time)
                .append(" - ")
                .append(algorithm)
                .append(" - ")
                .append(size)
                .append('x')
                .append(size);

        if (count > 0) {
            data.append(" - ")
                    .append(count)
                    .append(" pcs.");
        }
        data.append("\n");

        try {
            FileUtils.writeStringToFile(runTimesFile, data.toString(), StandardCharsets.UTF_8, true);
        } catch (IOException exception) {
            LOGGER.error("Something went wrong while writing to file {}{}.", runTimesFile);
            exception.printStackTrace();
        }

    }

    private static void runGenerate(int mazeCount,
                                    int size,
                                    MazeGenerator mazeGenerator,
                                    File mazeFile) {
        for (int counter = 0; counter < mazeCount; ++counter) {
            runGenerate(size, mazeGenerator, mazeFile);
        }
    }

    private static Maze runGenerate(int size,
                                    MazeGenerator mazeGenerator,
                                    File mazeFile) {
        Maze maze = mazeGenerator.generate(size, size);
        saveMazeToFile(maze, mazeFile);

        return maze;
    }

    private static void saveMazeToFile(Maze maze, File mazeFile) {
        String data = JSON_OBJECT_MARSHALLER.marshal(BINARIZED_MAZE.binarizeMaze(maze));
        try {
            FileUtils.writeStringToFile(mazeFile, data + "\n", StandardCharsets.UTF_8, true);
        } catch (IOException exception) {
            LOGGER.error("Something went wrong while writing to file {}.", mazeFile);
            exception.printStackTrace();
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
