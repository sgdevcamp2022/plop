package smilegate.plop.auth.exception;

public class PasswordNotChangedException extends RuntimeException {
    public PasswordNotChangedException(String msg) {
        super(msg);
    }
}
