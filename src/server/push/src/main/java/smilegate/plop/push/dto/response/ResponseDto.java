package smilegate.plop.push.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseDto<D> {
    private final String result;
    private final String message;
    private final D data;
}
