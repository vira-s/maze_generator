package edu.elte.thesis.controller;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.utils.MazeGeneratorAlgorithm;
import edu.elte.thesis.utils.MazeGeneratorRunner;
import edu.elte.thesis.utils.VaeRunner;
import edu.elte.thesis.view.window.MazeBoardPanel;
import edu.elte.thesis.view.window.MazeInfoPanel;
import edu.elte.thesis.view.window.MazeWindow;
import edu.elte.thesis.view.window.menu.WindowMenuBar;
import edu.elte.thesis.view.window.preferences.MazePreferenceTabbedPane;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
public class MazeController {

    private static final Logger LOGGER = LogManager.getLogger(MazeController.class);

    private static final String WINDOW_TITLE = "Maze Generator Using CVAE- by Viktoria Sinkovics";

    private static final Integer DEFAULT_MAZE_SIZE = 10;

    private static final String SIZE_PLACEHOLDER = "%SIZE%";

    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

    private static final String DEFAULT_FOLDER = new File("").getAbsolutePath() + "\\src\\main\\resources\\edu\\elte\\thesis\\data\\";

    private static final String GENERATION_FOLDER = System.getProperty("user.home") + "\\maze_generator\\";

    private static final String MAZE_FOLDER = "training_data\\";

    private static final String STATISTICS_FOLDR = "statistics\\";

    private static final String VAE_FOLDER = "vae\\";

    // Training Data
    private static final String DEFAULT_TRAINING_DATA_FILENAME = "training_data_" + SIZE_PLACEHOLDER +".txt";
    private static final String DEFAULT_TRAINING_DATA_FILE_LOCATION = DEFAULT_FOLDER + MAZE_FOLDER + DEFAULT_TRAINING_DATA_FILENAME;

    private static final String NEW_TRAINING_DATA_FILENAME = "training_data_" + SIZE_PLACEHOLDER + "_" + CURRENT_DATE + ".txt";
    private static final String NEW_TRAINING_DATA_FILE_LOCATION = GENERATION_FOLDER + MAZE_FOLDER + NEW_TRAINING_DATA_FILENAME;


    // Generate Single Maze
    private static final String DEFAULT_GENERATE_SINGLE_MAZE_FILENAME = "generated_algorithm_mazes" + SIZE_PLACEHOLDER + ".txt";
    private static final String DEFAULT_GENERATE_SINGLE_MAZE_FILE_LOCATION = GENERATION_FOLDER + DEFAULT_GENERATE_SINGLE_MAZE_FILENAME;


    // VAE Model
    private static final String DEFAULT_VAE_MODEL_FILENAME = "vae_model_" + SIZE_PLACEHOLDER + ".h5";
    private static final String DEFAULT_VAE_MODEL_FILE_LOCATION = DEFAULT_FOLDER + VAE_FOLDER + DEFAULT_VAE_MODEL_FILENAME;

    private static final String NEW_VAE_MODEL_FILENAME = "vae_model_" + SIZE_PLACEHOLDER + "_" + CURRENT_DATE + ".h5";
    private static final String NEW_VAE_MODEL_FILE_LOCATION = GENERATION_FOLDER + VAE_FOLDER + NEW_VAE_MODEL_FILENAME;


    // Generated With VAE
    private static final String VAE_GENERATED_FILENAME = "generated_vae_mazes_" + SIZE_PLACEHOLDER + ".txt";
    private static final String VAE_GENERATED_FILE_LOCATION = GENERATION_FOLDER + VAE_FOLDER + "\\" + CURRENT_DATE + "\\" + VAE_GENERATED_FILENAME;


    // Statistics
    private static final String STATISTICS_FILENAME = "algorithm_statistics.txt";
    private static final String STATISTICS_FILE_LOCATION = GENERATION_FOLDER + STATISTICS_FOLDR + STATISTICS_FILENAME;

    private static final String VAE_STATISTICS_FILENAME = "vae_statistics.txt";
    private static final String VAE_STATISTICS_FILE_WITH_PATH = GENERATION_FOLDER + VAE_FOLDER + VAE_STATISTICS_FILENAME;


    private MazeWindow parentWindow;

    private MazeInfoPanel infoPanel;

    private MazeBoardPanel mazeBoard;

    private MazePreferenceTabbedPane mazePreferenceTabbedPane;

    public MazeController(MazeWindow parentWindow) {
        this.parentWindow = parentWindow;

        initWindowElements();
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
        return this.mazeBoard;
    }

    public MazeInfoPanel createInfoPanel() {
        setMazeInfo();
        infoPanel.setVisible(true);
        return infoPanel;
    }

    public void handleGenerateExample(int mazeSize, MazeGeneratorAlgorithm algorithm) {
        Maze maze = MazeGeneratorRunner.generate(
                algorithm,
                mazeSize,
                new File(DEFAULT_GENERATE_SINGLE_MAZE_FILE_LOCATION
                        .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize)),
                new File(STATISTICS_FILE_LOCATION));

