package smilegate.plop.auth.exception;

public class RedisNullException extends RuntimeException {
    public RedisNullException(String msg) {
        super(msg);
    }
}
