package smilegate.plop.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastMessage {
    private String message_id;
    private String sender_id;
    private String content;
    private LocalDateTime last_modified_at;
}
