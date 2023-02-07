package smilegate.plop.user.exception;

import javax.naming.AuthenticationException;

public class WithdrawalUserException extends AuthenticationException {

    public WithdrawalUserException(String msg) {
        super (msg);
    }
}
