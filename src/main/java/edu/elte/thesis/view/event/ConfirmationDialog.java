package edu.elte.thesis.view.event;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * @author Viktoria Sinkovics
 */
public class ConfirmationDialog extends JDialog {

    private String[] options = {"Yes", "No"};

    private int answer;

    public ConfirmationDialog(Component parent, String text, String title) {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        answer = JOptionPane.showOptionDialog(parent, text, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE, null, options, options[0]);
    }

    public int getAnswer() {
        return answer;
    }
}
