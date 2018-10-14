package model.cell;

import exception.UnknownWallPositionException;

/**
 * @author Viktoria Sinkovics
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
