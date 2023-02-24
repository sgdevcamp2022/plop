package smilegate.plop.chat.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.UnexpectedTypeException;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomAPIException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomAPIException(CustomAPIException exception, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .status(exception.getStatus()).errorCode(exception.getCode())
                .description(exception.getDescription()).errorMsg(exception.getErrorMsg())
                .timestamp(LocalDateTime.now()).build();
        log.error("ErrorCode: {}, roomid: {}","ROOM_NOT_FOUND_ERROR", errorResponseDto.getErrorMsg());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtTokenIncorrectStructureException.class)
    public ResponseEntity<ErrorResponseDto> handleBlogAPIException(JwtTokenIncorrectStructureException exception, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = ErrorCode.JWT_PARSING_FAILED.toErrorResponseDto("JWT 파싱 실패");
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<ErrorResponseDto> handleUnexpectedTypeException(UnexpectedTypeException exception, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = ErrorCode.REQUEST_PARAM_INVALID.toErrorResponseDto(exception.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = ErrorCode.REQUEST_PARAM_INVALID.toErrorResponseDto(exception.getMessage());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
}
