package edu.elte.thesis.view.window.preferences.trainer;

import edu.elte.thesis.view.window.preferences.FileLoaderPanel;
import edu.elte.thesis.view.window.utils.WindowUtils;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * @author Viktoria Sinkovics
 */
public class ModelHandlerPanel extends JPanel {

    private static final Integer PANEL_WIDTH = 230;

    private static final Integer PANEL_HEIGHT = 120;

    private JRadioButton existingModel;

    private JRadioButton newModel;

    private FileLoaderPanel fileLoaderPanel;

    public ModelHandlerPanel() {
        setLayout(new GridLayout(0, 1));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBorder(WindowUtils.createTitledBorder("VAE Preferences"));

        initRadioButtons();
        initFileFields();
    }

    public JRadioButton getExistingModel() {
        return existingModel;
    }

    public JRadioButton getNewModel() {
        return newModel;
    }

    public FileLoaderPanel getFileLoaderPanel() {
        return fileLoaderPanel;
    }

    private void initRadioButtons() {
        existingModel = new JRadioButton("Load Existing Model");
        newModel = new JRadioButton("Train New Model");

        WindowUtils.createButtonGroup(existingModel, newModel);

        JPanel radioPanel = WindowUtils.createSmallUtilityPanel();

        radioPanel.add(existingModel);
        radioPanel.add(newModel);

        add(radioPanel, BorderLayout.NORTH);

    }

    private void initFileFields() {
        fileLoaderPanel = new FileLoaderPanel("Model's file: ");

        add(fileLoaderPanel, BorderLayout.CENTER);
    }
}
