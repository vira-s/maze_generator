package edu.elte.thesis.controller;

import edu.elte.thesis.graph.utils.BinarizedMaze;
import edu.elte.thesis.model.Maze;
import edu.elte.thesis.utils.MazeGeneratorAlgorithm;
import edu.elte.thesis.utils.MazeGeneratorRunner;
import edu.elte.thesis.utils.python.PythonRunner;
import edu.elte.thesis.view.event.ConfirmationDialog;
import edu.elte.thesis.view.window.MazeBoardPanel;
import edu.elte.thesis.view.window.MazeInfoPanel;
import edu.elte.thesis.view.window.MazeWindow;
import edu.elte.thesis.view.window.preferences.MazePreferenceTabbedPane;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.Assert;

import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
public class MazeController implements WindowListener {

    private static final Logger LOGGER = LogManager.getLogger(MazeController.class);

    private static final String WINDOW_TITLE = "Maze Generator Using CVAE- by Viktoria Sinkovics";

    private static final Integer DEFAULT_MAZE_SIZE = 10;

    private static final String SIZE_PLACEHOLDER = "%SIZE%";

    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

    private static final String DEFAULT_FOLDER = new File("").getAbsolutePath() + "\\src\\main\\resources\\edu\\elte\\thesis\\data\\";

    private static final String GENERATION_FOLDER = System.getProperty("user.home") + "\\maze_generator\\";

    private static final String MAZE_FOLDER = "training_data\\";

    private static final String GENERATED_FOLDER = "generated\\";

    private static final String STATISTICS_FOLDER = "statistics\\";

    private static final String VAE_FOLDER = "cvae\\";

    // Training Data
    private static final String DEFAULT_TRAINING_DATA_FILENAME = "training_data_" + SIZE_PLACEHOLDER +".txt";
    private static final String DEFAULT_TRAINING_DATA_FILE_LOCATION = DEFAULT_FOLDER + MAZE_FOLDER + DEFAULT_TRAINING_DATA_FILENAME;

    private static final String NEW_TRAINING_DATA_FILENAME = "training_data_" + SIZE_PLACEHOLDER + "_" + CURRENT_DATE + ".txt";
    private static final String NEW_TRAINING_DATA_FILE_LOCATION = GENERATION_FOLDER + MAZE_FOLDER + NEW_TRAINING_DATA_FILENAME;


    // Generate Single Maze
    private static final String DEFAULT_GENERATE_SINGLE_MAZE_FILENAME = "generated_algorithm_mazes_" + SIZE_PLACEHOLDER + ".txt";
    private static final String DEFAULT_GENERATE_SINGLE_MAZE_FILE_LOCATION = GENERATION_FOLDER + GENERATED_FOLDER + DEFAULT_GENERATE_SINGLE_MAZE_FILENAME;


    // VAE Model
    private static final String DEFAULT_VAE_MODEL_FILENAME = "cvae_model_" + SIZE_PLACEHOLDER + ".h5";
    private static final String DEFAULT_VAE_MODEL_FILE_LOCATION = DEFAULT_FOLDER + VAE_FOLDER + DEFAULT_VAE_MODEL_FILENAME;

    private static final String NEW_VAE_MODEL_FILENAME = "cvae_model_" + SIZE_PLACEHOLDER + "_" + CURRENT_DATE + ".h5";
    private static final String NEW_VAE_MODEL_FILE_LOCATION = GENERATION_FOLDER + VAE_FOLDER + NEW_VAE_MODEL_FILENAME;


    // Generated With VAE
    private static final String VAE_GENERATED_FILENAME = "generated_cvae_mazes_" + SIZE_PLACEHOLDER + ".txt";
    private static final String VAE_GENERATED_FILE_LOCATION = GENERATION_FOLDER + VAE_FOLDER + "\\" + VAE_GENERATED_FILENAME;


    // Statistics
    private static final String STATISTICS_FILENAME = "algorithm_statistics.txt";
    private static final String STATISTICS_FILE_LOCATION = GENERATION_FOLDER + STATISTICS_FOLDER + STATISTICS_FILENAME;

    private static final String VAE_STATISTICS_FILENAME = "cvae_statistics.txt";

    private static final String DIMENSION = "--dimension";

    private static final String MODEL_FILE = "--model_file";

    private static final String LOAD_MODEL = "--load_model";

