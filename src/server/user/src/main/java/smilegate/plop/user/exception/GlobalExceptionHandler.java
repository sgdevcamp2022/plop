package smilegate.plop.user.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import smilegate.plop.user.dto.response.ErrorResponseDto;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.USER_NOT_FOUND.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(FriendshipNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleFriendshipNotFoundException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.FRIENDSHIP_NOT_FOUND.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(FileStreamException.class)
    public ResponseEntity<ErrorResponseDto> handleFileException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.FAILED_FILE_STREAM.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponseDto);
    }
    @ExceptionHandler(NotAccessTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessTokenException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.NOT_ACCESS_TOKEN.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
    @ExceptionHandler({SignatureException.class, MalformedJwtException.class,
            UnsupportedJwtException.class,IllegalArgumentException.class, ExpiredJwtException.class
    })
    public ResponseEntity<ErrorResponseDto> handleJwtException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.INCORRECT_TOKEN.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }
}
