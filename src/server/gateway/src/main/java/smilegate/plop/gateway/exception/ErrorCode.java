package smilegate.plop.gateway.exception;


import lombok.Data;
import smilegate.plop.gateway.dto.ErrorResponseDto;

public enum ErrorCode {
    INVALID_TOKEN(401, "AUTH-001", "토큰이 유효하지 않은 경우"),
    EXPIRED_TOKEN(401, "AUTH-002", "토큰이 만료된 경우"),
    MISSING_TOKEN(401, "AUTH-003", "토큰을 전달하지 않은 경우");

    private final int status;
    private final String code;
    private final String description;

    ErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
    public ErrorResponseDto toErrorResponseDto(String msg) {
        return ErrorResponseDto
                .builder()
                .errorCode(this.code)
                .status(this.status)
                .description(this.description)
                .errorMsg(msg)
                .build();
    }
}