    private static final String GENERATE_ONLY = "--generate_only";

    private static final String TRAINING_DATA = "--training_data";

    private static final String EPOCHS = "--epochs";

    private MazeWindow parentWindow;

    private MazeInfoPanel infoPanel;

    private MazeBoardPanel mazeBoard;

    private MazePreferenceTabbedPane mazePreferenceTabbedPane;

    private PythonRunner pythonRunner;


    public MazeController(MazeWindow parentWindow) {
        super();
        this.parentWindow = parentWindow;
        parentWindow.setTitle(WINDOW_TITLE);
    }

    public MazePreferenceTabbedPane createMazePreferenceTabbedPane() {
        mazePreferenceTabbedPane = new MazePreferenceTabbedPane(this);
        return mazePreferenceTabbedPane;
    }

    public MazeBoardPanel createDefaultMazeBoard() {
        if (Objects.nonNull(mazeBoard)) {
            mazeBoard.setVisible(false);
        }

        mazeBoard = new MazeBoardPanel(new Maze(DEFAULT_MAZE_SIZE, DEFAULT_MAZE_SIZE));
        return mazeBoard;
    }

    public MazeInfoPanel createInfoPanel() {
        return createMazeInfo();
    }

    public void handleGenerateExample(int mazeSize, MazeGeneratorAlgorithm algorithm) {
        Maze maze = MazeGeneratorRunner.generate(
                algorithm,
                mazeSize,
                new File(DEFAULT_GENERATE_SINGLE_MAZE_FILE_LOCATION
                        .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize)),
                new File(STATISTICS_FILE_LOCATION));

