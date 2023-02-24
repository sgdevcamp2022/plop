package smilegate.plop.chat.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import smilegate.plop.chat.dto.MessageType;

import java.time.LocalDateTime;

// embedded document 일 경우
@Getter
@NoArgsConstructor
//@QueryEntity
@Document(collection = "messages")
public class MessageCollection {
    @Id
    private String _id;
    private String roomId;
    private MessageType type;
    private String senderId;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public MessageCollection(MessageType type, String roomId, String senderId, String content, LocalDateTime createdAt) {
        this.type = type;
        this.roomId = roomId;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
