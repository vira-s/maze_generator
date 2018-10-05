package model.cell;

import model.exception.UnknownWallPositionException;

/**
 * @author Viktoria Sinkovics on 10/1/2018
 */
public enum WallPosition {

    NORTH,

    EAST,

    SOUTH,

    WEST;

    public WallPosition opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                throw new UnknownWallPositionException("Unknown wall position: " + this);
        }
    }
}
