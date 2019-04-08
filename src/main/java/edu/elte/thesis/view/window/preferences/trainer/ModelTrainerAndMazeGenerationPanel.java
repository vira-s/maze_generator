package edu.elte.thesis.view.window.preferences.trainer;

import edu.elte.thesis.controller.MazeController;
import edu.elte.thesis.view.event.ResetContentAction;
import edu.elte.thesis.view.event.SubmitButtonAction;
import edu.elte.thesis.view.window.preferences.MazePreferenceTabbedPane;
import edu.elte.thesis.view.window.utils.WindowUtils;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * @author Viktoria Sinkovics
 */
public class ModelTrainerAndMazeGenerationPanel extends JPanel {

    private static final Integer PANEL_WIDTH = 240;

    private static final Integer PANEL_HEIGHT = 800;

    private final MazePreferenceTabbedPane parent;

    private TrainingDataPanel trainingDataPanel;
    private ModelHandlerPanel generatorModelHandlerPanel;

    private JButton resetButton;
    private JButton submitButton;

    public ModelTrainerAndMazeGenerationPanel(MazePreferenceTabbedPane parent) {
        this.parent = parent;

        setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        initMazeGenerationComponents();

        revalidate();
        repaint();
    }

    public TrainingDataPanel getTrainingDataPanel() {
        return trainingDataPanel;
    }

    public ModelHandlerPanel getGeneratorModelHandlerPanel() {
        return generatorModelHandlerPanel;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getSubmitButton() {
        return submitButton;
    }

    public MazeController getController() {
        return parent.getController();
    }

    private void initMazeGenerationComponents() {
        initTrainingDataPanel();
        initGeneratorModelFields();
        initFinalizerButtons();

        revalidate();
    }

    private void initTrainingDataPanel() {
        trainingDataPanel = new TrainingDataPanel();
        GridBagConstraints trainingDataPanelConstraints = WindowUtils.createConstraints(7,
                2,
                0,
                0,
                GridBagConstraints.NORTH);
        trainingDataPanelConstraints.insets = new Insets(0, 0, 5, 0);

        add(trainingDataPanel, trainingDataPanelConstraints);
    }

    private void initGeneratorModelFields() {
        GridBagConstraints generatorModelConstraints = WindowUtils.createConstraints(2,
                2,
                0,
                7,
                GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        generatorModelConstraints.insets = new Insets(5, 0, 5, 0);

        generatorModelHandlerPanel = new ModelHandlerPanel();

        add(generatorModelHandlerPanel, generatorModelConstraints);
    }

    private void initFinalizerButtons() {
        GridBagConstraints resetButtonConstraints = WindowUtils.createConstraints(1,
                1,
                0,
                9,
                GridBagConstraints.LAST_LINE_START);
        resetButtonConstraints.ipadx = 10;
        resetButtonConstraints.ipady = 5;
        resetButtonConstraints.insets = new Insets(10, 30, 5, 5);

        GridBagConstraints submitButtonConstraints = WindowUtils.createConstraints(1,
                1,
                1,
                9,
                GridBagConstraints.LAST_LINE_START);
        submitButtonConstraints.ipadx = 15;
        submitButtonConstraints.ipady = 5;
        submitButtonConstraints.insets = new Insets(10, 5, 5, 10);

        initSubmitButton();
        initResetButton();

        add(resetButton, resetButtonConstraints);
        add(submitButton, submitButtonConstraints);
    }

    private void initSubmitButton() {
        submitButton = WindowUtils.createSubmitButton();

        submitButton.setAction(new SubmitButtonAction(this));

        submitButton.setText("Submit");
    }

    private void initResetButton() {
        resetButton = WindowUtils.createResetButton();
        resetButton.setAction(new ResetContentAction());
        resetButton.setText("Reset");
    }

}
