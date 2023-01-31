package smilegate.plop.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestDMDto {
    private String message_type;
    private String message_to;
    private String content;
}
