package smilegate.plop.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import smilegate.plop.chat.domain.kafka.Producers;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.dto.MessageType;
import smilegate.plop.chat.service.ChatMessageService;
import smilegate.plop.chat.service.ChatRoomService;

@Slf4j
@RequiredArgsConstructor
@RestController()
//@RequestMapping("/chatting")
public class ChatMessageController {

    private final Producers producers;
    private final ChatRoomService chatRoomMongoService;
    private final ChatMessageService chatMessageService;

    // 1:1
    @PostMapping(value="/v1/dm-message", consumes = "application/json",produces = "application/json")
    public void sendMessage(@RequestBody ChatMessageDto chatMessageDto){
        // FIRST 이면 room 생성
        if(chatMessageDto.getMessage_type().equals(MessageType.FIRST)) {
            log.info("첫 메세지");
            chatMessageDto.setRoom_id(chatRoomMongoService.createDmRoom(chatMessageDto).getRoomId());
            log.info("룸 생성");
        }
        log.info("메시지 저장");
        ChatMessageDto savedMessage = chatMessageService.saveChatMessage(chatMessageDto);

        log.info("메시지 전송");
        producers.sendMessage(savedMessage);
    }

    // 그룹
    @PostMapping(value="/v1/group-message", consumes = "application/json",produces = "application/json")
    public void sendGroupMessage(@RequestBody ChatMessageDto chatMessageDto){
        log.info("메시지 저장");
        ChatMessageDto savedMessage = chatMessageService.saveChatMessage(chatMessageDto);

        log.info("메시지 전송");
        producers.sendGroupMessage(savedMessage);
    }

}
