package edu.elte.thesis.view.event;

import edu.elte.thesis.view.window.preferences.generator.GeneratorModelPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.text.ParseException;

/**
 * @author Viktoria Sinkovics
 */
public class GenerateWithVaeButtonAction extends AbstractAction {

    private static final Logger LOGGER = LogManager.getLogger(GenerateWithVaeButtonAction.class);

    private static final Color COLOR_PASSED = new Color(49, 130, 10);

    private static final Color COLOR_FAILED = new Color(122, 2, 23);

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
            parent.getController()
                    .handleGenerateExample(mazeSize, defaultModel);
        }
    }

    private boolean validateAndGetGenerationArguments() {
        defaultModel = parent.getVaeDefaultModelCheckBox().isSelected();
        parent.getVaeDefaultModelCheckBox().setForeground(COLOR_PASSED);

        try {
            parent.getVaeMazeSizeSpinner().commitEdit();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        mazeSize = (Integer) parent.getVaeMazeSizeSpinner().getValue();
        LOGGER.info("Captured maze size: {}", mazeSize);

        parent.getVaeMazeSizeSpinner()
                .setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, COLOR_PASSED));

        return true;
    }

}
