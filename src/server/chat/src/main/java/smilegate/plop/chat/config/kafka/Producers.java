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
import smilegate.plop.chat.dto.response.RespRoomDto;

// 토픽에 메시지전송(이벤트 발행)
@Slf4j
@Component
@RequiredArgsConstructor
public class Producers {
    private final KafkaTemplate<String, ChatMessageDto> kafkaTemplate;
    @Value("${kafka.topic.name}")
    private String topicDmName;

    private final KafkaTemplate<String, RespRoomDto> roomKafkaTemplate;
    @Value("${kafka.topic.room-name}")
    private String roomTopicName;

    public void sendMessage(ChatMessageDto chatMessageDto){
        log.info("토픽 : {}", topicDmName);

        ListenableFuture<SendResult<String, ChatMessageDto>> listenable = kafkaTemplate.send(topicDmName,chatMessageDto); // 보낼 메시지 포맷 변경 해야됨
        listenable.addCallback(new ListenableFutureCallback<SendResult<String, ChatMessageDto>>() {
            @Override
            public void onSuccess(SendResult<String, ChatMessageDto> result) {
                log.info("Sent message=[" + chatMessageDto.getContent() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + chatMessageDto.getContent() + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendRoomMessage(RespRoomDto respRoomDto){
        ListenableFuture<SendResult<String, RespRoomDto>> listenable = roomKafkaTemplate.send(roomTopicName,respRoomDto);
        listenable.addCallback(new ListenableFutureCallback<SendResult<String, RespRoomDto>>() {
            @Override
            public void onSuccess(SendResult<String, RespRoomDto> result) {
                log.info("Sent message=[" + respRoomDto.getRoom_id() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + respRoomDto.getRoom_id() + "] due to : " + ex.getMessage());
            }
        });
    }
}
