package smilegate.plop.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Schema(description = "채팅메시지")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatMessageDto {
    @Schema(description = "메시지 타입")
    private MessageType message_type;
    @NotEmpty
    @Schema(description = "채팅방 id")
    private String room_id;
    @NotEmpty
    @Schema(description = "보낸 유저 id")
    private String sender_id;
    @NotBlank
    @Schema(description = "내용")
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
