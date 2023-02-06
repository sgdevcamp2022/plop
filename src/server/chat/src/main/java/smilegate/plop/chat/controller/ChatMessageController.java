package smilegate.plop.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.chat.config.kafka.Producers;
import smilegate.plop.chat.dto.APIMessage;
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

    @GetMapping("/v1/history-message/{roomid}")
    public ResponseEntity<APIMessage> allMessagesAtRoom(@PathVariable(value = "roomid")String roomId){
        APIMessage apiMessage = new APIMessage();
        apiMessage.setMessage(APIMessage.ResultEnum.success);
        apiMessage.setData(chatMessageService.getAllMessagesAtRoom(roomId));

        return new ResponseEntity<>(apiMessage, HttpStatus.OK);
    }

    @GetMapping("/v1/history")
    public ResponseEntity<APIMessage> chatMessagePagination(
            @RequestParam(name = "roomid") String roomId,
            @RequestParam(name = "page") int page){
        APIMessage apiMessage = new APIMessage();
        apiMessage.setMessage(APIMessage.ResultEnum.success);
        apiMessage.setData(chatMessageService.chatMessagePagination(roomId,page));

        return new ResponseEntity<>(apiMessage, HttpStatus.OK);
    }
}
