package smilegate.plop.chat.domain.chat;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
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
    private ObjectId objMessageId;
    private String roomId;
    private MessageType type;
    private String userId;
    private String content;
    private LocalDateTime createAt;

    @Builder
    public MessageCollection(MessageType type, String userId, String content, LocalDateTime createAt) {
        this.type = type;
        this.userId = userId;
        this.content = content;
        this.createAt = createAt;
    }
}
