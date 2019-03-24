package edu.elte.thesis.view.window.menu;

import edu.elte.thesis.view.event.ExitAction;
import edu.elte.thesis.view.event.ResetContentAction;
import edu.elte.thesis.view.window.MazeWindow;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

/**
 * @author Viktoria Sinkovics
 */
public class WindowMenuBar extends JMenuBar {

    private final MazeWindow parentWindow;

    private JMenu optionsMenu;

    private JMenuItem reset;

    private JMenuItem exit;

    public WindowMenuBar(MazeWindow parentWindow) {
        this.parentWindow = parentWindow;
        setPreferredSize(new Dimension(getWidth(), 35));
        initOptions();
        add(optionsMenu);
    }

    private void initExitGame() {
        exit = new WindowMenuItem("Exit", 'E', KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_MASK),
                new ExitAction());
    }

    private void initOptions() {
        optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('O');
        setOptionsPreferences();
    }

    private void setOptionsPreferences() {
        initResetMenu();
        optionsMenu.add(reset);

        optionsMenu.addSeparator();

        initExitGame();
        optionsMenu.add(exit);
    }

    private void initResetMenu() {
        reset = new WindowMenuItem("Reset Window Content", 'R',
                KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK),
                new ResetContentAction());
    }

}
