package smilegate.plop.chat.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "유저 리스트")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestUsers {
    @Schema(description = "유저 id 리스트")
    private List<String> members;
}
