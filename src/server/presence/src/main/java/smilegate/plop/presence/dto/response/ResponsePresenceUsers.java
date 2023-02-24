package smilegate.plop.presence.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "접속한 유저 리스트")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePresenceUsers {
    @Schema(description = "접속한 친구 id 리스트")
    private List<String> members;
}
