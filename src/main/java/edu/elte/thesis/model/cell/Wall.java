package edu.elte.thesis.model.cell;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Represents one wall of a {@link MazeCell}.
 *
 * @author Viktoria Sinkovics
 */
public class Wall {

    private final WallPosition position;

    private boolean visible;

    public Wall(WallPosition position) {
        this.position = position;
        this.visible = true;
    }

    public Wall(WallPosition position, boolean visible) {
        this.position = position;
        this.visible = visible;
    }

    public WallPosition getPosition() {
        return position;
    }

    public boolean isVisible() {
        return visible;
    }

    public Wall setInvisible() {
        Assert.isTrue(this.visible, "Wall is already invisible: " + this);

        this.visible = false;
        return this;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Wall wall = (Wall) other;
        return visible == wall.visible
                && position == wall.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, visible);
    }

    @Override
    public String toString() {
        return "Wall{"
                + "position=" + position
                + ", visible=" + visible
                + '}';
    }
}
