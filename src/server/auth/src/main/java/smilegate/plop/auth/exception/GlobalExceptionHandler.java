package smilegate.plop.auth.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.bouncycastle.openssl.PasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import smilegate.plop.auth.dto.response.ErrorResponseDto;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.USER_NOT_FOUND.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateUserException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.DUPLICATION_USER.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDto);
    }
    @ExceptionHandler(WithdrawalUserException.class)
    public ResponseEntity<ErrorResponseDto> handleWithdrawalUserException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.WITHDRAWAL_USER.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
    }
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponseDto> handleIncorrectPasswordException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.INCORRECT_PASSWORD.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }
    @ExceptionHandler(NotAccessTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessTokenException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.NOT_ACCESS_TOKEN.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
    @ExceptionHandler(RedisNullException.class)
    public ResponseEntity<ErrorResponseDto> handleRedisNullException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.NOT_EXISTED_REFRESH_TOKEN.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDto);
    }
    @ExceptionHandler(PasswordNotChangedException.class)
    public ResponseEntity<ErrorResponseDto> handlePasswordNotChangedException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.PASSWORD_NOT_CHANGED.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }@ExceptionHandler(IncorrectVerificationCodeException.class)
    public ResponseEntity<ErrorResponseDto> handleVerificationCodeException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.INCORRECT_VERIFICATION_CODE.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }
    @ExceptionHandler({SignatureException.class, MalformedJwtException.class,
            UnsupportedJwtException.class,IllegalArgumentException.class, ExpiredJwtException.class
    })
    public ResponseEntity<ErrorResponseDto> handleJwtException(Exception e) {
        ErrorResponseDto errorResponseDto = ErrorCode.INCORRECT_TOKEN.toErrorResponseDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponseDto);
    }
}
