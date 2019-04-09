package edu.elte.thesis.view.window;

import edu.elte.thesis.model.Maze;
import edu.elte.thesis.model.cell.Wall;
import edu.elte.thesis.model.cell.WallPosition;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
public class MazeBoardPanel extends JPanel {

    private static final int PANEL_WIDTH = 750;

    private static final int PANEL_HEIGHT = 750;

    private static final int MAZE_AREA_SIZE = 650;

    private static final double X_OFFSET = 45.0;

    private static final double Y_OFFSET = 40.0;

    private Maze maze;

    public MazeBoardPanel(Maze maze) {
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.maze = maze;
        this.setLayout(new GridLayout());

    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke(new BasicStroke(2));
        double cellSize = (double) MAZE_AREA_SIZE / maze.getColumns();

        for (int row = 0; row < maze.getRows(); ++row) {
            for (int column = 0; column < maze.getColumns(); ++column) {
                drawCellLines(column, row, graphics2D, cellSize);
            }
        }
    }

    private void drawCellLines(int column,
                               int row,
                               Graphics2D graphics2D,
                               double cellSize) {
        List<WallPosition> visibleWalls = maze.getCellNodeByCoordinates(column, row)
                .getWalls()
                .stream()
                .filter(Wall::isVisible)
                .map(Wall::getPosition)
                .collect(Collectors.toList());

        for (WallPosition wallPosition : visibleWalls) {
            if (wallPosition == WallPosition.NORTH) {
                graphics2D.draw(createLine(column, row, column + 1, row, cellSize));

            } else if (wallPosition == WallPosition.EAST) {
                graphics2D.draw(createLine(column + 1, row, column + 1, row + 1, cellSize));

            } else if (wallPosition == WallPosition.SOUTH) {
                graphics2D.draw(createLine(column, row + 1, column + 1, row + 1, cellSize));

            } else if (wallPosition == WallPosition.WEST) {
                graphics2D.draw(createLine(column, row, column, row + 1, cellSize));
            }
        }
    }

    private Line2D.Double createLine(int col1, int row1, int col2, int row2, double cellSize) {
        double startX = X_OFFSET + (col1 * cellSize);
        double startY = Y_OFFSET + (row1 * cellSize);
        double endX = X_OFFSET + (col2 * cellSize);
        double endY = Y_OFFSET + (row2 * cellSize);

        return new Line2D.Double(startX, startY, endX, endY);
    }

}
