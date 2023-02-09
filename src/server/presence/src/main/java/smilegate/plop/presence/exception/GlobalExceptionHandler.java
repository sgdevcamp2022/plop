package smilegate.plop.presence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomAPIException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomAPIException(CustomAPIException exception, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(exception.getStatus()).errorCode(exception.getCode())
                .description(exception.getDescription()).errorMsg(exception.getErrorMsg())
                .timestamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtTokenIncorrectStructureException.class)
    public ResponseEntity<ErrorResponseDto> handleBlogAPIException(JwtTokenIncorrectStructureException exception, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = ErrorCode.JWT_PARSING_FAILED.toErrorResponseDto("JWT 파싱 실패");
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
}
