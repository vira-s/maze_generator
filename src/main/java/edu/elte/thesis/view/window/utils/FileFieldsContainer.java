package edu.elte.thesis.view.window.utils;

import org.jdesktop.xswingx.PromptSupport;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;

/**
 * @author Viktoria Sinkovics
 */
public class FileFieldsContainer {

    private JLabel label;

    private JTextField filePathField;

    public FileFieldsContainer(String label) {
        Assert.isTrue(StringUtils.hasLength(label), "label should not be null.");

        this.label = new JLabel(label);

        filePathField = new JTextField();
        PromptSupport.init("Enter filename/path here", Color.BLACK, Color.WHITE, filePathField);
        PromptSupport.setFontStyle(Font.ITALIC, filePathField);
    }

    public JLabel getLabel() {
        return label;
    }

    public JTextField getFilePathField() {
        return filePathField;
    }
}
