package smilegate.plop.chat.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public enum ErrorCode {
    DM_MEMBER_ERROR(4001,"CHATTING-001","상대방id가 있어야함"),
    GROUP_MEMBER_SIZE_ERROR(4002,"CHATTING-002","그룹멤버는 3명이상이여야함"),
    ROOM_NOT_FOUND_ERROR(4003,"CHATTING-003","해당 채팅방이 없음"),
    JWT_PARSING_FAILED(4201, "CHATTING-201", "인증에 실패한 경우");

    private final int status;
    private final String code;
    private final String description;

    ErrorCode(int status, String code, String description){
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
                .timestamp(LocalDateTime.now())
                .build();
    }
}
