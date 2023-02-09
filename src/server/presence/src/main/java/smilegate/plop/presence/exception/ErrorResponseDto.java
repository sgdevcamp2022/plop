package smilegate.plop.presence.exception;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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