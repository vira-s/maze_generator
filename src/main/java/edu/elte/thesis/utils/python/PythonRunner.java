package edu.elte.thesis.utils.python;

import edu.elte.thesis.controller.MazeController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;

import javax.swing.SwingWorker;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class to execute python scripts.
 *
 * @author Viktoria Sinkovics
 */
public class PythonRunner extends SwingWorker<Integer, String> {

    private static final Logger LOGGER = LogManager.getLogger(PythonRunner.class);

    private static final String CONDA_PYTHON = System.getProperty("user.home") + "\\Anaconda3\\envs\\maze_generator_tf\\python.exe";

    private static final String ABSOLUTE_PATH = new File("").getAbsolutePath();

    private static final String MAZE_GENERATOR_SCRIPT = ABSOLUTE_PATH + "\\src\\main\\resources\\edu\\elte\\thesis\\python\\maze_generator_cvae.py";

    private MazeController controller;

    private ArrayList<String> command;

    private int status = -2;

    private int currentEpoch = 0;

    private Process process;

    public PythonRunner(Map<String, String> arguments, MazeController controller) {
        Assert.notEmpty(arguments, "arguments should not be empty.");
        Assert.notNull(controller, "controller should not be null.");
        this.controller = controller;

        command = new ArrayList<>();
        command.add(CONDA_PYTHON);
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

        LOGGER.info("Created python command: {}", command);
    }

    @Override
    protected Integer doInBackground() {
        LOGGER.info("Starting process..");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();

            BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = inputStreamReader.readLine()) != null  && !isCancelled()) {
                if (!Strings.isBlank(line)) {
                    publish(line);
                }
            }

            if (!isCancelled()) {
                status = process.waitFor();
            }

            process.getInputStream().close();
            process.getOutputStream().close();
            process.getErrorStream().close();

        } catch (Exception exception) {
            LOGGER.error("An exception was thrown while executing the script.\n{}",
                    Arrays.toString(exception.getStackTrace()));
        } finally {
            if (Objects.nonNull(process)) {
                LOGGER.debug("Closing python process.");
                process.destroy();
            }
        }

        return status;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String line : chunks) {
            LOGGER.info(line);
            if (line.startsWith("Epoch") && !line.startsWith("Epoch: 0")) {
                currentEpoch += 1;
                status = 5;
            }

            this.controller.updateProgress(line, currentEpoch);
            this.controller.handleUpdateMazeBoard(Integer.parseInt(command.get(command.indexOf("--dimension") + 1)));
        }
    }

    @Override
    public void done() {
        if (status == 0 || status == 5) {
            LOGGER.info("Process finished with status: {}. Updating maze board...", status);

            controller.getInfoPanel().stopProgress();

            controller.handleUpdateMazeBoardAndMazeInfo(
                    Integer.parseInt(command.get(command.indexOf("--dimension") + 1)),
                    command.get(command.indexOf("--model_file") + 1),
                    command.contains("--generate_only") || status == 5);
        } else {
            LOGGER.error("Couldn't generate maze");
            controller.getInfoPanel().stopProgress("Couldn't generate maze");
        }
    }
}
