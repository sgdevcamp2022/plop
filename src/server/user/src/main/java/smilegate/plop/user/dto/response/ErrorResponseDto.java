package smilegate.plop.user.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import smilegate.plop.user.dto.BaseResponse;

@Data
@RequiredArgsConstructor
@Builder
public class ErrorResponseDto extends BaseResponse {
//    private final int errorCode;
//    private final String message;
   private final int status;
   private final String errorCode;
   private final String description;
   private final String errorMsg;
}
