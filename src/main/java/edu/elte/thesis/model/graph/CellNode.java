package edu.elte.thesis.model.graph;

import edu.elte.thesis.model.cell.MazeCell;
import edu.elte.thesis.model.cell.Wall;
import edu.elte.thesis.model.cell.WallPosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return entity.getColumn();
    }

    public int getRow() {
        return entity.getRow();
    }

    public List<Wall> getWalls() {
        return entity.getWalls();
    }

    public boolean isVisited() {
        return entity.isVisited();
    }

    public void markAsVisited() {
        entity.markAsVisited();
    }

    public boolean isNeighbourOf(MazeCell mazeCell) {
        return entity.isNeighbourOf(mazeCell);
    }

    public boolean isUpperNeighbourOf(CellNode nextCell) {
        return entity.isUpperNeighbourOf(nextCell.entity);
    }

    public boolean isLowerNeighbourOf(CellNode nextCell) {
        return entity.isLowerNeighbourOf(nextCell.entity);
    }

    public boolean isLeftNeighbourOf(CellNode nextCell) {
        return entity.isLeftNeighbourOf(nextCell.entity);
    }

    public boolean isRightNeighbourOf(CellNode nextCell) {
        return entity.isRightNeighbourOf(nextCell.entity);
    }

    public void removeWall(WallPosition wallPosition) {
        entity.removeWall(wallPosition);
    }

    public Wall getWallByPosition(WallPosition wallPosition) {
        return entity.getWallByPosition(wallPosition);
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
                + ", childrenCoordinates=["
                + (Objects.isNull(children) || children.isEmpty()
                    ? null
                    : children.stream()
                            .map(child -> "(" + child.getEntity().getColumn() + ", " + child.getEntity().getRow() + ")")
                            .collect(Collectors.joining(", ")))
                + "], entity=" + entity
                + '}';
    }
}
