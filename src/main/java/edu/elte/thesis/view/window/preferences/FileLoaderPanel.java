package edu.elte.thesis.view.window.preferences;

import edu.elte.thesis.view.window.utils.FileFieldsContainer;
import edu.elte.thesis.view.window.utils.WindowUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * @author Viktoria Sinkovics
 */
public class FileLoaderPanel extends JPanel {

    private static final Integer PANEL_WIDTH = 230;

    private static final int PANEL_HEIGHT = 70;

    private FileFieldsContainer fileFields;

    public FileLoaderPanel(String borderTitle,
                           String label,
                           int top,
                           int left,
                           int bottom,
                           int right) {
        Assert.isTrue(StringUtils.hasLength(borderTitle), "borderTitle should not be null.");
        Assert.isTrue(StringUtils.hasLength(label), "label should not be null.");

        initPanelPreferences(borderTitle, top, left, bottom, right);
        initFileFields(label);
    }

    public FileLoaderPanel(String borderTitle,
                           String label) {
        Assert.isTrue(StringUtils.hasLength(borderTitle), "borderTitle should not be null.");
        Assert.isTrue(StringUtils.hasLength(label), "label should not be null.");

        initPanelPreferences(borderTitle);
        initFileFields(label);
    }

    public FileLoaderPanel(String borderTitle,
                           String label,
                           int fontSize) {
        Assert.isTrue(StringUtils.hasLength(borderTitle), "borderTitle should not be null.");
        Assert.isTrue(StringUtils.hasLength(label), "label should not be null.");

        initPanelPreferences(borderTitle, fontSize);
        initFileFields(label);
    }

    public FileLoaderPanel(String label) {
        Assert.isTrue(StringUtils.hasLength(label), "label should not be null.");

        initPanelPreferences();
        initFileFields(label);
    }
    public FileFieldsContainer getFileFields() {
        return fileFields;
    }

    public JLabel getLabel() {
        return fileFields.getLabel();
    }

    public JTextField getFilePathField() {
        return fileFields.getFilePathField();
    }

    private void initPanelPreferences() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new GridLayout(2, 1));
    }

    private void initPanelPreferences(String borderTitle) {
        initPanelPreferences(borderTitle, 13);
    }

    private void initPanelPreferences(String borderTitle, int fontSize) {
        initPanelPreferences(borderTitle,
                fontSize,
                1,
                1,
                1,
                1);
    }

    private void initPanelPreferences(String borderTitle,
                                      int top,
                                      int left,
                                      int bottom,
                                      int right) {
        initPanelPreferences(borderTitle,
                13,
                top,
                left,
                bottom,
                right);
    }

    private void initPanelPreferences(String borderTitle,
                                      int fontSize,
                                      int top,
                                      int left,
                                      int bottom,
                                      int right) {
        initPanelPreferences();
        setBorder(WindowUtils.createTitledBorder(borderTitle,
                Color.BLACK,
                fontSize,
                top,
                left,
                bottom,
                right));
    }

    private void initFileFields(String label) {
        fileFields = new FileFieldsContainer(label);

        add(fileFields.getLabel(), BorderLayout.NORTH);
        add(fileFields.getFilePathField(), BorderLayout.CENTER);
    }

}
