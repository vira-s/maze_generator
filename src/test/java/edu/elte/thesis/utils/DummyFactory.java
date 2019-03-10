package edu.elte.thesis.utils;

import edu.elte.thesis.model.cell.MazeCell;
import edu.elte.thesis.model.cell.WallPosition;
import edu.elte.thesis.model.graph.CellNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains frequently used objects for testing.
 *
 * @author Viktoria Sinkovics
 */
public class DummyFactory {

    public static final int DEFAULT_COLUMNS = 10;

    public static final int DEFAULT_ROWS = 10;

    public static final List<String> ACYCLIC_MAZE = new ArrayList<>(Arrays.asList(
            "11111111",
            "11111111",
            "11111111",
            "11000111",
            "11110111",
            "11000111",
            "11111111",
            "11111111"
    ));

    public static final List<String> CYCLIC_MAZE = new ArrayList<>(Arrays.asList(
            "11111111",
            "11111111",
            "11111111",
            "11000111",
            "11010111",
            "11000111",
            "11111111",
            "11111111"
    ));

    public static final List<String> DISCONNECTED_MAZE = new ArrayList<>(Arrays.asList(
            "11111111",
            "11111111",
            "11111111",
            "11000111",
            "11111111",
            "11000111",
            "11111111",
            "11111111"
    ));

    public static List<CellNode> getCyclicNodes() {
        CellNode first = new CellNode(new MazeCell(0, 0));
        CellNode second = new CellNode(new MazeCell(1, 0));
        CellNode third = new CellNode(new MazeCell(0, 1));
        CellNode fourth = new CellNode(new MazeCell(1, 1));

        first.makeRoot();
        first.setParent(fourth);
        first.addChild(second);
        first.addChild(third);
        first.removeWall(WallPosition.EAST);
        first.removeWall(WallPosition.SOUTH);

        second.setParent(first);
        second.addChild(fourth);
        second.removeWall(WallPosition.WEST);
        second.removeWall(WallPosition.SOUTH);

        third.setParent(first);
        third.addChild(fourth);
        third.removeWall(WallPosition.EAST);
        third.removeWall(WallPosition.NORTH);

        fourth.setParent(third);
        fourth.removeWall(WallPosition.NORTH);
        fourth.removeWall(WallPosition.WEST);

        return Arrays.asList(first, second, third, fourth);
    }

    public static List<CellNode> getAcyclicNodes() {
        CellNode first = new CellNode(new MazeCell(0, 0));
        CellNode second = new CellNode(new MazeCell(1, 0));
        CellNode third = new CellNode(new MazeCell(1, 1));
        CellNode fourth = new CellNode(new MazeCell(0, 1));

        first.makeRoot();
        first.addChild(second);
        first.removeWall(WallPosition.EAST);

        second.setParent(first);
        second.addChild(third);
        second.removeWall(WallPosition.WEST);
        second.removeWall(WallPosition.SOUTH);

        third.setParent(second);
        third.addChild(fourth);
        third.removeWall(WallPosition.NORTH);
        third.removeWall(WallPosition.WEST);

        fourth.setParent(third);
        fourth.removeWall(WallPosition.EAST);

        return Arrays.asList(first, second, third, fourth);
    }

    public static List<CellNode> getDisconnectedNodes() {
        CellNode first = new CellNode(new MazeCell(0, 0));
        CellNode second = new CellNode(new MazeCell(1, 0));
        CellNode third = new CellNode(new MazeCell(1, 1));
        CellNode fourth = new CellNode(new MazeCell(0, 1));

        first.makeRoot();
        first.addChild(second);
        first.removeWall(WallPosition.EAST);

        second.setParent(first);
        second.removeWall(WallPosition.WEST);

        third.addChild(fourth);
        third.removeWall(WallPosition.WEST);

        fourth.setParent(third);
        fourth.removeWall(WallPosition.EAST);

        return Arrays.asList(first, second, third, fourth);
    }
}
