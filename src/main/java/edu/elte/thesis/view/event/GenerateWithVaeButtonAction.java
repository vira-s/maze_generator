package edu.elte.thesis.view.event;

import edu.elte.thesis.view.event.utils.MazeGenerationArgumentValidator;
import edu.elte.thesis.view.window.preferences.generator.GeneratorModelPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.text.ParseException;

/**
 * @author Viktoria Sinkovics
 */
public class GenerateWithVaeButtonAction extends AbstractAction {

    private static final Logger LOGGER = LogManager.getLogger(GenerateWithVaeButtonAction.class);

    private GeneratorModelPanel parent;

    private int mazeSize;

    private boolean defaultModel;

    public GenerateWithVaeButtonAction(GeneratorModelPanel parent) {
        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean argumentsExtracted = validateAndGetGenerationArguments();

        if (argumentsExtracted) {
            boolean modelFileFound = parent.getController()
                    .handleGenerateExample(mazeSize, defaultModel);
            if (!modelFileFound) {
                parent.getVaeMazeSizeSpinner()
                        .setBorder(MazeGenerationArgumentValidator.VALIDATION_FAILED_BORDER);

                new ErrorDialog(null,
                        "Please revise the below error(s): \n\t* "
                                + "Couldn't find any model for the specified size (" + mazeSize + "x" + mazeSize + ")",
                        "Invalid Maze Generation Arguments");

            }
        }
    }

    private boolean validateAndGetGenerationArguments() {
        defaultModel = parent.getVaeDefaultModelCheckBox().isSelected();
        parent.getVaeDefaultModelCheckBox().setForeground(MazeGenerationArgumentValidator.COLOR_PASSED);

        try {
            parent.getVaeMazeSizeSpinner().commitEdit();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        mazeSize = (Integer) parent.getVaeMazeSizeSpinner().getValue();
        LOGGER.info("Captured maze size: {}", mazeSize);

        parent.getVaeMazeSizeSpinner()
                .setBorder(MazeGenerationArgumentValidator.VALIDATION_PASSED_BORDER);

        return true;
    }

}
