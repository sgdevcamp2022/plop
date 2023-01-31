package smilegate.plop.chat.dto;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ReqGroupDto {
    private String creator;
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
