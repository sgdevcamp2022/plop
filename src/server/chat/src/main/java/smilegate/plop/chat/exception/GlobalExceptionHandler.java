package smilegate.plop.chat.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtTokenIncorrectStructureException.class)
    public ResponseEntity<ErrorResponseDto> handleBlogAPIException(JwtTokenIncorrectStructureException exception, WebRequest webRequest)
    {
        ErrorResponseDto errorResponseDto = ErrorCode.JWT_PARSING_FAILED.toErrorResponseDto("JWT 파싱 실패");
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
}
