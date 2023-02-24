package smilegate.plop.user.domain.friend;

import javax.persistence.Column;
import java.io.Serializable;

public class FriendIdEntity implements Serializable {
    @Column(name = "sender_id")
    private long senderId;

    @Column(name = "receiver_id")
    private long receiverId;
}
