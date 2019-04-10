package edu.elte.thesis.utils.python;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Utility class to execute python scripts.
 *
 * @author Viktoria Sinkovics
 */
public class PythonRunner {

    private static final Logger LOGGER = LogManager.getLogger(PythonRunner.class);

    private static final String ABSOLUTE_PATH = new File("").getAbsolutePath();

    private static final String MAZE_GENERATOR_SCRIPT = ABSOLUTE_PATH + "\\src\\main\\resources\\edu\\elte\\thesis\\python\\maze_generator_cvae.py";

    private ArrayList<String> command;

    public PythonRunner(Map<String, String> arguments) {

        Assert.notEmpty(arguments, "arguments should not be empty.");

        command = new ArrayList<>();
        command.add("python");
        command.add("-u");
        command.add(MAZE_GENERATOR_SCRIPT);

        arguments.forEach((argument, value) -> {
            if (value.equals(String.valueOf(true))) {
                command.add(argument);
            } else {
                command.add(argument);
                command.add(value);
            }
        });
    }

    public int runCommand() {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(false);
        Process process = null;
        int exitValue = -2;
        try {
            process = processBuilder.start();

            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = inputStreamReader.readLine()) != null) {
                if (!Strings.isBlank(line)) {
                    LOGGER.info("{}", line);
                }
            }
            inputStreamReader.close();

            while ((line = errorStreamReader.readLine()) != null) {
                if (!Strings.isBlank(line)) {
                    LOGGER.error("{}", line);
                }
            }
            errorStreamReader.close();

            // TODO maybe more if there are more epochs/bigger mazes
            process.waitFor(60, TimeUnit.MINUTES);
            exitValue = process.exitValue();

            LOGGER.info("Script exited with value [{}]", exitValue);

        } catch (Exception exception) {
            LOGGER.error("An exception was thrown while executing the script.\n{}",
                    Arrays.toString(exception.getStackTrace()));
        } finally {
            if (Objects.nonNull(process)) {
                LOGGER.debug("Closing python process.");
                process.destroy();
            }
        }

        return exitValue;
    }
}
