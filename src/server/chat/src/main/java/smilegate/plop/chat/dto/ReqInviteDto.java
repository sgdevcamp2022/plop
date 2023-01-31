package smilegate.plop.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ReqInviteDto {
    private String room_id;
    private List<String> members;

    public ReqInviteDto() {
    }

    public ReqInviteDto(String room_id, List<String> members) {
        this.room_id = room_id;
        this.members = members;
    }
}
