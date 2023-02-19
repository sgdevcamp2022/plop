package smilegate.plop.chat.config.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.dto.RoomMessageDto;
import smilegate.plop.chat.service.ChatMessageService;

// 토픽에 메시지전송(이벤트 발행)
@Slf4j
@Component
@RequiredArgsConstructor
public class Producers {
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    @Value("${kafka.topic.chat-name}")
    private String topicChatName;

    private final KafkaTemplate<String, RoomMessageDto> roomKafkaTemplate;
    @Value("${kafka.topic.room-name}")
    private String topicRoomName;
    private ChatMessageService chatMessageService;

    public void sendMessage(ChatMessageDto chatMessageDto){
        ListenableFuture<SendResult<String, ChatMessageDto>> listenable = kafkaTemplate.send(topicChatName,chatMessageDto); // 보낼 메시지 포맷 변경 해야됨
        listenable.addCallback(new ListenableFutureCallback<SendResult<String, ChatMessageDto>>() {
            @Override
            public void onSuccess(SendResult<String, ChatMessageDto> result) {
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[" + chatMessageDto.getContent() + "] due to : " + ex.getMessage());
                chatMessageService.deleteChat(chatMessageDto.getMessage_id());
                log.info("메시지 삭제= {}",chatMessageDto.getMessage_id());
            }
        });
    }

    public void sendRoomMessage(RoomMessageDto roomMessageDto){
        ListenableFuture<SendResult<String, RoomMessageDto>> listenable = roomKafkaTemplate.send(topicRoomName,roomMessageDto);
        listenable.addCallback(new ListenableFutureCallback<SendResult<String, RoomMessageDto>>() {
            @Override
            public void onSuccess(SendResult<String, RoomMessageDto> result) {
                log.info("Sent message=[" + roomMessageDto.getRespRoomDto().getRoom_id() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + roomMessageDto.getRespRoomDto().getRoom_id() + "] due to : " + ex.getMessage());
            }
        });
    }
}
