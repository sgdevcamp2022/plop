package smilegate.plop.auth.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import smilegate.plop.auth.dto.BaseResponse;

@Data
@RequiredArgsConstructor
public class ResponseDto<D> extends BaseResponse {
    private final String result;
    private final String message;
    private final D data;
}
