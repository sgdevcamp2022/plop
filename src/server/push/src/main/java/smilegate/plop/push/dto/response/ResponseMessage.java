package smilegate.plop.push.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseMessage {
    private final String result;
    private final String title;
    private final String body;
}
