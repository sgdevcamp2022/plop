package smilegate.plop.auth.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import smilegate.plop.auth.dto.BaseResponse;
import smilegate.plop.auth.exception.ErrorCode;

import java.time.LocalDateTime;

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
