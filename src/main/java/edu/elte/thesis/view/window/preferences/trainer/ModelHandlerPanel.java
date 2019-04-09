package edu.elte.thesis.view.window.preferences.trainer;

import edu.elte.thesis.view.window.utils.WindowUtils;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * @author Viktoria Sinkovics
 */
public class ModelHandlerPanel extends JPanel {

    private static final Integer PANEL_WIDTH = 230;

    private static final Integer PANEL_HEIGHT = 100;

    private JRadioButton existingModelButton;

    private JRadioButton newModelButton;

    private JLabel epochSpinnerLabel;

    private JSpinner epochSpinner;

    private JCheckBox vaeDefaultModelCheckBox;

    public ModelHandlerPanel() {
        setLayout(new GridLayout(0, 1));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBorder(WindowUtils.createTitledBorder("VAE Preferences"));

        initRadioButtons();
        initEpochFields();
        initVaeDefaultCheckbox();
    }

    public JRadioButton getExistingModelButton() {
        return existingModelButton;
    }

    public JRadioButton getNewModelButton() {
        return newModelButton;
    }

    public JLabel getEpochSpinnerLabel() {
        return epochSpinnerLabel;
    }

    public JSpinner getEpochSpinner() {
        return epochSpinner;
    }

    public JCheckBox getVaeDefaultModelCheckBox() {
        return vaeDefaultModelCheckBox;
    }

    private void initRadioButtons() {
        existingModelButton = new JRadioButton("Train Existing Model");
        existingModelButton.addActionListener(event -> {
            vaeDefaultModelCheckBox.setEnabled(true);
            revalidate();
            repaint();
        });

        newModelButton = new JRadioButton("Train New Model");
        newModelButton.addActionListener(event -> {
            vaeDefaultModelCheckBox.setEnabled(false);
            revalidate();
            repaint();
        });

        WindowUtils.createButtonGroup(existingModelButton, newModelButton);

        JPanel radioPanel = WindowUtils.createSmallUtilityPanel();
        radioPanel.setPreferredSize(new Dimension(230, 30));

        radioPanel.add(existingModelButton);
        radioPanel.add(newModelButton);

        add(radioPanel, BorderLayout.NORTH);

    }

    private void initVaeDefaultCheckbox() {
        vaeDefaultModelCheckBox = new JCheckBox("Use default model if present");
        add(vaeDefaultModelCheckBox, BorderLayout.CENTER);
    }

    private void initEpochFields() {
        epochSpinnerLabel = new JLabel("Training epochs:");
        epochSpinner = new JSpinner(WindowUtils.getEpochSpinnerModel());

        JPanel epochPanel = WindowUtils.createSmallUtilityPanel();
        epochPanel.setPreferredSize(new Dimension(230, 30));

        epochPanel.add(epochSpinnerLabel, BorderLayout.NORTH);
        epochPanel.add(epochSpinner, BorderLayout.SOUTH);

        add(epochPanel, BorderLayout.SOUTH);
    }

}
