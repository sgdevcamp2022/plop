package smilegate.plop.push.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestMessage {
    String title;
    String body;
    List<String> target;
}
