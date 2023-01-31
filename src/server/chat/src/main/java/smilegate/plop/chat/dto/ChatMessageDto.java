package smilegate.plop.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {

    private MessageType message_type;
    private String room_id;
    private String sender_id;
    private String content;
    private String message_to;
    private String message_id;
    private LocalDateTime created_at;

    @Builder
    public ChatMessageDto(MessageType message_type, String room_id, String sender_id, String content, String message_to) {
        this.message_type = message_type;
        this.room_id = room_id;
        this.sender_id = sender_id;
        this.content = content;
        this.message_to = message_to;
    }

    @Override
    public String toString() {
        return "Type: "+message_type+", message_to: "+message_to+", sender: " + sender_id+", Content: "+content;
    }
}
