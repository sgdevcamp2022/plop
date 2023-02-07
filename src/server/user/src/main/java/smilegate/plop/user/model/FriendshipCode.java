package smilegate.plop.user.model;


import lombok.Data;

public enum FriendshipCode {
    NONE(0),
    REQUESTED(1),
    ACCCEPTED(2),
    REJECTED(3),
    BLOCKED(9),

    ;

    private final int status;

    FriendshipCode(int status) {
        this.status = status;
    }
    public int value() {
        return this.status;
    }
}
