package smilegate.plop.user.domain.friend;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.TypeDef;
import smilegate.plop.user.dto.response.ResponseFriend;

import javax.persistence.*;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "friend")

public class FriendEntity {
    @Id
    @Column(name = "sender_id", nullable = false, unique = true)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false, unique = true)
    private Long receiverId;

    @Column(name = "status")
    private Integer status;


    public ResponseFriend toResponseFriend(String senderId, String receiverId) {
        return ResponseFriend.builder()
                .sender(senderId)
                .receiver(receiverId)
                .build();
    }

}
