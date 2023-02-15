package smilegate.plop.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Schema(description = "그룹 채팅방 생성 요청")
@NoArgsConstructor
public class ReqGroupDto {
    @Schema(description = "요청하는 유저 id")
    private String creator;
    @Size(min = 2)
    @Schema(description = "요청받는 유저 id 리스트")
    private List<String> members;

    public ReqGroupDto(List<String> members) {
        this.members = members;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public List<String> getMembers() {
        return members;
    }
}
