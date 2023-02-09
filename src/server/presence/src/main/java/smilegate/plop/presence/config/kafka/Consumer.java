package smilegate.plop.presence.config.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import smilegate.plop.presence.dto.PresenceUserDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {
    private final SimpMessagingTemplate template;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${kafka.topic.name}")
    public void listenMessage(PresenceMessage presenceMessage){
        PresenceUserDto presenceUserDto = PresenceUserDto.builder()
                .user_id(presenceMessage.getUser_id()).status(presenceMessage.getStatus()).build();
        for(String userId : presenceMessage.getFriends()) template.convertAndSend("/presence/user-sub/"+userId, presenceUserDto);
    }
}
