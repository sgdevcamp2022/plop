package smilegate.plop.auth.exception;

public class IncorrectVerificationCodeException extends RuntimeException {
    public IncorrectVerificationCodeException(String msg) {
        super(msg);
    }
}
