package edu.elte.thesis.view.event.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.BorderFactory;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import java.awt.Color;
import java.text.ParseException;
import java.util.Optional;

/**
 * @author Viktoria Sinkovics
 */
public class MazeGenerationArgumentValidator {

    private static final Logger LOGGER = LogManager.getLogger(MazeGenerationArgumentValidator.class);


    public static final Color COLOR_PASSED = new Color(49, 130, 10);

    public static final Color COLOR_FAILED = new Color(122, 2, 23);

    public static final Border VALIDATION_PASSED_BORDER = BorderFactory.createMatteBorder(2, 2, 2, 2, COLOR_PASSED);

    public static final Border VALIDATION_FAILED_BORDER = BorderFactory.createMatteBorder(2, 2, 2, 2, COLOR_FAILED);

    public static Optional<Integer> validateAndGetMazeSize(JSpinner mazeSizeToGenerateSpinner) {
        try {
            mazeSizeToGenerateSpinner.commitEdit();
        } catch (ParseException exception) {
            exception.printStackTrace();

            return Optional.empty();
        }

        Integer mazeSize = (Integer) mazeSizeToGenerateSpinner.getValue();

        if (mazeSize < 2) {
            LOGGER.error("Maze size too low: {}", mazeSize);
            return Optional.empty();
        }

        return Optional.of(mazeSize);
    }

}
