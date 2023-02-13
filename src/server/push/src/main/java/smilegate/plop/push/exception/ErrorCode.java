package smilegate.plop.push.exception;


import smilegate.plop.push.dto.response.ErrorResponseDto;

public enum ErrorCode {
    UNAUTHORIZED(401, "AUTH-002", "인증에 실패한 경우"),
    WITHDRAWAL_USER(403, "AUTH-005", "탈퇴한 회원이 요청한 경우"),
    PASSWORD_NOT_CHANGED(400, "AUTH-006", "새 비밀번호로 바꿀 수 없는 경우"),
    INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우"),
    INTERNAL_SERVER_ERROR(500, "COMMON-002", "서버에서 처리할 수 없는 경우"),

    DUPLICATE_LOGIN_ID(400, "ACCOUNT-001", "계정명이 중복된 경우"),

    ACCOUNT_NOT_FOUND(404, "ACCOUNT-003", "계정을 찾을 수 없는 경우"),
    ROLE_NOT_EXISTS(403, "ACCOUNT-004", "권한이 부족한 경우"),
    TOKEN_NOT_EXISTS(404, "ACCOUNT-005", "해당 key의 인증 토큰이 존재하지 않는 경우"),

    ARTIST_NOT_FOUND(404, "ARTIST-001", "가수를 찾을 수 없는 경우"),

    SONG_NOT_FOUND(404, "SONG-001", "곡을 찾을 수 없는 경우"),

    CONTEST_INVALID_DATE(400, "CONTEST-001", "선정 곡 날짜가 적절치 않은 경우");

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
