package edu.elte.thesis.view.window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;

/**
 * @author Viktoria Sinkovics
 */
public class MazeInfoPanel extends JPanel {

    private MazeWindow parentWindow;

    private JLabel mazeInfo;

    public MazeInfoPanel(MazeWindow parentWindow) {
        this.parentWindow = parentWindow;
        mazeInfo = new JLabel();
        add(mazeInfo);

        setPreferredSize(new Dimension(parentWindow.getWidth(), 30));
        validate();
    }

    public JLabel getMazeInfo() {
        return mazeInfo;
    }

    public MazeWindow getParentWindow() {
        return parentWindow;
    }

    public void setMazeInfo(Integer columns, Integer rows) {
        String labelText = "Using maze size " + columns + "x" + rows + ".";
        mazeInfo.setText(labelText);
        validate();
    }
}
