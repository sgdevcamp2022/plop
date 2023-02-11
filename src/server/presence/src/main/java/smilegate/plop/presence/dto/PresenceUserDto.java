package smilegate.plop.presence.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;



@Schema(description = "사용자 접속 상태 정보")
@Getter
@Setter
@NoArgsConstructor
public class PresenceUserDto {
    @Schema(description = "자신 id")
    private String user_id;
    @Schema(description = "접속상태: online, offline")
    private String status;

    @Builder
    public PresenceUserDto(String user_id, String status) {
        this.user_id = user_id;
        this.status = status;
    }
}
