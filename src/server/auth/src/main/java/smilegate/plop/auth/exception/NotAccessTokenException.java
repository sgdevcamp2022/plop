package smilegate.plop.auth.exception;

public class NotAccessTokenException extends RuntimeException {
    public NotAccessTokenException(String msg) {
        super(msg);
    }
}
