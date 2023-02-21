package smilegate.plop.user.exception;

public class DuplicatedFriendshipException extends RuntimeException {
    public DuplicatedFriendshipException(String msg) {
        super(msg);
    }
}
