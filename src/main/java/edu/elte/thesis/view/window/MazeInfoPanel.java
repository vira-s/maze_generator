package edu.elte.thesis.view.window;

import edu.elte.thesis.view.window.utils.WindowUtils;
import org.springframework.util.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
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

    private JPanel progressPanel;

    private JLabel progressLabel;

    private JProgressBar progressBar;

    private int estimatedTime;

    public MazeInfoPanel(MazeWindow parentWindow) {
        this.parentWindow = parentWindow;
        setLayout(new GridLayout(0, 1));
        label = new JTextPane();
        label.setContentType("text/html");
        label.setEditable(false);
        label.setOpaque(false);
        add(label);

        initProgressPanel();

        setPreferredSize(new Dimension(parentWindow.getWidth(), 50));
        setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 30));
        validate();
    }

    private void initProgressPanel() {
        progressPanel = new JPanel();
        progressPanel.setPreferredSize(new Dimension(600, 20));
        progressPanel.setLayout(new GridBagLayout());

        progressLabel = new JLabel("Running...");
        progressLabel.setPreferredSize(new Dimension(300, 15));
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(200, 15));

        GridBagConstraints labelConstraints = WindowUtils.createConstraints(1,
                2,
                0,
                0,
                GridBagConstraints.NORTH);
        GridBagConstraints barConstraints = WindowUtils.createConstraints(1,
                1,
                2,
                0,
                GridBagConstraints.NORTH);
        progressPanel.add(progressLabel, labelConstraints);
        progressPanel.add(progressBar, barConstraints);

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

    public void startProgress() {
        if(!Arrays.asList(getComponents()).contains(progressPanel)) {
            add(progressPanel, BorderLayout.CENTER);
        }

        progressLabel.setVisible(true);
        progressBar.setVisible(true);
        progressPanel.setVisible(true);
        repaint();
    }

    public void stopProgress() {
        if(Arrays.asList(getComponents()).contains(progressPanel)) {
            remove(progressPanel);
        }

        progressLabel.setVisible(false);
        progressBar.setVisible(false);
        progressPanel.setVisible(false);
    }

    public void setProgress(double estimatedTime, int currentEpoch, int totalEpochs) {
        this.estimatedTime = (int) estimatedTime;
        progressLabel.setText("Estimated time: " + this.estimatedTime + " minutes, "
                + currentEpoch + "/" + totalEpochs + " epochs finished");

        progressBar.setIndeterminate(false);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        double progress = ((double) currentEpoch / totalEpochs) * 100;
        progressBar.setValue((int) progress);
        progressBar.setString(progress + "%");
    }

    public void setProgress(int currentEpoch, int totalEpochs) {
        progressLabel.setText("Estimated time: " + this.estimatedTime + " minutes, "
                + currentEpoch + "/" + totalEpochs + " epochs finished");

        double progress = ((double) currentEpoch / totalEpochs) * 100;
        progressBar.setValue((int) progress);
        progressBar.setString(progress + "%");
    }

    public void setProgressLabel(String text) {
        progressLabel.setText(text);
        startProgress();
    }
}
