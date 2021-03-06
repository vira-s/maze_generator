package edu.elte.thesis;

import edu.elte.thesis.view.window.MazeWindow;

import javax.swing.UIManager;

/**
 * @author Viktoria Sinkovics
 */
public class MazeGeneratorApplication {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MazeWindow mazeWindow = new MazeWindow();
        mazeWindow.setVisible(true);
    }

}
