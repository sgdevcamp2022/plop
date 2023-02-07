package smilegate.plop.user.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import smilegate.plop.user.dto.BaseResponse;

@Data
@RequiredArgsConstructor
public class ResponseDto<D> extends BaseResponse {
    private final String result;
    private final String message;
    private final D data;
}
