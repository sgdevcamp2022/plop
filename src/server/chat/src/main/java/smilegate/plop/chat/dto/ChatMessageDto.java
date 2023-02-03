package smilegate.plop.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatMessageDto {
    private MessageType message_type;
    private String room_id;
    private String sender_id;
    private String content;
    private String message_id;
    private LocalDateTime created_at;

    @Builder
    public ChatMessageDto(String message_id, MessageType message_type, String room_id, String sender_id, String content,LocalDateTime created_at) {
        this.message_id = message_id;
        this.message_type = message_type;
        this.room_id = room_id;
        this.sender_id = sender_id;
        this.content = content;
        this.created_at = created_at;
    }
}
