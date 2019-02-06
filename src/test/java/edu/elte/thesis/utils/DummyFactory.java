package edu.elte.thesis.utils;

import edu.elte.thesis.model.cell.MazeCell;
import edu.elte.thesis.model.graph.CellNode;

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


    public static List<CellNode> getCyclicNodes() {
        CellNode first = new CellNode(new MazeCell(0, 0));
        CellNode second = new CellNode(new MazeCell(1, 0));
        CellNode third = new CellNode(new MazeCell(1, 1));
        CellNode fourth = new CellNode(new MazeCell(0, 1));

        first.makeRoot();
        first.setParent(fourth);
        first.addChild(second);

        second.setParent(first);
        second.addChild(third);

        third.setParent(second);
        third.addChild(fourth);

        fourth.setParent(third);
        fourth.addChild(first);

        return Arrays.asList(first, second, third, fourth);
    }

    public static List<CellNode> getAcyclicNodes() {
        CellNode first = new CellNode(new MazeCell(0, 0));
        CellNode second = new CellNode(new MazeCell(1, 0));
        CellNode third = new CellNode(new MazeCell(1, 1));
        CellNode fourth = new CellNode(new MazeCell(0, 1));

        first.makeRoot();
        first.addChild(second);

        second.setParent(first);
        second.addChild(third);

        third.setParent(second);
        third.addChild(fourth);

        fourth.setParent(third);

        return Arrays.asList(first, second, third, fourth);
    }

    public static List<CellNode> getDisconnectedNodes() {
        CellNode first = new CellNode(new MazeCell(0, 0));
        CellNode second = new CellNode(new MazeCell(1, 0));
        CellNode third = new CellNode(new MazeCell(1, 1));
        CellNode fourth = new CellNode(new MazeCell(0, 1));

        first.makeRoot();
        first.addChild(second);

        second.setParent(first);

        third.addChild(fourth);

        fourth.setParent(third);

        return Arrays.asList(first, second, third, fourth);
    }
}
