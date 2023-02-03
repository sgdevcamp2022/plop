package smilegate.plop.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import smilegate.plop.chat.domain.room.Member;
import smilegate.plop.chat.domain.room.RoomType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RespRoomDto {
    private String room_id;
    private String title;
    private RoomType type;
    private List<Member> members;
    private List<String> managers;
    private LocalDateTime createdAt;
}
