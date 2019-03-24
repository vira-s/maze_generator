package edu.elte.thesis.view.event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;

/**
 * @author Viktoria Sinkovics
 */
public class ResetContentAction extends AbstractAction {

    private static final Logger LOGGER = LogManager.getLogger(ResetContentAction.class);

    @Override
    public void actionPerformed(ActionEvent event) {
        ConfirmationDialog resetDialog = new ConfirmationDialog(null,
                "Are you sure you want to reset the contents?",
                "Reset Contents");
        if (resetDialog.getAnswer() == JOptionPane.YES_OPTION) {
            // TODO implement action
            LOGGER.info("Resetting..");
        }
        resetDialog.setVisible(false);
    }
}
