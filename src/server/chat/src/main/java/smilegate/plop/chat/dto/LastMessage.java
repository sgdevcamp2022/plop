package smilegate.plop.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LastMessage {
    private String message_id;
    private String sender_id;
    private String content;
    private LocalDateTime created_at;
}
