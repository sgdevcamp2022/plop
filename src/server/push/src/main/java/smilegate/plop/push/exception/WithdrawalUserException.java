package smilegate.plop.push.exception;

import javax.naming.AuthenticationException;

public class WithdrawalUserException extends AuthenticationException {

    public WithdrawalUserException(String msg) {
        super (msg);
    }
}
