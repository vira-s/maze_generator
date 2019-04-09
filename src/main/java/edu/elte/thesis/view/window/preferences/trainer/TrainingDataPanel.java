package edu.elte.thesis.view.window.preferences.trainer;

import edu.elte.thesis.view.window.preferences.FileLoaderPanel;
import edu.elte.thesis.view.window.utils.WindowUtils;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * @author Viktoria Sinkovics
 */
public class TrainingDataPanel extends JPanel {

    private static final int PANEL_WIDTH = 230;

    private static final int PANEL_HEIGHT = 530;

    private static final String LOAD_TRAINING_DATA_TEXT = "Load Training Data";

    private static final String GENERATE_TRAINING_DATA_TEXT = "Generate Training Data";

    private static final String TRAINING_DATA_FILE_TEXT = "File containing the training data: ";

    private JRadioButton generateMazeRadioButton;

    private JRadioButton loadMazeRadioButton;

    private FileLoaderPanel fileLoaderPanel;

    private MazeGenerationHandlerPanel generationFieldPanel;

    public TrainingDataPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBorder(WindowUtils.createTitledBorder("Training Data Preferences"));
        setLayout(new GridBagLayout());

        initRadioButtonOptions();
        initFileFields();
        initMazeGenerationFields();

        revalidate();
        repaint();
    }

    public JRadioButton getGenerateMazeRadioButton() {
        return generateMazeRadioButton;
    }

    public JRadioButton getLoadMazeRadioButton() {
        return loadMazeRadioButton;
    }

    public FileLoaderPanel getFileLoaderPanel() {
        return fileLoaderPanel;
    }

    public MazeGenerationHandlerPanel getGenerationFieldPanel() {
        return generationFieldPanel;
    }

    private void initRadioButtonOptions() {
        GridBagConstraints radioButtonConstraints = WindowUtils.createConstraints(1,
                1,
                0,
                0,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.NORTH);
        radioButtonConstraints.insets = new Insets(5, 0, 5, 0);

        initLoadButton();
        initGenerateButton();

        Font radioButtonFont = new Font(loadMazeRadioButton.getFont().getName(),
                Font.BOLD,
                13);
        loadMazeRadioButton.setFont(radioButtonFont);
        generateMazeRadioButton.setFont(radioButtonFont);

        WindowUtils.createButtonGroup(loadMazeRadioButton, generateMazeRadioButton);

        JPanel radioPanel = WindowUtils.createSmallUtilityPanel();
        radioPanel.add(loadMazeRadioButton);
        radioPanel.add(generateMazeRadioButton);

        add(radioPanel, radioButtonConstraints);
    }

    private void initLoadButton() {
        loadMazeRadioButton = new JRadioButton(LOAD_TRAINING_DATA_TEXT);
        loadMazeRadioButton.addActionListener(event -> {
            WindowUtils.disablePanel(generationFieldPanel);
            WindowUtils.enablePanel(fileLoaderPanel);

            revalidate();
            repaint();
        });
    }

    private void initGenerateButton() {
        generateMazeRadioButton = new JRadioButton(GENERATE_TRAINING_DATA_TEXT);
        generateMazeRadioButton.addActionListener(event -> {
            WindowUtils.enablePanel(generationFieldPanel);
            WindowUtils.disablePanel(fileLoaderPanel);

            revalidate();
            repaint();
        });
    }

    private void initFileFields() {
        fileLoaderPanel = new FileLoaderPanel(LOAD_TRAINING_DATA_TEXT,
                TRAINING_DATA_FILE_TEXT,
                1,
                0,
                0,
                0);

        GridBagConstraints fileFieldConstraints = WindowUtils.createConstraints(2,
                1,
                0,
                1,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        fileFieldConstraints.insets = new Insets(5, 0, 5, 0);

        add(fileLoaderPanel, fileFieldConstraints);
    }

    private void initMazeGenerationFields() {
        generationFieldPanel = new MazeGenerationHandlerPanel(GENERATE_TRAINING_DATA_TEXT,
                1,
                0,
                0,
                0);


        GridBagConstraints generationFieldsConstraints = WindowUtils.createConstraints(5,
                1,
                0,
                3,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.SOUTH);
        generationFieldsConstraints.insets = new Insets(5, 0, 5, 0);

        add(generationFieldPanel, generationFieldsConstraints);

        WindowUtils.disablePanel(generationFieldPanel);
    }

}
