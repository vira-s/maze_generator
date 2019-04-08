package edu.elte.thesis.utils;

import edu.elte.thesis.graph.utils.BinarizedMaze;
import edu.elte.thesis.model.Maze;
import edu.elte.thesis.utils.python.PythonRunner;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Viktoria Sinkovics
 */
public class VaeRunner {

    private static final Logger LOGGER = LogManager.getLogger(VaeRunner.class);

    private static final String DIMENSION = "--dimension";

    private static final String MODEL_FILE = "--model_file";

    private static final String LOAD_MODEL = "--load_model";

    private static final String GENERATE_ONLY = "--generate_only";

    private static final String TRAINING_DATA = "--training_data";

    private static final String EPOCHS = "--epochs";

    public static Optional<Maze> generateOnly(int mazeSize,
                                              String modelFilename,
                                              String generatedMazeFileWithPath) throws IOException {
        Map<String, String> arguments = new HashMap<>();
        arguments.put(GENERATE_ONLY, String.valueOf(true));
        arguments.put(LOAD_MODEL, String.valueOf(true));
        arguments.put(DIMENSION, String.valueOf(mazeSize));
        arguments.put(MODEL_FILE, modelFilename);

        return runScriptWithArguments(arguments, generatedMazeFileWithPath);
    }

    public static Optional<Maze> trainModel(boolean loadModel,
                                            int mazeSize,
                                            int epochs,
                                            String modelFilename,
                                            String trainingDataFilename,
                                            String generatedMazeFileWithPath) throws IOException {
        Map<String, String> arguments = new HashMap<>();
        arguments.put(DIMENSION, String.valueOf(mazeSize));
        arguments.put(EPOCHS, String.valueOf(epochs));
        arguments.put(TRAINING_DATA, trainingDataFilename);
        arguments.put(MODEL_FILE, modelFilename);
        if (loadModel) {
            arguments.put(LOAD_MODEL, String.valueOf(true));
        }

        LOGGER.info("Arguments: {}", arguments);
        return runScriptWithArguments(arguments, generatedMazeFileWithPath);
    }

    private static Optional<Maze> runScriptWithArguments(Map<String, String> arguments,
                                                         String generatedMazeFileWithPath) throws IOException {
        int exitValue = PythonRunner.runPython(arguments);

        if (exitValue == 0) {
            LOGGER.info("Script exited with 0");

            Maze maze = getGeneratedResult(generatedMazeFileWithPath);
            return Optional.of(maze);
        }

        LOGGER.info("Couldn't execute the script.");
        return Optional.empty();
    }

    private static Maze getGeneratedResult(String generatedMazeFileWithPath) throws IOException {
        File generatedMazes = new File(generatedMazeFileWithPath);
        ReversedLinesFileReader reversedLinesFileReader = new ReversedLinesFileReader(generatedMazes, Charset.forName("UTF-8"));
        String line = reversedLinesFileReader.readLine();
        LOGGER.info("{}", line);

        // TODO investigate why the unmarshaller doesn't work
        //  JsonObjectMarshaller jsonObjectMarshaller = new JsonObjectMarshaller(BinarizedMaze.class);
        // TODO investigate why the unmarshaller doesn't work
        //  BinarizedMaze binarizedMaze = (BinarizedMaze) jsonObjectMarshaller.unmarshal(line);

        BinarizedMaze binarizedMaze = new BinarizedMaze(Arrays.asList(line.substring(line.indexOf("[") + 1, line.indexOf("]"))
                .replace("\"", "")
                .split(",")));

        Maze maze = binarizedMaze.createGraphFromBinarizedMaze();
        LOGGER.info(maze);
        return maze;
    }

}
