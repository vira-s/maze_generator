package edu.elte.thesis.view.window;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * @author Viktoria Sinkovics
 */
public class MazeBoardPanel extends JPanel {

    private static final int PANEL_WIDTH = 750;

    private static final int PANEL_HEIGHT = 750;

    private Integer columns;

    private Integer rows;

    public MazeBoardPanel(Integer columns, Integer rows) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.setLayout(new GridLayout(rows, columns));

        this.columns = columns;
        this.rows = rows;
    }

    public Integer getColumns() {
        return columns;
    }

    public Integer getRows() {
        return rows;
    }
}
