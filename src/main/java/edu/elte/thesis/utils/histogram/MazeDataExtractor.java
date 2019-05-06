package edu.elte.thesis.utils.histogram;

import edu.elte.thesis.graph.utils.BinarizedMaze;
import edu.elte.thesis.graph.utils.DepthFirstSearchRunner;
import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.graph.CellNode;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
public class MazeDataExtractor {

    private static final Logger LOGGER = LogManager.getLogger(MazeDataExtractor.class);

    private static final String GENERATION_FOLDER = System.getProperty("user.home") + "\\maze_generator\\";

    private static final String STATISTICS_FOLDER = "statistics\\";

    private static final String SIZE_PLACEHOLDER = "%SIZE%";

    private static final String WALK_FILENAME = "longest_walks_of_" + SIZE_PLACEHOLDER +"_mazes.txt";

    private static final String SUBGRAPH_FILENAME = "count_of_disconnected_parts_in_" + SIZE_PLACEHOLDER +"_mazes.txt";

    private static final String WALK_FILE_LOCATION = GENERATION_FOLDER + STATISTICS_FOLDER + WALK_FILENAME;

    private static final String SUBGRAPH_FILE_LOCATION = GENERATION_FOLDER + STATISTICS_FOLDER + SUBGRAPH_FILENAME;

    public static boolean calculateLongestWalksAndDisconnectedParts(String filename, int size) {
        List<Integer> longestWalks = new ArrayList<>();
        List<Integer> disconnectedSubgraphCount = new ArrayList<>();

        try {
            List<Maze> mazes = Files.lines(Paths.get(filename))
                    .map(line -> new BinarizedMaze(Arrays.asList(line.substring(line.indexOf("[") + 1, line.indexOf("]"))
                            .replace("\"", "")
                            .split(","))))
                    .map(BinarizedMaze::createGraphFromBinarizedMaze)
                    .collect(Collectors.toList());

            LOGGER.info("Maze count: {}", mazes.size());

            for (Maze maze : mazes) {
                List<CellNode> roots = maze.findAllRoots();
                disconnectedSubgraphCount.add(roots.size());
                LOGGER.info("Root count: {}", roots.size());

                List<Integer> walksOfSingleMaze = new ArrayList<>();
                for (CellNode root : roots) {
                    walksOfSingleMaze.add(DepthFirstSearchRunner.collectAllNodesForRoot(root).size());
                }

                LOGGER.info("Longest walk size: {}", Collections.max(walksOfSingleMaze));
                longestWalks.add(Collections.max(walksOfSingleMaze));
            }

            FileUtils.writeStringToFile(new File(WALK_FILE_LOCATION.replace(SIZE_PLACEHOLDER, size + "x" + size)),
                    Strings.join(longestWalks, ','),
                    StandardCharsets.UTF_8,
                    true);

            FileUtils.writeStringToFile(new File(SUBGRAPH_FILE_LOCATION.replace(SIZE_PLACEHOLDER, size + "x" + size)),
                    Strings.join(disconnectedSubgraphCount, ','),
                    StandardCharsets.UTF_8,
                    true);

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
