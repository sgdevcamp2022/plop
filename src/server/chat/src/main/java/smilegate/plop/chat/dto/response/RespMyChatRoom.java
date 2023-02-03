package smilegate.plop.chat.dto.response;

import lombok.Data;
import smilegate.plop.chat.dto.LastMessage;

@Data
public class RespMyChatRoom {
    private String room_id;
    private String title;
    private LastMessage last_message;
}
