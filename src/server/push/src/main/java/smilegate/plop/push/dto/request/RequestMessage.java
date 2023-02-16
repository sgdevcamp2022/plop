package smilegate.plop.push.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestMessage {
    String title;
    String body;
    String roomId;
    List<String> target;
}
