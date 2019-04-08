package edu.elte.thesis.view.event;

import edu.elte.thesis.utils.MazeGeneratorAlgorithm;
import edu.elte.thesis.view.window.preferences.generator.GeneratorModelPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
public class GenerateWithAlgorithmButtonAction extends AbstractAction {

    private static final Logger LOGGER = LogManager.getLogger(GenerateWithAlgorithmButtonAction.class);

    private static final Color COLOR_PASSED = new Color(49, 130, 10);

    private static final Color COLOR_FAILED = new Color(122, 2, 23);

    private GeneratorModelPanel parent;

    private int mazeSize;

    private MazeGeneratorAlgorithm algorithm;

    public GenerateWithAlgorithmButtonAction(GeneratorModelPanel parent) {
        this.parent = parent;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        boolean argumentsExtracted = validateAndGetGenerationArguments();

        if (argumentsExtracted) {
            parent.getController()
                    .handleGenerateExample(mazeSize, algorithm);
        }
    }

    private boolean validateAndGetGenerationArguments() {
        List<String> errorMessages = new ArrayList<>();

        List<JRadioButton> selectedRadioButtons = parent.getAlgorithmNameRadioButtons()
                .stream()
                .filter(AbstractButton::isSelected)
                .collect(Collectors.toList());

        if (selectedRadioButtons.isEmpty() || selectedRadioButtons.size() > 1) {
            errorMessages.add( "Exactly one algorithm must be selected.");

            parent.getAlgorithmNameRadioButtons()
                    .forEach(checkbox -> checkbox.setForeground(COLOR_FAILED));
        } else {
            String algorithmName = selectedRadioButtons.get(0).getText();
            LOGGER.info("Captured algorithm name: {}", algorithmName);

            parent.getAlgorithmNameRadioButtons().stream()
                    .filter(AbstractButton::isSelected)
                    .forEach(checkbox -> checkbox.setForeground(COLOR_PASSED));
            parent.getAlgorithmNameRadioButtons().stream()
                    .filter(jCheckBox -> !jCheckBox.isSelected())
                    .forEach(checkbox -> checkbox.setForeground(null));

            this.algorithm = MazeGeneratorAlgorithm.findByShortName(algorithmName)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown algorithm name: " + algorithmName));
        }

        try {
            parent.getMazeSizeSpinner().commitEdit();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        mazeSize = (Integer) parent.getMazeSizeSpinner().getValue();
        LOGGER.info("Captured maze size: {}", mazeSize);

        parent.getMazeSizeSpinner()
                .setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, COLOR_PASSED));

        if (!errorMessages.isEmpty()) {
            new ErrorDialog(null,
                    "Please revise the below error(s): \n\t* " + String.join("\n\t* ", errorMessages),
                    "Invalid Maze Generation Arguments");
            return false;
        }

        return true;
    }
}
