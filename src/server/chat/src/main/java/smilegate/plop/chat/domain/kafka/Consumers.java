package smilegate.plop.chat.domain.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import smilegate.plop.chat.dto.ChatMessageDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumers {
    private final SimpMessagingTemplate template;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}" ,topics="${kafka.topic.dm.name}")
    public void listenDM(ChatMessageDto chatMessageDto){
        log.info("채팅방: {}, 보낸사람: {}, 메시지: {}", chatMessageDto.getRoom_id(), chatMessageDto.getSender_id(), chatMessageDto.getContent());
        template.convertAndSend("/chatting/queue/room/"+chatMessageDto.getRoom_id(), chatMessageDto);
    }

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}" ,topics="${kafka.topic.group.name}")
    public void listenGroupMessage(ChatMessageDto chatMessageDto){
        log.info("채팅방: {}, 보낸사람: {}, 메시지: {}", chatMessageDto.getRoom_id(), chatMessageDto.getSender_id(), chatMessageDto.getContent());
        template.convertAndSend("/chatting/topic/room/"+chatMessageDto.getRoom_id(), chatMessageDto);
    }
}
