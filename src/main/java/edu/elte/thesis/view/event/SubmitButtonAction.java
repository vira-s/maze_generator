package edu.elte.thesis.view.event;

import edu.elte.thesis.utils.MazeGeneratorAlgorithm;
import edu.elte.thesis.view.event.utils.MazeGenerationArgumentValidator;
import edu.elte.thesis.view.window.preferences.trainer.MazeGenerationHandlerPanel;
import edu.elte.thesis.view.window.preferences.trainer.ModelHandlerPanel;
import edu.elte.thesis.view.window.preferences.trainer.ModelTrainerAndMazeGenerationPanel;
import edu.elte.thesis.view.window.preferences.trainer.TrainingDataPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
public class SubmitButtonAction extends AbstractAction {

    private static final Logger LOGGER = LogManager.getLogger(SubmitButtonAction.class);

    private ModelTrainerAndMazeGenerationPanel parent;

    private int mazeSize;

    private int mazeCount;

    private File trainingData;

    private File modelFile;

    private List<MazeGeneratorAlgorithm> algorithmNames;

    public SubmitButtonAction(ModelTrainerAndMazeGenerationPanel parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        TrainingDataPanel trainingDataPanel = parent.getTrainingDataPanel();
        ModelHandlerPanel modelHandlerPanel = parent.getGeneratorModelHandlerPanel();

        boolean validationOk;
        boolean canIRunIt = false;

        if (trainingDataPanel.getGenerateMazeRadioButton()
                .isSelected()) {
            validationOk = validateAndGetTrainingDataGenerationArguments(trainingDataPanel.getGenerationFieldPanel());
            if (validationOk) {
                trainingData = parent.getController()
                        .handleGenerateTrainingData(mazeSize, mazeCount, algorithmNames);
            }
        } else {
            validationOk = validateAndGetLoadedTrainingData(trainingDataPanel.getFileLoaderPanel().getFilePathField());
        }

        if (validationOk) {
            parent.getController()
                    .handleTrainModel(
                            modelHandlerPanel.getExistingModelButton().isSelected(),
                            modelHandlerPanel.getVaeDefaultModelCheckBox().isEnabled()
                                    && modelHandlerPanel.getVaeDefaultModelCheckBox().isSelected(),
                            mazeSize,
                            trainingData,
                            getEpochs(modelHandlerPanel.getEpochSpinner()));
        }
    }

    private boolean validateAndGetLoadedTrainingData(JTextField trainingDataFileField) {
        List<String> errorMessages = new ArrayList<>();
        String text = trainingDataFileField.getText();

        if (Strings.isBlank(text)) {
            errorMessages.add("A filename must be provided.");
            trainingDataFileField.setBorder(MazeGenerationArgumentValidator.VALIDATION_FAILED_BORDER);
        } else {
            Optional<File> trainingDataOptional = parent.getController()
                    .handleLoadTrainingData(text);

            if (trainingDataOptional.isPresent()) {
                trainingData = trainingDataOptional.get();
                String trainingDataAbsolutePath = trainingData.getAbsolutePath();
                LOGGER.info("Training Data: {}", trainingDataAbsolutePath);

                trainingDataAbsolutePath = trainingDataAbsolutePath.substring(0, trainingDataAbsolutePath.lastIndexOf("."));
                mazeSize = Integer.parseInt(trainingDataAbsolutePath.substring(trainingDataAbsolutePath.lastIndexOf("x") + 1));

                return true;
            } else {
                errorMessages.add("The provided training data file was not found or it was empty.");
                LOGGER.error("The provided training data file '{}' was not found or it was empty.", text);
                trainingDataFileField.setBorder(MazeGenerationArgumentValidator.VALIDATION_FAILED_BORDER);
            }
        }

        if (!errorMessages.isEmpty()) {
            new ErrorDialog(null,
                    "Please revise the below error(s): \n\t* " + String.join("\n\t* ", errorMessages),
                    "Invalid Training Data File");
        }

        return false;
    }

    private boolean validateAndGetTrainingDataGenerationArguments(MazeGenerationHandlerPanel generationFieldPanel) {
        List<String> errorMessages = new ArrayList<>();

        handleMazeSize(errorMessages, generationFieldPanel.getMazeSizeToGenerateSpinner());
        handleMazeCount(generationFieldPanel.getMazeCountToGenerateSpinner());
        handleAlgorithms(errorMessages, generationFieldPanel.getAlgorithmNameCheckBoxes());

        if (!errorMessages.isEmpty()) {
            new ErrorDialog(null,
                    "Please revise the below error(s): \n\t* " + String.join("\n\t* ", errorMessages),
                    "Invalid Training Data Arguments");
            return false;
        }

        return true;
    }

    private void handleMazeSize(List<String> errorMessages, JSpinner mazeSizeToGenerateSpinner) {
        Optional<Integer> resultSize = MazeGenerationArgumentValidator.validateAndGetMazeSize(mazeSizeToGenerateSpinner);

        if (resultSize.isPresent()) {
            mazeSize = resultSize.get();
            LOGGER.info("Captured maze size: {}", mazeSize);

            mazeSizeToGenerateSpinner
                    .setBorder(MazeGenerationArgumentValidator.VALIDATION_PASSED_BORDER);
        } else {
            errorMessages.add("Maze size needs to be at least 2.");
            mazeSizeToGenerateSpinner
                    .setBorder(MazeGenerationArgumentValidator.VALIDATION_FAILED_BORDER);
        }
    }

    private void handleMazeCount(JSpinner mazeCountToGenerateSpinner) {
        mazeCount = (Integer) mazeCountToGenerateSpinner.getValue();
        LOGGER.info("Captured maze count: {}", mazeCount);

        mazeCountToGenerateSpinner
                .setBorder(MazeGenerationArgumentValidator.VALIDATION_PASSED_BORDER);

    }

    private int getEpochs(JSpinner epochsSpinner) {
        int epochs = (Integer) epochsSpinner.getValue();
        LOGGER.info("Captured epochs: {}", epochs);

        epochsSpinner
                .setBorder(MazeGenerationArgumentValidator.VALIDATION_PASSED_BORDER);
        return epochs;
    }

    private void handleAlgorithms(List<String> errorMessages, List<JCheckBox> algorithmNameCheckBoxes) {
        algorithmNames = retrieveSelectedAlgorithms(algorithmNameCheckBoxes);
        if (!algorithmNames.isEmpty()) {
            LOGGER.info("Captured algorithm names: {}", algorithmNames);
            algorithmNameCheckBoxes.stream()
                    .filter(AbstractButton::isSelected)
                    .forEach(checkbox -> checkbox.setForeground(MazeGenerationArgumentValidator.COLOR_PASSED));
            algorithmNameCheckBoxes.stream()
                    .filter(jCheckBox -> !jCheckBox.isSelected())
                    .forEach(checkbox -> checkbox.setForeground(Color.BLACK));
        } else {
            errorMessages.add("At least one algorithm must be selected.");

            algorithmNameCheckBoxes
                    .forEach(checkbox -> checkbox.setForeground(MazeGenerationArgumentValidator.COLOR_FAILED));
        }
    }

    private List<MazeGeneratorAlgorithm> retrieveSelectedAlgorithms(List<JCheckBox> algorithmNameCheckBoxes) {
        return algorithmNameCheckBoxes.stream()
                .filter(AbstractButton::isSelected)
                .map(checkbox -> MazeGeneratorAlgorithm.findByShortName(checkbox.getText()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
