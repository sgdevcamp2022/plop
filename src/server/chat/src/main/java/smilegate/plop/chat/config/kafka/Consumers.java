package smilegate.plop.chat.config.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.dto.MessageType;
import smilegate.plop.chat.dto.response.RespRoomDto;
import smilegate.plop.chat.service.ChatRoomService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumers {
    private final SimpMessagingTemplate template;
    private final ChatRoomService chatRoomService;

    @KafkaListener(groupId = "${spring.kafka.chat-consumer.group-id}" ,topics="${kafka.topic.chat-name}")
    public void listenChat(ChatMessageDto chatMessageDto){
        if(chatMessageDto.getMessage_type()==MessageType.FIRST){
            RespRoomDto respRoomDto = chatRoomService.getChatRoomInfo(chatMessageDto.getRoom_id());
            /**
             * 상대방 userid 찾기
             *  if 인덱스 0 이 보낸사람id랑 다르면 0이 상대방 userid, 보낸사람이랑 같으면 1이 상대방 userid
             */
            String userid = !respRoomDto.getMembers().get(0).getUserId().equals(chatMessageDto.getSender_id()) ?
                    respRoomDto.getMembers().get(0).getUserId() : respRoomDto.getMembers().get(1).getUserId();
            template.convertAndSend("/chatting/topic/new-room/"+userid,respRoomDto);
        }
        log.info("채팅방: {}, 보낸사람: {}, 메시지: {}", chatMessageDto.getRoom_id(), chatMessageDto.getSender_id(), chatMessageDto.getContent());
        template.convertAndSend("/chatting/topic/room/"+chatMessageDto.getRoom_id(), chatMessageDto);
    }

    /**
     * 자신을 제외한 룸 멤버들에게 방생성 정보 전달
     * @param respRoomDto
     */
    @KafkaListener(groupId = "${spring.kafka.room-consumer.group-id}",topics = "${kafka.topic.room-name}", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupCreation(RespRoomDto respRoomDto){
        List<String> userIdList = respRoomDto.getMembers().stream().map(m -> m.getUserId()).collect(Collectors.toList());
        userIdList.remove(respRoomDto.getManagers().get(0));
        for(String userId : userIdList){
            template.convertAndSend("/chatting/topic/new-room/"+userId,respRoomDto);
        }
    }
}
