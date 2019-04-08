package edu.elte.thesis.utils;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Viktoria Sinkovics
 */
public enum MazeGeneratorAlgorithm {

    HUNT_AND_KILL("huntAndKill"),

    GROWING_TREE("growingTree"),

    RANDOM_WALK("randomWalk"),

    SIDEWINDER("sidewinder"),

    RECURSIVE_BACKTRACKER("recursiveBacktracker");

    private final String shortName;


    MazeGeneratorAlgorithm(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static Stream<MazeGeneratorAlgorithm> getSortedValues() {
        return Arrays.stream(MazeGeneratorAlgorithm.values())
                .sorted(Comparator.comparing(MazeGeneratorAlgorithm::getShortName));
    }

    public static Optional<MazeGeneratorAlgorithm> findByShortName(String shortName) {
        Assert.notNull(shortName, "shortName should not be null.");

        return Arrays.stream(MazeGeneratorAlgorithm.values())
                .filter(value -> Objects.equals(value.shortName, shortName))
                .findFirst();
    }

}
