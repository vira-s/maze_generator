package edu.elte.thesis.view.window.utils;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.swing.JLabel;
import javax.swing.JTextField;

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
        // TODO Add default text to the text field without using non-maven 3rd party libsu
        // org.jdesktop.xswingx.PromptSupport.init("Enter filename/path here", Color.BLACK, Color.WHITE, filePathField);
        // org.jdesktop.xswingx.PromptSupport.setFontStyle(Font.ITALIC, filePathField);
    }

    public JLabel getLabel() {
        return label;
    }

    public JTextField getFilePathField() {
        return filePathField;
    }
}
