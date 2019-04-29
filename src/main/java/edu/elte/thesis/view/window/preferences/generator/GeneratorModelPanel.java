package edu.elte.thesis.view.window.preferences.generator;

import edu.elte.thesis.controller.MazeController;
import edu.elte.thesis.utils.MazeGeneratorAlgorithm;
import edu.elte.thesis.view.event.GenerateWithAlgorithmButtonAction;
import edu.elte.thesis.view.event.GenerateWithVaeButtonAction;
import edu.elte.thesis.view.window.preferences.MazePreferenceTabbedPane;
import edu.elte.thesis.view.window.utils.WindowUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import java.awt.Color;
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

    private final MazePreferenceTabbedPane parent;

    private JPanel algorithmPanel;

    private JSpinner mazeSizeSpinner;

    private ArrayList<JRadioButton> algorithmNameRadioButtons;

    private JButton algorithmGenerateButton;

    private JPanel vaeLoaderPanel;

    private JSpinner vaeMazeSizeSpinner;

    private JCheckBox vaeDefaultModelCheckBox;

    private JButton vaeGenerateButton;

    public GeneratorModelPanel(MazePreferenceTabbedPane parent) {
        this.parent = parent;

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 35));

        initAlgorithmSelectorPanel();
        initVaePanel();

        revalidate();
        repaint();

        initDummyPanel();
    }

    public MazePreferenceTabbedPane getParent() {
        return parent;
    }

    public JPanel getVaeLoaderPanel() {
        return vaeLoaderPanel;
    }

    public JSpinner getVaeMazeSizeSpinner() {
        return vaeMazeSizeSpinner;
    }

    public JCheckBox getVaeDefaultModelCheckBox() {
        return vaeDefaultModelCheckBox;
    }

    public JPanel getAlgorithmPanel() {
        return algorithmPanel;
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

    public MazeController getController() {
        return parent.getController();
    }

    private void initAlgorithmSelectorPanel() {
        algorithmPanel = new JPanel();
        algorithmPanel.setPreferredSize(new Dimension(230, ALGO_PANEL_HEIGHT));
        algorithmPanel.setLayout(new GridLayout(0, 1));
        algorithmPanel.setBorder(WindowUtils.createTitledBorder("Generate with algorithm"));

        JLabel mazeSizeLabel = new JLabel("Size of the maze: ");
        mazeSizeSpinner = new JSpinner(WindowUtils.getMazeSizeSpinnerModel());

        algorithmPanel.add(mazeSizeLabel);
        algorithmPanel.add(mazeSizeSpinner);

        algorithmNameRadioButtons = new ArrayList<>();
        ButtonGroup radioButtonGroup = new ButtonGroup();

        MazeGeneratorAlgorithm.getSortedValues()
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


        algorithmGenerateButton = WindowUtils.createButton(GENERATE_BUTTON_TEXT, new GenerateWithAlgorithmButtonAction(this));
        add(algorithmGenerateButton, generateButtonConstraints);
    }

    private void initVaePanel() {
        vaeLoaderPanel = new JPanel();
        vaeLoaderPanel.setLayout(new GridLayout(3, 1));
        vaeLoaderPanel.setPreferredSize(new Dimension(230, 100));
        vaeLoaderPanel.setBorder(WindowUtils.createTitledBorder("Generate with CVAE",
                Color.BLACK,
                14,
                1,
                1,
                1,
                1));


        JLabel vaeMazeSizeLabel = new JLabel("Size of the maze: ");
        vaeMazeSizeSpinner = new JSpinner(WindowUtils.getMazeSizeSpinnerModel());
        vaeDefaultModelCheckBox = new JCheckBox("Generate with default model if present");

        vaeLoaderPanel.add(vaeMazeSizeLabel);
        vaeLoaderPanel.add(vaeMazeSizeSpinner);
        vaeLoaderPanel.add(vaeDefaultModelCheckBox);


        GridBagConstraints vaeFieldConstraints = WindowUtils.createConstraints(3,
                2,
                0,
                7,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        add(vaeLoaderPanel, vaeFieldConstraints);
        vaeFieldConstraints.insets = new Insets(5, 0, 10, 0);

        GridBagConstraints generateButtonConstraints = initGenerateButtonConstraints(10);

        vaeGenerateButton = WindowUtils.createButton(GENERATE_BUTTON_TEXT, new GenerateWithVaeButtonAction(this));
        add(vaeGenerateButton, generateButtonConstraints);
    }

    private GridBagConstraints initGenerateButtonConstraints(int gridy) {
        GridBagConstraints generateButtonConstraints = WindowUtils.createConstraints(1,
                1,
                1,
                gridy,
                GridBagConstraints.LAST_LINE_END);
        generateButtonConstraints.ipadx = 5;
        generateButtonConstraints.ipady = 5;
        generateButtonConstraints.insets = new Insets(5, 0, 10, 5);

        return generateButtonConstraints;
    }

    private void initDummyPanel() {
        JPanel dummyPanel = new JPanel();
        dummyPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 200));

        GridBagConstraints dummyConstraints = WindowUtils.createConstraints(8,
                2,
                0,
                11,
                GridBagConstraints.BOTH,
                GridBagConstraints.LAST_LINE_START);
        dummyConstraints.ipadx = 130;
        dummyConstraints.ipady = 250;

        add(dummyPanel, dummyConstraints);
    }
}
