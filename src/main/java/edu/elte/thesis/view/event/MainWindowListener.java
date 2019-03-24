package edu.elte.thesis.view.event;

import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Viktoria Sinkovics
 */
public class MainWindowListener implements WindowListener {

    @Override
    public void windowClosing(WindowEvent arg0) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(null,
                "Are you sure you want to exit?",
                "Exit");
        int answer = confirmationDialog.getAnswer();
        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
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

}
