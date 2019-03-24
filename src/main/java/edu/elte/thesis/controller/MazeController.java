package edu.elte.thesis.controller;

import edu.elte.thesis.view.window.MazeBoardPanel;
import edu.elte.thesis.view.window.MazeCellButton;
import edu.elte.thesis.view.window.MazeInfoPanel;
import edu.elte.thesis.view.window.MazeWindow;
import edu.elte.thesis.view.window.menu.WindowMenuBar;
import edu.elte.thesis.view.window.preferences.MazePreferenceTabbedPane;

import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Objects;

/**
 * @author Viktoria Sinkovics
 */
public class MazeController {
    private static final String WINDOW_TITLE = "Maze Generator Using CVAE- by Viktoria Sinkovics";

    private static final Integer DEFAULT_MAZE_SIZE = 10;

    private MazeWindow parentWindow;

    private MazeBoardPanel mazeBoard;

    private MazeInfoPanel infoPanel;

    private JButton board[][];

    public MazeController(MazeWindow parentWindow) {
        this.parentWindow = parentWindow;

        initWindowElements();
    }

    public MazeBoardPanel getMazeBoard() {
        return mazeBoard;
    }

    public MazeInfoPanel getInfoPanel() {
        return infoPanel;
    }

    public JButton[][] getBoard() {
        return board;
    }

    public MazePreferenceTabbedPane createMazePreferenceTabbedPane() {
        return new MazePreferenceTabbedPane();
    }

    public MazeBoardPanel createMazeBoard() {
        return createMazeBoard(DEFAULT_MAZE_SIZE, DEFAULT_MAZE_SIZE);
    }

    public MazeBoardPanel createMazeBoard(Integer columns, Integer rows) {
        if (Objects.nonNull(mazeBoard)) {
            mazeBoard.setVisible(false);
        }
        mazeBoard = new MazeBoardPanel(columns, rows);

        board = new JButton[columns][rows];
        createBoard(columns, rows);
        return this.mazeBoard;
    }

    public MazeInfoPanel createInfoPanel() {
        infoPanel = new MazeInfoPanel(parentWindow);
        setMazeInfo();
        infoPanel.setVisible(true);
        return infoPanel;
    }

    private void initWindowElements() {
        parentWindow.setTitle(WINDOW_TITLE);
        createMazeBoard();
        parentWindow.add(createInfoPanel(), BorderLayout.NORTH);
        parentWindow.add(mazeBoard);
        parentWindow.setJMenuBar(new WindowMenuBar(parentWindow));
    }

    private void createBoard(Integer columns, Integer rows) {
        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                createBoardElement(column, row);
            }
        }
    }

    private void createBoardElement(Integer column, Integer row) {
        MazeCellButton button = new MazeCellButton(this, column, row);
        button.setPreferredSize(new Dimension(750 % this.mazeBoard.getColumns(),
                750 % this.mazeBoard.getRows()));
        board[column][row] = button;
        mazeBoard.add(button);

    }

    private void setMazeInfo() {
        infoPanel.setMazeInfo(mazeBoard.getColumns(), mazeBoard.getRows());
    }

}
