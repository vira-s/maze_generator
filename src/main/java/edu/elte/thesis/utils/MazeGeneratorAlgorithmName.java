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
public enum MazeGeneratorAlgorithmName {

    HUNT_AND_KILL("huntAndKill"),

    GROWING_TREE("growingTree"),

    RANDOM_WALK("randomWalk"),

    SIDEWINDER("sidewinder"),

    RECURSIVE_BACKTRACKER("recursiveBacktracker");

    private final String shortName;

    MazeGeneratorAlgorithmName(String shortName) {
        this.shortName = shortName;
    }

    public static Stream<MazeGeneratorAlgorithmName> getSortedValues() {
        return Arrays.stream(MazeGeneratorAlgorithmName.values())
                .sorted(Comparator.comparing(MazeGeneratorAlgorithmName::getShortName));
    }

    public String getShortName() {
        return shortName;
    }

    public Optional<MazeGeneratorAlgorithmName> findByShortName(String shortName) {
        Assert.notNull(shortName, "shortName should not be null.");

        return Arrays.stream(MazeGeneratorAlgorithmName.values())
                .filter(value -> Objects.equals(value.shortName, shortName))
                .findFirst();
    }

}
