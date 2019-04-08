package edu.elte.thesis.view.window;

import edu.elte.thesis.controller.MazeController;
import edu.elte.thesis.model.cell.Wall;
import edu.elte.thesis.model.cell.WallPosition;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Viktoria Sinkovics
 */
public class MazeCellButton extends JButton {

    private final MazeController controller;

    private final Integer row;

    private final Integer column;

    private List<Wall> walls;

    public MazeCellButton(MazeController controller,
                          Integer column,
                          Integer row, double cellSize) {
        this.column = column;
        this.row = row;
        this.controller = controller;
        this.walls = Arrays.asList(
                new Wall(WallPosition.NORTH),
                new Wall(WallPosition.EAST),
                new Wall(WallPosition.SOUTH),
                new Wall(WallPosition.WEST));

        this.setEnabled(false);
        this.setBackground(Color.LIGHT_GRAY);
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    }

    public MazeCellButton(MazeController controller,
                          Integer column,
                          Integer row,
                          List<Wall> walls, double cellSize) {
        this.column = column;
        this.row = row;
        this.controller = controller;
        this.walls = walls.stream()
                .map(wall -> new Wall(wall.getPosition(), wall.isVisible()))
                .collect(Collectors.toList());

        this.setEnabled(false);
        this.setBackground(Color.LIGHT_GRAY);

        this.setBorder(BorderFactory.createMatteBorder(
                getWall(WallPosition.NORTH).isVisible() ? 1 : 0,
                getWall(WallPosition.WEST).isVisible()  ? 1 : 0,
                getWall(WallPosition.SOUTH).isVisible()  ? 1 : 0,
                getWall(WallPosition.EAST).isVisible()  ? 1 : 0,
                Color.BLACK));
    }

    public MazeController getController() {
        return controller;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getColumn() {
        return column;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    private Wall getWall(WallPosition wallPosition) {
        return this.walls.stream()
                .filter(w -> w.getPosition() == wallPosition)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing wall at {}" + wallPosition));
    }

}
