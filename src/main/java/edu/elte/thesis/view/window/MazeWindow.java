package edu.elte.thesis.view.window;

import edu.elte.thesis.controller.MazeController;
import edu.elte.thesis.view.event.MainWindowListener;
import edu.elte.thesis.view.window.preferences.MazePreferenceTabbedPane;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * @author Viktoria Sinkovics
 */
public class MazeWindow extends JFrame {

    private MazeController mazeController;

    private MazeInfoPanel infoPanel;

    private MazeBoardPanel mazeBoard;

    private MazePreferenceTabbedPane mazePreferenceTabbedPane;

    public MazeWindow() {
        mazeController = new MazeController(this);
        setWindowsBasics();

        infoPanel = mazeController.createInfoPanel();
        mazePreferenceTabbedPane = mazeController.createMazePreferenceTabbedPane();
        mazeBoard = mazeController.createMazeBoard();

        add(infoPanel, BorderLayout.NORTH);
        add(mazePreferenceTabbedPane, BorderLayout.WEST);
        add(mazeBoard, BorderLayout.CENTER);

        validate();
        pack();

        mazePreferenceTabbedPane.setVisible(true);
        mazeBoard.setVisible(true);
    }

    private void setWindowsBasics() {
        setWindowActionPreferences();
        setDimensions();
    }

    private void setWindowActionPreferences() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new MainWindowListener());
    }

    private void setDimensions() {
        setPreferredSize(new Dimension(1000, 800));
        setResizable(false);
    }
}
