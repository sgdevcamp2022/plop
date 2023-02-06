package smilegate.plop.chat.dto.response;

import lombok.*;
import smilegate.plop.chat.domain.room.Member;
import smilegate.plop.chat.dto.LastMessage;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RespMyChatRoom {
    private String room_id;
    private String title;
    private List<Member> members;
    private LastMessage last_message;
}
