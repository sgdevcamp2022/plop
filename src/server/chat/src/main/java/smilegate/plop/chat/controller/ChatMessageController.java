package smilegate.plop.chat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.chat.config.kafka.Producers;
import smilegate.plop.chat.dto.APIMessage;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.exception.CustomAPIException;
import smilegate.plop.chat.exception.ErrorCode;
import smilegate.plop.chat.service.ChatMessageService;
import smilegate.plop.chat.service.ChatRoomService;
import smilegate.plop.chat.service.PushService;

import javax.validation.Valid;


@Tag(name="chat", description = "채팅 메시지 API")
@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/chatting")
public class ChatMessageController {
    private final Producers producers;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final PushService pushService;

    @MessageMapping("/chatting/pub")
    @Operation(summary = "웹소켓메시지 전송")
    public void sendSocketMessage(@Valid @RequestBody ChatMessageDto chatMessageDto){
        if(!chatRoomService.existsRoom(chatMessageDto.getRoom_id())){
            throw new CustomAPIException(ErrorCode.ROOM_NOT_FOUND_ERROR, "채팅방이 없음-"+chatMessageDto.getRoom_id());
        }
        ChatMessageDto savedMessage = chatMessageService.saveChatMessage(chatMessageDto);
        producers.sendMessage(savedMessage);

        // 비동기 푸시 알림
        pushService.pushMessageToUsers(chatMessageDto);
    }

    @Operation(summary = "메시지 전송")
    @PostMapping(value="/v1/message", consumes = "application/json",produces = "application/json")
    public void sendMessage(@Valid @RequestBody ChatMessageDto chatMessageDto){
        if(!chatRoomService.existsRoom(chatMessageDto.getRoom_id())){
            throw new CustomAPIException(ErrorCode.ROOM_NOT_FOUND_ERROR, "채팅방이 없음-"+chatMessageDto.getRoom_id());
        }
        ChatMessageDto savedMessage = chatMessageService.saveChatMessage(chatMessageDto);
        producers.sendMessage(savedMessage);

        // 비동기 푸시 알림
        pushService.pushMessageToUsers(chatMessageDto);
    }

    @Operation(summary = "채팅방 새 메시지 받기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅방 새 메시지 조회 성공", content = @Content(schema = @Schema(implementation = APIMessage.class)))})
    @GetMapping("/v1/new-message/{roomid}/{readMsgId}")
    public ResponseEntity<APIMessage> newMessagesAtRoom(@Parameter(description = "채팅방 id") @PathVariable(value = "roomid") String roomId,
                                                        @Parameter(description = "메세지 id") @PathVariable String readMsgId){
        APIMessage apiMessage = new APIMessage();
        apiMessage.setMessage(APIMessage.ResultEnum.success);
        apiMessage.setData(chatMessageService.getNewMessages(roomId, readMsgId));
        return new ResponseEntity<>(apiMessage, HttpStatus.OK);
    }

    @Operation(summary = "과거채팅보기(해당 채팅방내 전체 메시지 가져오기)", description = "내림차순으로 해당 채팅방의 전체 메세지를 조회합니다.")
    @GetMapping("/v1/history-message/{roomid}")
    public ResponseEntity<APIMessage> allMessagesAtRoom(@Parameter(description = "채팅방 id") @PathVariable(value = "roomid") String roomId){
        APIMessage apiMessage = new APIMessage();
        apiMessage.setMessage(APIMessage.ResultEnum.success);
        apiMessage.setData(chatMessageService.getAllMessagesAtRoom(roomId));
        return new ResponseEntity<>(apiMessage, HttpStatus.OK);
    }

    @Operation(summary = "채팅 Pagination", description = "내림차순으로 해당 채팅방 Pagination, 사이즈 N = 50 고정")
    @GetMapping("/v1/history")
    public ResponseEntity<APIMessage> chatMessagePagination(
            @Parameter(description = "채팅방 id") @RequestParam(name = "roomid") String roomId,
            @Parameter(description = "첫 페이지는 0부터 시작") @RequestParam(name = "page") int page){
        APIMessage apiMessage = new APIMessage();
        apiMessage.setMessage(APIMessage.ResultEnum.success);
        apiMessage.setData(chatMessageService.chatMessagePagination(roomId,page));
        return new ResponseEntity<>(apiMessage, HttpStatus.OK);
    }
}
