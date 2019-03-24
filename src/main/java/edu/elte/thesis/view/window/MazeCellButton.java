package edu.elte.thesis.view.window;

import edu.elte.thesis.controller.MazeController;
import edu.elte.thesis.model.cell.Wall;
import edu.elte.thesis.model.cell.WallPosition;
import org.springframework.util.Assert;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 * @author Viktoria Sinkovics
 */
public class MazeCellButton extends JButton {

    private final MazeController controller;

    private final Integer row;

    private final Integer column;

    private List<Wall> walls;

    public MazeCellButton(MazeController controller, Integer column, Integer row) {
        this.column = column;
        this.row = row;
        this.controller = controller;
        this.walls = Arrays.asList(
                new Wall(WallPosition.NORTH),
                new Wall(WallPosition.EAST),
                new Wall(WallPosition.SOUTH),
                new Wall(WallPosition.WEST));

        this.setEnabled(false);
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
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

    public void removeWallAt(WallPosition... wallPositions) {
        Assert.notNull(wallPositions, "wallPositions should not be null.");
        List<WallPosition> positions = Arrays.asList(wallPositions);

        this.walls.stream()
                .filter(wall -> positions.contains(wall.getPosition()))
                .forEach(Wall::setInvisible);

        this.setBorder(BorderFactory.createMatteBorder(
                positions.contains(WallPosition.NORTH) ? 0 : 2,
                positions.contains(WallPosition.WEST) ? 0 : 2,
                positions.contains(WallPosition.SOUTH) ? 0 : 2,
                positions.contains(WallPosition.EAST) ? 0 : 2,
                Color.BLACK));
    }
}
