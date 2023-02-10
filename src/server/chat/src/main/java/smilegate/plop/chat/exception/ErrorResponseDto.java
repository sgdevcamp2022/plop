package smilegate.plop.chat.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "에러 처리")
@Data
@RequiredArgsConstructor
@Builder
public class ErrorResponseDto {
    private final int status;
    private final String errorCode;
    private final String description;
    private final String errorMsg;
    private final LocalDateTime timestamp;
}