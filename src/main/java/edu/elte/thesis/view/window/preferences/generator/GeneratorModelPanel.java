package edu.elte.thesis.view.window.preferences.generator;

import edu.elte.thesis.utils.MazeGeneratorAlgorithmName;
import edu.elte.thesis.view.window.preferences.FileLoaderPanel;
import edu.elte.thesis.view.window.utils.WindowUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

/**
 * @author Viktoria Sinkovics
 */
public class GeneratorModelPanel extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(GeneratorModelPanel.class);

    private static final Integer PANEL_WIDTH = 240;

    private static final int PANEL_HEIGHT = 700;

    private static final int ALGO_PANEL_HEIGHT = 140;

    private static final String GENERATE_BUTTON_TEXT = "Generate";

    private FileLoaderPanel vaeLoaderPanel;

    private JPanel algorithmPanel;

    private JLabel mazeSizeLabel;

    private JSpinner mazeSizeSpinner;

    private ArrayList<JRadioButton> algorithmNameRadioButtons;

    private JButton algorithmGenerateButton;

    private JButton vaeGenerateButton;

    public GeneratorModelPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        initAlgorithmSelectorPanel();
        initFilePanel();

        revalidate();
        repaint();

        initDummyPanel();
    }

    public FileLoaderPanel getVaeLoaderPanel() {
        return vaeLoaderPanel;
    }

    public JPanel getAlgorithmPanel() {
        return algorithmPanel;
    }

    public JLabel getMazeSizeLabel() {
        return mazeSizeLabel;
    }

    public JSpinner getMazeSizeSpinner() {
        return mazeSizeSpinner;
    }

    public ArrayList<JRadioButton> getAlgorithmNameRadioButtons() {
        return algorithmNameRadioButtons;
    }

    public JButton getAlgorithmGenerateButton() {
        return algorithmGenerateButton;
    }

    public JButton getVaeGenerateButton() {
        return vaeGenerateButton;
    }

    private void initAlgorithmSelectorPanel() {
        algorithmPanel = new JPanel();
        algorithmPanel.setPreferredSize(new Dimension(230, ALGO_PANEL_HEIGHT));
        algorithmPanel.setLayout(new GridLayout(0, 1));
        algorithmPanel.setBorder(WindowUtils.createTitledBorder("Generate with algorithm"));

        mazeSizeLabel = new JLabel("Size of the mazes: ");
        mazeSizeSpinner = new JSpinner(WindowUtils.getMazeSizeSpinnerModel());

        algorithmPanel.add(mazeSizeLabel);
        algorithmPanel.add(mazeSizeSpinner);

        algorithmNameRadioButtons = new ArrayList<>();
        ButtonGroup radioButtonGroup = new ButtonGroup();

        MazeGeneratorAlgorithmName.getSortedValues()
                .forEach(value -> {
                    JRadioButton radioButton = new JRadioButton(value.getShortName());

                    algorithmNameRadioButtons.add(radioButton);
                    radioButtonGroup.add(radioButton);
                    algorithmPanel.add(radioButton);
                });


        GridBagConstraints algorithmPanelConstraints = WindowUtils.createConstraints(6,
                2,
                0,
                0,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.NORTH);
        algorithmPanelConstraints.insets = new Insets(5, 0, 0, 0);

        add(algorithmPanel, algorithmPanelConstraints);

        GridBagConstraints generateButtonConstraints = initGenerateButtonConstraints(6);
        algorithmGenerateButton = new JButton(GENERATE_BUTTON_TEXT);
        add(algorithmGenerateButton, generateButtonConstraints);
    }

    private void initFilePanel() {
        vaeLoaderPanel = new FileLoaderPanel("Generate with Model", "Model's file: ", 14);

        GridBagConstraints fileFieldsConstraints = WindowUtils.createConstraints(2,
                2,
                0,
                7,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        add(vaeLoaderPanel, fileFieldsConstraints);
        fileFieldsConstraints.insets = new Insets(5, 0, 10, 0);

        GridBagConstraints generateButtonConstraints = initGenerateButtonConstraints(9);

        vaeGenerateButton = new JButton(GENERATE_BUTTON_TEXT);
        add(vaeGenerateButton, generateButtonConstraints);
    }

    private GridBagConstraints initGenerateButtonConstraints(int gridy) {
        GridBagConstraints generateButtonConstraints = WindowUtils.createConstraints(1,
                1,
                1,
                gridy,
                GridBagConstraints.LAST_LINE_END);
        generateButtonConstraints.ipadx = 15;
        generateButtonConstraints.ipady = 10;
        generateButtonConstraints.insets = new Insets(5, 0, 10, 5);

        return generateButtonConstraints;
    }

    private void initDummyPanel() {
        JPanel dummyPanel = new JPanel();
        dummyPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 300));

        GridBagConstraints dummyConstraints = WindowUtils.createConstraints(10,
                2,
                0,
                10,
                GridBagConstraints.BOTH,
                GridBagConstraints.LAST_LINE_START);
        dummyConstraints.ipadx = 230;
        dummyConstraints.ipady = 250;

        add(dummyPanel, dummyConstraints);
    }
}
