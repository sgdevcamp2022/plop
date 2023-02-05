package smilegate.plop.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ReqInviteDto {
    private String room_id;
    private List<String> members;
}
