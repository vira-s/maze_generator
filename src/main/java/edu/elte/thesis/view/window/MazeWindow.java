package edu.elte.thesis.view.window;

import edu.elte.thesis.controller.MazeController;
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

    private MazeInfoPanel infoPanel;

    private MazeBoardPanel mazeBoard;

    private MazePreferenceTabbedPane mazePreferenceTabbedPane;

    public MazeWindow() {
        MazeController mazeController = new MazeController(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(mazeController);

        setDimensions();

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

    private void setDimensions() {
        setResizable(false);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - WINDOW_WIDTH / 2,
                screenSize.height / 2 - WINDOW_HEIGHT / 2);
    }

    public MazeInfoPanel getInfoPanel() {
        return infoPanel;
    }

    public MazeBoardPanel getMazeBoard() {
        return mazeBoard;
    }

    public MazePreferenceTabbedPane getMazePreferenceTabbedPane() {
        return mazePreferenceTabbedPane;
    }
}