        createMazeBoard(maze);
        createMazeInfo(GENERATED_FOLDER + DEFAULT_GENERATE_SINGLE_MAZE_FILENAME
                        .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize),
                STATISTICS_FOLDER + STATISTICS_FILENAME);

        parentWindow.revalidate();
        parentWindow.repaint();
    }

    public boolean handleGenerateExample(int mazeSize, boolean defaultModel) {
        Optional<String> modelFile = retrieveModelFilePath(defaultModel, mazeSize);
        if (modelFile.isPresent()) {
            Map<String, String> arguments = new HashMap<>();
            arguments.put(GENERATE_ONLY, String.valueOf(true));
            arguments.put(LOAD_MODEL, String.valueOf(true));
            arguments.put(DIMENSION, String.valueOf(mazeSize));
            arguments.put(MODEL_FILE, modelFile.get());

            runCommand(arguments);
            return true;
        }
        LOGGER.error("Couldn't find model file");
        return false;
    }

    public File handleGenerateTrainingData(int mazeSize,
                                           int mazeCount,
                                           List<MazeGeneratorAlgorithm> algorithmNames) {
        File mazeFile = new File(NEW_TRAINING_DATA_FILE_LOCATION
                .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize));
        File runTimesFile = new File(STATISTICS_FILE_LOCATION);

        MazeGeneratorRunner.generate(algorithmNames, mazeCount, mazeSize, mazeFile, runTimesFile);

        return mazeFile;
    }

    public Optional<File> handleLoadTrainingData(String filename) {
        Assert.hasLength(filename, "filename should not be empty.");

        File fullFile = new File(filename);
        File defaultFile = new File(DEFAULT_FOLDER + MAZE_FOLDER + filename);
        File generatedFile = new File(GENERATION_FOLDER + MAZE_FOLDER + filename);


        if (fullFile.exists() && !fullFile.isDirectory() && fullFile.length() != 0) {
            LOGGER.info("Found file: {}", fullFile.getAbsolutePath());
            return Optional.of(fullFile);

        } else if (defaultFile.exists() && !defaultFile.isDirectory() && defaultFile.length() != 0) {
            LOGGER.info("Found file: {}", defaultFile.getAbsolutePath());
            return Optional.of(defaultFile);

        } else if (generatedFile.exists() && !generatedFile.isDirectory() && generatedFile.length() != 0) {
            LOGGER.info("Found file: {}", generatedFile.getAbsolutePath());
            return Optional.of(generatedFile);
        }

        LOGGER.error("No file with name '{}' was found", filename);
        return Optional.empty();
    }

    public boolean handleTrainModel(boolean loadModel,
                                    boolean defaultModel,
                                    int mazeSize,
                                    File trainingData,
                                    int epochs) {
        Optional<String> modelFile;
        if (loadModel) {
            modelFile = retrieveModelFilePath(defaultModel, mazeSize);
        } else {
            modelFile = Optional.of(NEW_VAE_MODEL_FILE_LOCATION.replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize));
        }

        if (modelFile.isPresent()) {
            Map<String, String> arguments = new HashMap<>();
            arguments.put(DIMENSION, String.valueOf(mazeSize));
            arguments.put(EPOCHS, String.valueOf(epochs));
            arguments.put(TRAINING_DATA, trainingData.getAbsolutePath());
            arguments.put(MODEL_FILE, modelFile.get());
            if (loadModel) {
                arguments.put(LOAD_MODEL, String.valueOf(true));
            }

            runCommand(arguments);
            return true;
        } else {
            LOGGER.error("Couldn't find model file");
            return false;
        }
    }

    public void cancelProcess() {
        if (Objects.nonNull(pythonRunner)
                && !pythonRunner.isDone()) {
            LOGGER.info("Cancelling process..");
            pythonRunner.cancel(true);
            pythonRunner.done();
        }
    }

    public void handleUpdateMazeBoardAndMazeInfo(int mazeSize, String modelFile, boolean generateOnly) {
        try {
            Thread.sleep(2000);

            Maze maze = getGeneratedResult(VAE_GENERATED_FILE_LOCATION
                    .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize));

            if (generateOnly) {
                updateMazeBoardAndInfoPanel(maze, mazeSize);

            } else {
                updateMazeBoardAndInfoPanel(maze, mazeSize,
                        modelFile.substring(modelFile.indexOf(VAE_FOLDER)));
            }
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void handleUpdateMazeBoard(int mazeSize) {
        try {
            Thread.sleep(2000);

            Maze maze = getGeneratedResult(VAE_GENERATED_FILE_LOCATION
                    .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize));
            updateMazeBoard(maze);
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public void updateProgress(String line, int currentEpoch) {
        if (line.startsWith("Epoch: ")) {
            int totalEpochs = (Integer) mazePreferenceTabbedPane
                    .getModelTrainerAndMazeGenerationPanel()
                    .getGeneratorModelHandlerPanel()
                    .getEpochSpinner()
                    .getValue();

            double runTime = Double.parseDouble(line.substring(line.lastIndexOf(" ")).trim());
            double estimatedTime = (runTime * (totalEpochs - currentEpoch)) / 60 + 1;
            String text = "Estimated time: " + (runTime * (totalEpochs - currentEpoch)) / 60 + 1 + " minutes";

            if (currentEpoch == 1) {
                LOGGER.info(text);
                infoPanel.setProgress(estimatedTime, currentEpoch, totalEpochs);
            } else if (currentEpoch != 0) {
                String newText = " "
                        + currentEpoch
                        + "/"
                        + totalEpochs
                        + " epochs finished.";
                LOGGER.info(text + newText);
                infoPanel.setProgress(estimatedTime, currentEpoch, totalEpochs);
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(null,
                "Are you sure you want to exit?",
                "Exit");
        int answer = confirmationDialog.getAnswer();
        if (answer == JOptionPane.YES_OPTION) {
            cancelProcess();
            System.exit(0);
        } else {
            confirmationDialog.setVisible(false);
        }
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    public MazeWindow getParentWindow() {
        return parentWindow;
    }

    private void runCommand(Map<String, String> arguments) {
        infoPanel.setProgressLabel("Running VAE...");
        parentWindow.revalidate();
        parentWindow.repaint();

        pythonRunner = new PythonRunner(arguments, this);
        pythonRunner.execute();
    }

    private Maze getGeneratedResult(String generatedMazeFileWithPath) throws IOException {
        File generatedMazes = new File(generatedMazeFileWithPath);
        ReversedLinesFileReader reversedLinesFileReader = new ReversedLinesFileReader(generatedMazes, Charset.forName("UTF-8"));
        String line = reversedLinesFileReader.readLine();
        LOGGER.info("{}", line);

        BinarizedMaze binarizedMaze = new BinarizedMaze(Arrays.asList(line.substring(line.indexOf("[") + 1, line.indexOf("]"))
                .replace("\"", "")
                .split(",")));

        return binarizedMaze.createGraphFromBinarizedMaze();
    }

    private void updateMazeBoard(Maze maze) {
        createMazeBoard(maze);
    }

    private void updateMazeBoardAndInfoPanel(Maze maze, int mazeSize) {
        updateMazeBoardAndInfoPanel(maze, mazeSize, null);
    }

    private void updateMazeBoardAndInfoPanel(Maze maze, int mazeSize, String vaeGeneratedFile) {
        createMazeBoard(maze);
        if (Strings.isBlank(vaeGeneratedFile)) {
            createMazeInfo(VAE_FOLDER + CURRENT_DATE + "\\"
                            + VAE_GENERATED_FILENAME.replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize),
                    VAE_FOLDER + VAE_STATISTICS_FILENAME);
        } else {
            createMazeInfo(VAE_FOLDER + CURRENT_DATE + "\\"
                            + VAE_GENERATED_FILENAME.replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize),
                    VAE_FOLDER + VAE_STATISTICS_FILENAME,
                    vaeGeneratedFile);
        }

        parentWindow.revalidate();
        parentWindow.repaint();
    }

    private static Optional<String> retrieveModelFilePath(boolean defaultModel,
                                                          int mazeSize) {
        String newVaeModelFilePrefixWithPath = NEW_VAE_MODEL_FILE_LOCATION
                .substring(0, NEW_VAE_MODEL_FILE_LOCATION.lastIndexOf("_"))
                .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize);

        String defaultVaeFile = DEFAULT_VAE_MODEL_FILE_LOCATION
                .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize);

        File defaultVaeModelFile = new File(defaultVaeFile);
        if (defaultModel
                && defaultVaeModelFile.exists()
                && !defaultVaeModelFile.isDirectory()) {
            LOGGER.info("Using default model: {}", defaultVaeFile);

            return Optional.of(defaultVaeFile);

        } else {
            String newVaeModelFileFolder = newVaeModelFilePrefixWithPath
                    .substring(0, newVaeModelFilePrefixWithPath.lastIndexOf("\\"));
            String newVaeModelPrefix = newVaeModelFilePrefixWithPath
                    .substring(newVaeModelFilePrefixWithPath.lastIndexOf("\\") + 1);

            File[] files = new File(newVaeModelFileFolder)
                    .listFiles((FileFilter) new PrefixFileFilter(newVaeModelPrefix, IOCase.INSENSITIVE));
            LOGGER.info(Arrays.toString(files));

            if (Objects.nonNull(files)
                    && files.length != 0) {
                LOGGER.info("Found matching model file(s): {}", Arrays.toString(files));

                List<File> models = Arrays.stream(files)
                        .sorted(Comparator.comparing(File::lastModified))
                        .collect(Collectors.toList());
                Collections.reverse(models);
                return Optional.of(models.get(0).getAbsolutePath());
            }
        }

        LOGGER.info("Couldn't find any matching files.");
        return Optional.empty();
    }

    private MazeInfoPanel createMazeInfo() {
        return createMazeInfo(null,
                null,
                null);
    }

    private MazeInfoPanel createMazeInfo(String mazeFile, String statisticsFile) {
        return createMazeInfo(mazeFile,
                statisticsFile,
                null);
    }


    private MazeInfoPanel createMazeInfo(String mazeFile, String statisticsFile, String vaeFile) {
        if (Objects.nonNull(infoPanel)) {
            infoPanel.setVisible(false);
            parentWindow.remove(infoPanel);
        }

        infoPanel = new MazeInfoPanel(parentWindow);
        infoPanel.setMazeInfo(mazeBoard.getMaze().getColumns(),
                mazeBoard.getMaze().getRows(),
                mazeFile,
                statisticsFile,
                vaeFile);

        infoPanel.setVisible(true);
        parentWindow.add(infoPanel, BorderLayout.NORTH);

        parentWindow.validate();
        parentWindow.pack();
        return infoPanel;
    }

    private void createMazeBoard(Maze maze) {
        if (Objects.nonNull(mazeBoard)) {
            mazeBoard.setVisible(false);
            parentWindow.remove(mazeBoard);
        }
        mazeBoard = new MazeBoardPanel(maze);

        mazeBoard.setVisible(true);
        parentWindow.add(mazeBoard, BorderLayout.CENTER);
        parentWindow.validate();
        parentWindow.pack();
    }
}
