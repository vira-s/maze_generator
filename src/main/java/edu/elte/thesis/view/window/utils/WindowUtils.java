package edu.elte.thesis.view.window.utils;

import org.springframework.util.Assert;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.Arrays;

/**
 * Contains utility methods used by the views.
 *
 * @author Viktoria Sinkovics
 */
public class WindowUtils {

    public static GridBagConstraints createConstraints(int gridheight,
                                                       int gridwidth,
                                                       int gridx,
                                                       int gridy,
                                                       int fill,
                                                       int anchor) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridheight = gridheight;
        constraints.gridwidth = gridwidth;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.fill = fill;
        constraints.anchor = anchor;

        return constraints;
    }

    public static GridBagConstraints createConstraints(int gridheight,
                                                       int gridwidth,
                                                       int gridx,
                                                       int gridy,
                                                       int anchor) {
        return createConstraints(gridheight,
                gridwidth,
                gridx,
                gridy,
                GridBagConstraints.VERTICAL,
                anchor);
    }

    public static JButton createButton(Color foregroundColor,
                                       Color backgroundColor) {
        Assert.notNull(foregroundColor, "foregroundColor should not be null.");
        Assert.notNull(backgroundColor, "backgroundColor should not be null.");

        JButton button = new JButton();
        button.setForeground(foregroundColor);
        button.setBackground(backgroundColor);

        return button;

    }

    public static JButton createButton(String text, AbstractAction action) {
        JButton button = createButton(new Color(0, 100, 0),
                new Color(0, 100, 0, 50));

        Font submitFont = new Font(button.getFont().getName(),
                Font.BOLD,
                button.getFont().getSize());
        button.setFont(submitFont);

        button.setAction(action);
        button.setText(text);

        return button;
    }

    public static void createButtonGroup(JRadioButton... radioButtons) {
        Assert.notNull(radioButtons, "radioButtons should not be null.");

        ButtonGroup radioGroup = new ButtonGroup();
        Arrays.stream(radioButtons).forEach(radioGroup::add);
        radioGroup.setSelected(radioButtons[0].getModel(), true);
    }

    public static Border createTitledBorder(String borderTitle) {
        return createTitledBorder(borderTitle, Color.BLACK, 14, 1, 1, 1, 1);
    }

    public static Border createTitledBorder(String borderTitle, Color color) {
        return createTitledBorder(borderTitle, color, 14, 1, 1, 1, 1);
    }

    public static Border createTitledBorder(String borderTitle, Color color, int titleSize) {
        return createTitledBorder(borderTitle, color, titleSize, 1, 1, 1, 1);
    }

    public static Border createTitledBorder(String borderTitle,
                                            Color color,
                                            int titleSize,
                                            int top,
                                            int left,
                                            int bottom,
                                            int right) {
        Font titleFont = new Font(Font.SANS_SERIF, Font.BOLD, titleSize);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createMatteBorder(top, left, bottom, right, color),
                borderTitle,
                TitledBorder.LEADING,
                TitledBorder.DEFAULT_POSITION,
                titleFont);
        titledBorder.setTitleColor(color);
        return titledBorder;
    }

    public static JPanel createSmallUtilityPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 1));
        panel.setPreferredSize(new Dimension(230, 40));

        return panel;
    }

    public static SpinnerModel getMazeSizeSpinnerModel() {
        return new SpinnerNumberModel(5, 5, 100, 1);
    }

    public static SpinnerModel getMazeCountSpinnerModel() {
        return new SpinnerNumberModel(10000, 10000, 500000, 1000);
    }

    public static SpinnerModel getEpochSpinnerModel() {
        return new SpinnerNumberModel(20, 5, 1000, 1);
    }

    public static void enablePanel(JPanel panel) {
        Arrays.stream(panel.getComponents())
                .forEach(component -> component.setEnabled(true));

        Border border = panel.getBorder();
        if (border instanceof TitledBorder) {
            TitledBorder titledBorder = (TitledBorder) panel.getBorder();
            titledBorder.setTitleColor(Color.BLACK);
        }
    }

    public static void disablePanel(JPanel panel) {
        Arrays.stream(panel.getComponents())
                .forEach(component -> component.setEnabled(false));

        Border border = panel.getBorder();
        if (border instanceof TitledBorder) {
            TitledBorder titledBorder = (TitledBorder) panel.getBorder();
            titledBorder.setTitleColor(Color.GRAY);
        }
    }
}
