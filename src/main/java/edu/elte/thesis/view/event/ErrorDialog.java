package edu.elte.thesis.view.event;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * @author Viktoria Sinkovics
 */
public class ErrorDialog extends JDialog {

    private int answer;

    public ErrorDialog(Component parent, String text, String title) {
        answer = JOptionPane.showOptionDialog(
                parent,
                text,
                title,
                JOptionPane.OK_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                new String[] {"OK"},
                0);
    }

    public Integer getAnswer() {
        return answer;
    }
}
