package edu.elte.thesis.view.window.menu;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * @author Viktoria Sinkovics
 */
public class WindowMenuItem extends JMenuItem {

    public WindowMenuItem(String text, char mnemonic, KeyStroke keyStroke, Action action) {
        this.setAction(action);
        this.setBasics(text, mnemonic, keyStroke);
    }

    private void setBasics(String text, char mnemonic, KeyStroke keyStroke) {
        this.setText(text);
        this.setMnemonic(mnemonic);
        this.setAccelerator(keyStroke);
    }

}
