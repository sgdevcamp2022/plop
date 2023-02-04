package smilegate.plop.chat.dto.response;

import lombok.*;
import smilegate.plop.chat.dto.LastMessage;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RespMyChatRoom {
    private String room_id;
    private String title;
    private LastMessage last_message;
}
