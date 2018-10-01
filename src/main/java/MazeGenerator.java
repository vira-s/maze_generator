import model.Maze;

/**
 * Interface to collect the common functionality of the generators.
 *
 * @author Viktoria Sinkovics on 10/1/2018
 */
public interface MazeGenerator {

    Maze generate(int height, int width);

}
