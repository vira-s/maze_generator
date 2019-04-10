package edu.elte.thesis.view.event;

import edu.elte.thesis.controller.MazeController;

import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Viktoria Sinkovics
 */
public class MainWindowListener implements WindowListener {

    private MazeController mazeController;

    public MainWindowListener(MazeController mazeController) {
        super();
        this.mazeController = mazeController;
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        ConfirmationDialog confirmationDialog = new ConfirmationDialog(null,
                "Are you sure you want to exit?",
                "Exit");
        int answer = confirmationDialog.getAnswer();
        if (answer == JOptionPane.YES_OPTION) {
            mazeController.cancelProcess();
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

}
