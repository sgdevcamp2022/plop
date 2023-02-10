package smilegate.plop.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "1:1 방생성 요청")
@Setter
@Getter
public class ReqDmDto {
    @Schema(description = "요청하는 유저 id")
    private String creator;
    @Schema(description = "요청받는 유저 id")
    private String message_to;
}
