package smilegate.plop.chat.config.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.dto.RoomMessageDto;
import smilegate.plop.chat.dto.response.RespRoomDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumers {
    private final SimpMessagingTemplate template;

    @KafkaListener(groupId = "${spring.kafka.chat-consumer.group-id}" ,topics="${kafka.topic.chat-name}")
    public void listenChat(ChatMessageDto chatMessageDto){
        template.convertAndSend("/chatting/topic/room/"+chatMessageDto.getRoom_id(), chatMessageDto);
    }

    @KafkaListener(groupId = "${spring.kafka.room-consumer.group-id}",topics = "${kafka.topic.room-name}", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupCreation(RoomMessageDto roomMessageDto){
        RespRoomDto respRoomDto = roomMessageDto.getRespRoomDto();
        for(String userId : roomMessageDto.getReceivers()){
            template.convertAndSend("/chatting/topic/new-room/"+userId,respRoomDto);
        }
    }
}
