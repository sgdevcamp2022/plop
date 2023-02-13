package smilegate.plop.gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import smilegate.plop.gateway.dto.ErrorResponseDto;

import java.nio.charset.StandardCharsets;

@Component
@Order(-1) // 내부 bean 보다 우선 순위를 높여 해당 빈이 동작하게 설정
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("In Exception Handler");

        ErrorCode errorCode;
        ErrorResponseDto errorResponseDto;
         if (ex.getClass() == MalformedJwtException.class || ex.getClass() == SignatureException.class
        || ex.getClass() == UnsupportedJwtException.class ) {
             errorCode = ErrorCode.INVALID_TOKEN;
             errorResponseDto = errorCode.toErrorResponseDto("유효하지 않은 토큰");
        } else if (ex.getClass() == ExpiredJwtException.class){
             errorCode = ErrorCode.EXPIRED_TOKEN;
             errorResponseDto = errorCode.toErrorResponseDto("만료된 토큰");
        } else if (ex.getClass() == JwtTokenMissingException.class) {
             errorCode = ErrorCode.MISSING_TOKEN;
             errorResponseDto = errorCode.toErrorResponseDto("토큰이 전달되지 않음");
         } else {
             errorResponseDto = null;
             ex.printStackTrace();
         }
         ObjectMapper mapper = new ObjectMapper();
        String result = null;
        try {
            result = mapper.writeValueAsString(errorResponseDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type","application/json");
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));

    }
}
