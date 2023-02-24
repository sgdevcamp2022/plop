package smilegate.plop.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "초대 요청")
@Setter
@Getter
public class ReqInviteDto {
    @Schema(description = "채팅방 id")
    private String room_id;
    @Schema(description = "본인 제외, 초대할 사람 리스트")
    private List<String> members;
}
