package edu.elte.thesis.view.window.preferences.trainer;

import edu.elte.thesis.utils.MazeGeneratorAlgorithmName;
import edu.elte.thesis.view.window.utils.WindowUtils;
import org.springframework.util.Assert;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viktoria Sinkovics
 */
public class MazeGenerationHandlerPanel extends JPanel {

    private static final Integer PANEL_WIDTH = 230;

    private static final int PANEL_HEIGHT = 270;

    private JLabel algorithmSelectorLabel;

    private List<JCheckBox> algorithmNameCheckBoxes;

    private JLabel mazeCountToGenerateLabel;

    private JSpinner mazeCountToGenerateSpinner;

    private JLabel mazeSizeToGenerateLabel;

    private JSpinner mazeSizeToGenerateSpinner;

    /**
     * Constructor.
     *
     * @param borderTitle The title of the panel's border
     * @param top The size of the border at the top
     * @param left The size of the border on the left
     * @param bottom The size of the border at the top
     * @param right The size of the border on the right
     */
    public MazeGenerationHandlerPanel(String borderTitle, int top, int left, int bottom, int right) {
        Assert.notNull(borderTitle, "borderTitle should not be null.");

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new GridLayout(10, 1));

        setBorder(WindowUtils.createTitledBorder(borderTitle, Color.BLACK, 13, top, left, bottom, right));

        initMazeSizeFields();
        initMazeCountFields();
        initAlgorithmSelectorFields();
    }

    public JLabel getAlgorithmSelectorLabel() {
        return algorithmSelectorLabel;
    }

    public List<JCheckBox> getAlgorithmNameCheckBoxes() {
        return algorithmNameCheckBoxes;
    }

    public JLabel getMazeCountToGenerateLabel() {
        return mazeCountToGenerateLabel;
    }

    public JSpinner getMazeCountToGenerateSpinner() {
        return mazeCountToGenerateSpinner;
    }

    public JLabel getMazeSizeToGenerateLabel() {
        return mazeSizeToGenerateLabel;
    }

    public JSpinner getMazeSizeToGenerateSpinner() {
        return mazeSizeToGenerateSpinner;
    }

    private void initMazeSizeFields() {
        mazeSizeToGenerateLabel = new JLabel("Size of the mazes: ");
        mazeSizeToGenerateSpinner = new JSpinner(WindowUtils.getMazeSizeSpinnerModel());

        add(mazeSizeToGenerateLabel, BorderLayout.NORTH);
        add(mazeSizeToGenerateSpinner, BorderLayout.AFTER_LAST_LINE);
    }

    private void initMazeCountFields() {
        mazeCountToGenerateLabel = new JLabel("Number of the mazes: ");
        mazeCountToGenerateSpinner = new JSpinner(WindowUtils.getMazeCountSpinnerModel());

        JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor) mazeCountToGenerateSpinner.getEditor())
                .getTextField();
        spinnerTextField.setEditable(false);
        spinnerTextField.setBackground(Color.WHITE);

        add(mazeCountToGenerateLabel, BorderLayout.NORTH);
        add(mazeCountToGenerateSpinner, BorderLayout.AFTER_LAST_LINE);
    }

    private void initAlgorithmSelectorFields() {
        algorithmSelectorLabel = new JLabel("Select one or more algorithms: ");
        add(algorithmSelectorLabel, BorderLayout.AFTER_LINE_ENDS);

        algorithmNameCheckBoxes = new ArrayList<>();
        MazeGeneratorAlgorithmName.getSortedValues()
                .forEach(value -> {
                    JCheckBox jCheckBox = new JCheckBox(value.getShortName());
                    algorithmNameCheckBoxes.add(jCheckBox);
                    add(jCheckBox, BorderLayout.AFTER_LINE_ENDS);
                });
    }
}