        createMazeBoard(maze);
        setMazeInfo(MAZE_FOLDER + DEFAULT_GENERATE_SINGLE_MAZE_FILENAME
                        .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize),
                STATISTICS_FOLDR + STATISTICS_FILENAME);

        mazeBoard.setVisible(true);
        infoPanel.setVisible(true);
    }

    public void handleGenerateExample(int mazeSize, boolean defaultModel) {
        Optional<Maze> maze = Optional.empty();

        Optional<String> modelFile = retrieveModelFilePath(defaultModel, mazeSize);
        if (modelFile.isPresent()) {
            try {
                maze = VaeRunner.generateOnly(mazeSize,
                        modelFile.get(),
                        VAE_GENERATED_FILE_LOCATION
                                .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize));
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            if (maze.isPresent()) {
                updateMazeBoard(maze.get(), mazeSize);
            } else {
                LOGGER.error("Couldn't generate maze");
            }
        } else {
            LOGGER.error("Couldn't find model file");
        }
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

    public void handleTrainModel(boolean loadModel,
                                 boolean defaultModel,
                                 int mazeSize,
                                 File trainingData,
                                 int epochs) {
        /*
        boolean loadModel,
        int mazeSize,

        int epochs,
        String modelFilename,
        String trainingDataFilename,
        String generatedMazeFileWithPath
         */
        Optional<String> modelFile;
        if (loadModel) {
            modelFile = retrieveModelFilePath(defaultModel, mazeSize);
        } else {
            modelFile = Optional.of(NEW_VAE_MODEL_FILE_LOCATION.replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize));
        }

        Optional<Maze> maze = Optional.empty();
        if (modelFile.isPresent()) {
            try {
                maze = VaeRunner.trainModel(loadModel,
                        mazeSize,
                        epochs,
                        modelFile.get(),
                        trainingData.getAbsolutePath(),
                        VAE_GENERATED_FILE_LOCATION.replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize));
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            if (maze.isPresent()) {
                updateMazeBoard(maze.get(), mazeSize);
            } else {
                LOGGER.error("Couldn't generate maze");
            }
        } else {
            LOGGER.error("Couldn't find model file");
        }
    }

    private void updateMazeBoard(Maze maze, int mazeSize) {
        createMazeBoard(maze);
        setMazeInfo(VAE_FOLDER + "\\" + CURRENT_DATE + "\\"
                        + VAE_GENERATED_FILENAME.replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize),
                VAE_FOLDER + VAE_STATISTICS_FILENAME);

        mazeBoard.setVisible(true);
        infoPanel.setVisible(true);
    }

    private static Optional<String> retrieveModelFilePath(boolean defaultModel,
                                                          int mazeSize) {
        String newVaeModelFilePrefixWithPath = NEW_VAE_MODEL_FILE_LOCATION
                .substring(0, NEW_VAE_MODEL_FILE_LOCATION.lastIndexOf("_"))
                .replace(SIZE_PLACEHOLDER, mazeSize + "x" + mazeSize);

        File defaultVaeModelFile = new File(DEFAULT_VAE_MODEL_FILE_LOCATION);

        if (defaultModel
                && defaultVaeModelFile.exists()
                && !defaultVaeModelFile.isDirectory()) {
            LOGGER.info("Using default model: {}", DEFAULT_VAE_MODEL_FILE_LOCATION);

            return Optional.of(DEFAULT_VAE_MODEL_FILE_LOCATION);

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

    public MazeBoardPanel getMazeBoard() {
        return mazeBoard;
    }

    public MazeInfoPanel getInfoPanel() {
        return infoPanel;
    }

    public MazePreferenceTabbedPane getMazePreferenceTabbedPane() {
        return mazePreferenceTabbedPane;
    }

    public MazeWindow getParentWindow() {
        return parentWindow;
    }

    private void initWindowElements() {
        parentWindow.setTitle(WINDOW_TITLE);
        createDefaultMazeBoard();

        parentWindow.add(createInfoPanel(), BorderLayout.NORTH);
        parentWindow.add(createMazePreferenceTabbedPane(), BorderLayout.WEST);
        parentWindow.add(mazeBoard, BorderLayout.CENTER);
        parentWindow.setJMenuBar(new WindowMenuBar(parentWindow));
    }

    private void setMazeInfo() {
        setMazeInfo(null,
                null,
                null);
    }

    private void setMazeInfo(String mazeFile, String statisticsFile) {
        setMazeInfo(mazeFile,
                statisticsFile,
                null);
    }


    private void setMazeInfo(String mazeFile, String statisticsFile, String vaeFile) {
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

        parentWindow.add(infoPanel, BorderLayout.NORTH);
        infoPanel.setVisible(true);
    }

    private void createMazeBoard(Maze maze) {
        if (Objects.nonNull(mazeBoard)) {
            mazeBoard.setVisible(false);
            parentWindow.remove(mazeBoard);
        }
        mazeBoard = new MazeBoardPanel(maze);

        parentWindow.add(mazeBoard, BorderLayout.CENTER);
        parentWindow.validate();
        parentWindow.pack();
    }
}
