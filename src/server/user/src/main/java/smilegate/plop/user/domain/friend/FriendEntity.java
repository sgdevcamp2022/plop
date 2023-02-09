package smilegate.plop.user.domain.friend;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.TypeDef;
import smilegate.plop.user.dto.response.ResponseFriend;

import javax.persistence.*;
import java.io.Serializable;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@Entity(name = "friend")
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "friend")
@IdClass(FriendIdEntity.class)
public class FriendEntity {
    @Id
    @Column(name = "sender_id", nullable = false)
    private Long senderId;
    @Id
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;
//    @Column(name = "sender_email", nullable = false)
//    private Long senderEmail;
//    @Column(name = "receiver_email", nullable = false)
//    private Long receiverEmail;

    @Column(name = "status")
    private Integer status;


    public ResponseFriend toResponseFriend(String senderId, String receiverId) {
        return ResponseFriend.builder()
                .sender(senderId)
                .receiver(receiverId)
                .build();
    }

}
