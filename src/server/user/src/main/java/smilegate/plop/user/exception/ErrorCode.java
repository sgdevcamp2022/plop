package smilegate.plop.user.exception;

import smilegate.plop.user.dto.response.ErrorResponseDto;


public enum ErrorCode {
    INVALID_TOKEN(401, "AUTH-001", "토큰이 유효하지 않은 경우"),
    EXPIRED_TOKEN(401, "AUTH-002", "토큰이 만료된 경우"),
    MISSING_TOKEN(401, "AUTH-003", "토큰을 전달하지 않은 경우"),
    INCORRECT_TOKEN(401, "AUTH-004", "허용된 토큰이 아닌 경우"),
    NOT_ACCESS_TOKEN(400, "AUTH-005", "액세스 토큰이 아닌 경우"),
    NOT_EXISTED_REFRESH_TOKEN(404, "AUTH-006", "저장된 리프레쉬 토큰이 없는 경우"),
    UNAUTHORIZED(401, "AUTH-007", "인증에 실패한 경우"),
    WITHDRAWAL_USER(403, "AUTH-008", "탈퇴한 회원이 요청한 경우"),
    PASSWORD_NOT_CHANGED(400, "AUTH-009", "새 비밀번호로 바꿀 수 없는 경우"),
    INCORRECT_PASSWORD(401, "AUTH-010", "비밀번호가 일치하지 않는 경우"),
    INCORRECT_VERIFICATION_CODE(401, "AUTH-011", "이메일 인증 코드가 틀린 경우"),
    USER_NOT_FOUND(404, "USER-001", "해당 유저가 존재하지 않는 경우"),
    DUPLICATION_USER(409,"USER-002","해당 유저가 이미 존재하는 경우"),//409 confilct
    FAILED_FILE_STREAM(500,"USER-003","파일 전송 혹은 변환에 실패한 경우"),
    FRIENDSHIP_NOT_FOUND(404,"USER-004","서로 요청받은 친구 요청이 없는 경우"),
    DUPLICATED_FRIENDSHIP(400,"USER-005","이미 친구 요청을 받은 대상이거나, 친구인 경우");

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
                .status(this.status)
                .errorCode(this.code)
                .description(this.description)
                .errorMsg(msg)
                .build();
    }
}
