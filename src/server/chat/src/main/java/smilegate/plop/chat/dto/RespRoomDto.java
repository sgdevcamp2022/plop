package smilegate.plop.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import smilegate.plop.chat.domain.room.Member;

import java.util.List;

@Data
@AllArgsConstructor
public class RespRoomDto {
    private String room_id;
    private String title;
    private List<Member> members;
    private List<String> managers;
}
