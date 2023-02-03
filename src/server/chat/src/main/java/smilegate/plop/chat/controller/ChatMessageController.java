package smilegate.plop.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smilegate.plop.chat.config.kafka.Producers;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.service.ChatMessageService;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/chatting")
public class ChatMessageController {
    private final Producers producers;
    private final ChatMessageService chatMessageService;

    @PostMapping(value="/v1/message", consumes = "application/json",produces = "application/json")
    public void sendMessage(@RequestBody ChatMessageDto chatMessageDto){
        log.info("메시지 저장");
        ChatMessageDto savedMessage = chatMessageService.saveChatMessage(chatMessageDto);

        log.info("메시지 전송");
        producers.sendMessage(savedMessage);
    }
}
