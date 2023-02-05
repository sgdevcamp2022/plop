package smilegate.plop.chat.domain.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.dto.MessageType;
import smilegate.plop.chat.dto.response.RespRoomDto;
import smilegate.plop.chat.service.ChatRoomService;

@Slf4j
@Component
@RequiredArgsConstructor
public class Consumers {
    private final SimpMessagingTemplate template;
    private final ChatRoomService chatRoomService;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}" ,topics="${kafka.topic.name}")
    public void listenDM(ChatMessageDto chatMessageDto){
        if(chatMessageDto.getMessage_type()==MessageType.FIRST){
            RespRoomDto respRoomDto = chatRoomService.getChatRoomInfo(chatMessageDto.getRoom_id());
            String userid = "";
            /** 상대방 userid 찾기
             *  if 인덱스 0 이 보낸사람id랑 다르면 0이 상대방 userid, 보낸사람이랑 같으면 1이 상대방 userid
             */
            userid = !respRoomDto.getMembers().get(0).getUserId().equals(chatMessageDto.getSender_id()) ?
                    respRoomDto.getMembers().get(0).getUserId() : respRoomDto.getMembers().get(1).getUserId();
            template.convertAndSend("/chatting/topic/new-room/"+userid,respRoomDto);
        }
        log.info("채팅방: {}, 보낸사람: {}, 메시지: {}", chatMessageDto.getRoom_id(), chatMessageDto.getSender_id(), chatMessageDto.getContent());
        template.convertAndSend("/chatting/topic/room/"+chatMessageDto.getRoom_id(), chatMessageDto);
    }
}
