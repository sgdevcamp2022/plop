package smilegate.plop.chat.dto;

import lombok.*;

import smilegate.plop.chat.dto.response.RespRoomDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomMessageDto {
    private List<String> receivers;
    private RespRoomDto respRoomDto;
}
