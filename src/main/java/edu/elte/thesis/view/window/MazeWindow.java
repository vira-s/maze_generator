package edu.elte.thesis.view.window;

import edu.elte.thesis.controller.MazeController;
import edu.elte.thesis.view.event.MainWindowListener;
import edu.elte.thesis.view.window.preferences.MazePreferenceTabbedPane;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * @author Viktoria Sinkovics
 */
public class MazeWindow extends JFrame {

    private static final int WINDOW_WIDTH = 1000;

    private static final int WINDOW_HEIGHT = 800;

    private MazeController mazeController;

    private MazeInfoPanel infoPanel;

    private MazeBoardPanel mazeBoard;

    private MazePreferenceTabbedPane mazePreferenceTabbedPane;

    public MazeWindow() {
        mazeController = new MazeController(this);
        setWindowsBasics();

        infoPanel = mazeController.createInfoPanel();
        mazePreferenceTabbedPane = mazeController.createMazePreferenceTabbedPane();
        mazeBoard = mazeController.createDefaultMazeBoard();

        add(infoPanel, BorderLayout.NORTH);
        add(mazePreferenceTabbedPane, BorderLayout.WEST);
        add(mazeBoard, BorderLayout.CENTER);

        validate();
        pack();

        mazePreferenceTabbedPane.setVisible(true);
        mazeBoard.setVisible(true);
        infoPanel.setVisible(true);
    }

    private void setWindowsBasics() {
        setWindowActionPreferences();
        setDimensions();
    }

    private void setWindowActionPreferences() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new MainWindowListener(mazeController));
    }

    private void setDimensions() {
        setResizable(false);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - WINDOW_WIDTH / 2,
                screenSize.height / 2 - WINDOW_HEIGHT / 2);
    }
}
