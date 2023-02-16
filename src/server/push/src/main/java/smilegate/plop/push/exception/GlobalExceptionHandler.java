package smilegate.plop.push.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import smilegate.plop.push.dto.response.ErrorResponseDto;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.USER_NOT_FOUND.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }

    @ExceptionHandler(NotAccessTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessTokenException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.NOT_ACCESS_TOKEN.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
    @ExceptionHandler(PushException.class)
    public ResponseEntity<ErrorResponseDto> handleMessagingException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.FAILED_TO_SEND_MESSAGE.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
    @ExceptionHandler(PushFormatException.class)
    public ResponseEntity<ErrorResponseDto> handleMessagingFormatException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.INVALID_MESSAGE_FORMAT.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorResponseDto);
    }
}
