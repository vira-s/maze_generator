package edu.elte.thesis.view.event;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;

/**
 * @author Viktoria Sinkovics
 */
public class ExitAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent event) {
        ConfirmationDialog exitDialog = new ConfirmationDialog(null, "Are you sure you want to exit?", "Exit");
        if (exitDialog.getAnswer() == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            exitDialog.setVisible(false);
        }
    }

}
