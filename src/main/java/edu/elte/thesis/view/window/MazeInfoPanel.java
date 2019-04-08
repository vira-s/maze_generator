package edu.elte.thesis.view.window;

import org.springframework.util.StringUtils;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktoria Sinkovics
 */
public class MazeInfoPanel extends JPanel {

    private static final String HTML_TAG = "<html>";

    private static final String INFO_SEPARATOR = "<span style=\"font-size:10px;font-weight:bold\"> | </span>";

    private static final String BASE_DIR = System.getProperty("user.home") + "\\maze_generator";

    private MazeWindow parentWindow;

    private JTextPane label;

    public MazeInfoPanel(MazeWindow parentWindow) {
        this.parentWindow = parentWindow;

        label = new JTextPane();
        label.setContentType("text/html");
        label.setEditable(false);
        label.setOpaque(false);
        add(label);

        setPreferredSize(new Dimension(parentWindow.getWidth(), 50));
        validate();
    }

    public JTextPane getLabel() {
        return label;
    }

    public MazeWindow getParentWindow() {
        return parentWindow;
    }

    private String getDefaultMazeInfo(Integer columns, Integer rows) {
        return HTML_TAG
                + "<span style=\"font-size=5px\">Using maze size "
                + columns
                + "x"
                + rows
                + INFO_SEPARATOR
                + "Files generated by the program will be stored at: <b>"
                + BASE_DIR
                + "</b></span>"
                ;
    }

    public void setMazeInfo(Integer columns,
                            Integer rows,
                            String mazeFile,
                            String statisticsFile,
                            String vaeFile) {
        StringBuilder labelText = new StringBuilder(getDefaultMazeInfo(columns, rows));

        List<String> fileTexts = new ArrayList<>();

        if (!StringUtils.isEmpty(mazeFile)) {
            fileTexts.add("<b>" + mazeFile + "</b>");
        }

        if (!StringUtils.isEmpty(statisticsFile)) {
            fileTexts.add("<b>" + statisticsFile + "</b>");
        }

        if (!StringUtils.isEmpty(vaeFile)) {
            fileTexts.add("<b>" + vaeFile + "</b>");
        }

        if (!fileTexts.isEmpty()) {
            labelText.append("<span style=\"font-size=5px\"><br>Files updated: ")
                    .append(String.join(INFO_SEPARATOR, fileTexts))
                    .append("</span>");
        }

        label.setText(labelText.toString());
        validate();
    }
}
