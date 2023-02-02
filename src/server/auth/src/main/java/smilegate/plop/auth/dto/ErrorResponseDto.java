package smilegate.plop.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import smilegate.plop.auth.exception.ErrorCode;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Builder
public class ErrorResponseDto {
//    private final int errorCode;
//    private final String message;
   private final int status;
   private final String errorCode;
   private final String description;
   private final String errorMsg;
}
