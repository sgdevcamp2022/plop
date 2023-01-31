package smilegate.plop.chat.dto;

import lombok.Data;

@Data
public class RespMyChatRoom {
    private String room_id;
    private String title;

    private LastMessage last_message;
}
