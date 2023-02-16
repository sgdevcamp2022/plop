package smilegate.plop.user.exception;

public class NotAccessTokenException extends RuntimeException {
    public NotAccessTokenException(String msg) {
        super(msg);
    }
}
