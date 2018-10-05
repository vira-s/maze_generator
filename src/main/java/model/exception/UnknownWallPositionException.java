package model.exception;

/**
 * @author Viktoria Sinkovics on 10/5/2018
 */
public class UnknownWallPositionException extends RuntimeException {

    public UnknownWallPositionException(String message) {
        super(message);
    }

}
