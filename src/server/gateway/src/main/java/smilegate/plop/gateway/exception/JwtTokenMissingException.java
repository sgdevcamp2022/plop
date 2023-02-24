package smilegate.plop.gateway.exception;

import javax.naming.AuthenticationException;

public class JwtTokenMissingException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JwtTokenMissingException(String msg) {
        super (msg);
    }
}
