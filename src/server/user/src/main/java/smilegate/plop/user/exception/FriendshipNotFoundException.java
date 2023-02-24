package smilegate.plop.user.exception;

public class FriendshipNotFoundException extends RuntimeException {
    public FriendshipNotFoundException(String msg) {
        super(msg);
    }
}
