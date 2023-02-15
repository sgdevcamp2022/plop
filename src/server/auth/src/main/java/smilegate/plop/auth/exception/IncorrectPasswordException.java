package smilegate.plop.auth.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String msg) {
        super(msg);
    }
}
