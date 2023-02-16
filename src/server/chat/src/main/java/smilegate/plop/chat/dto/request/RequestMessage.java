package smilegate.plop.chat.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestMessage {
    String title;
    String body;
    String roomId;
    List<String> target;
}
