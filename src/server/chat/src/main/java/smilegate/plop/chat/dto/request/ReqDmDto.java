package smilegate.plop.chat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqDmDto {
    private String creator;
    private String message_to;
}
