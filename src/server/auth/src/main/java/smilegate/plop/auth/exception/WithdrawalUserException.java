package smilegate.plop.auth.exception;

import javax.naming.AuthenticationException;

public class WithdrawalUserException extends RuntimeException {

    public WithdrawalUserException(String msg) {
        super (msg);
    }
}
