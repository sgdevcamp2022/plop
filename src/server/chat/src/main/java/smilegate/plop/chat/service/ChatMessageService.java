package smilegate.plop.chat.service;

import org.springframework.stereotype.Service;
import smilegate.plop.chat.domain.chat.ChatMessageRepository;
import smilegate.plop.chat.domain.chat.MessageCollection;
import smilegate.plop.chat.dto.ChatMessageDto;

import java.time.LocalDateTime;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessageDto saveChatMessage(ChatMessageDto chatMessageDto){

        MessageCollection messageCollection = chatMessageRepository.save(MessageCollection.builder()
                .type(chatMessageDto.getMessage_type())
                .roomId(chatMessageDto.getRoom_id())
                .senderId(chatMessageDto.getSender_id())
                .content(chatMessageDto.getContent())
                .createAt(LocalDateTime.now())
                .build());

        return convertEntityToDto(messageCollection);
    }

    private ChatMessageDto convertEntityToDto(MessageCollection messageCollection) {
        return ChatMessageDto.builder()
                .room_id(messageCollection.getRoomId())
                .message_type(messageCollection.getType())
                .sender_id(messageCollection.getSenderId())
                .content(messageCollection.getContent())
                .created_at(messageCollection.getCreateAt())
                .message_id(messageCollection.get_id())
                .build();
    }

}
