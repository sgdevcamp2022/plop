package smilegate.plop.chat.dto.response;

import lombok.*;
import smilegate.plop.chat.domain.room.Member;
import smilegate.plop.chat.domain.room.RoomType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RespRoomDto {
    private String room_id;
    private String title;
    private RoomType type;
    private List<Member> members;
    private List<String> managers;
    private LocalDateTime createdAt;
}
