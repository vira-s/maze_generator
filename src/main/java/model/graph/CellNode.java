package model.graph;

import model.cell.MazeCell;
import model.cell.Wall;
import model.cell.WallPosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;

/**
 * Represents a node for a {@link MazeCell}.
 *
 * @author Viktoria Sinkovics
 */
public class CellNode extends Node<MazeCell> {

    private static final Logger LOGGER = LogManager.getLogger(CellNode.class);

    public CellNode(MazeCell entity) {
        super(entity);
    }

    public CellNode(Node<MazeCell> parent, MazeCell entity) {
        super(parent, entity);
    }

    public CellNode(Node<MazeCell> parent, List<Node<MazeCell>> children, MazeCell entity) {
        super(parent, children, entity);
    }

    public int getColumn() {
        return this.entity.getColumn();
    }

    public int getRow() {
        return this.entity.getRow();
    }

    public boolean isVisited() {
        return this.entity.isVisited();
    }

    public void markAsVisited() {
        this.entity.markAsVisited();
    }

    public boolean isNeighbourOf(MazeCell mazeCell) {
        return this.entity.isNeighbourOf(mazeCell);
    }

    public boolean isUpperNeighbourOf(CellNode nextCell) {
        return this.entity.isUpperNeighbourOf(nextCell.entity);
    }

    public boolean isLowerNeighbourOf(CellNode nextCell) {
        return this.entity.isLowerNeighbourOf(nextCell.entity);
    }

    public boolean isLeftNeighbourOf(CellNode nextCell) {
        return this.entity.isLeftNeighbourOf(nextCell.entity);
    }

    public boolean isRightNeighbourOf(CellNode nextCell) {
        return this.entity.isRightNeighbourOf(nextCell.entity);
    }

    public void removeWall(WallPosition wallPosition) {
        this.entity.removeWall(wallPosition);
    }

    public Wall getWallByPosition(WallPosition wallPosition) {
        return this.entity.getWallByPosition(wallPosition);
    }

    public void print(String prefix, boolean isTail) {
        String name = "(" + entity.getColumn() + "," + entity.getRow() + ")";

        System.out.println(prefix + (isTail ? "|__ " : "|-- ") + name);

        for (int i = 0; i < children.size() - 1; i++) {
            CellNode child = (CellNode) children.get(i);
            child.print(prefix + (isTail ? "    " : "|   "), false);
        }
        if (children.size() > 0) {
            CellNode child = (CellNode) children.get(children.size() - 1);
            child.print(prefix + (isTail ? "    " : "|   "), true);
        }
    }

    @Override
    public String toString() {
        return "CellNode{"
                + "root=" + root
                + ", parentCoordinates=("
                + (Objects.isNull(parent)
                    ? null
                    : parent.getEntity().getColumn() + ", " + parent.getEntity().getRow()) + ")"
                + ", entity=" + entity
                + '}';
    }
}
