package smilegate.plop.chat.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReqReadMessage {
    private String room_id;
    private String user_id;
    private String message_id;
}
