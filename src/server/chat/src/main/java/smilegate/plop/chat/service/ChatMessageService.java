package smilegate.plop.chat.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.domain.chat.ChatMessageRepository;
import smilegate.plop.chat.domain.chat.MessageCollection;
import smilegate.plop.chat.dto.ChatMessageDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private static final int SIZE = 50;
    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    public ChatMessageDto saveChatMessage(ChatMessageDto chatMessageDto){

        MessageCollection messageCollection = chatMessageRepository.save(MessageCollection.builder()
                .type(chatMessageDto.getMessage_type())
                .roomId(chatMessageDto.getRoom_id())
                .senderId(chatMessageDto.getSender_id())
                .content(chatMessageDto.getContent())
                .createdAt(LocalDateTime.now())
                .build());

        return convertEntityToDto(messageCollection);
    }

    public List<ChatMessageDto> getNewMessages(String roomId, String readMsgId){
        List<MessageCollection> messageCollections = chatMessageRepository.getNewMessages(roomId, readMsgId);
        return messageCollections.stream().map(m -> convertEntityToDto(m)).collect(Collectors.toList());
    }

    public List<ChatMessageDto> getAllMessagesAtRoom(String roomId) {
        return chatMessageRepository.getAllMessagesAtRoom(roomId).stream().map(mc -> convertEntityToDto(mc)).collect(Collectors.toList());
    }

    public Page<ChatMessageDto> chatMessagePagination(String roomId, int page){
        Page<MessageCollection> messageCollectionPage = chatMessageRepository.findByRoomIdWithPagingAndFiltering(roomId, page, SIZE);
        Page<ChatMessageDto> chatMessageDtoPage = messageCollectionPage.map(new Function<MessageCollection, ChatMessageDto>() {
            @Override
            public ChatMessageDto apply(MessageCollection messageCollection) {
                return convertEntityToDto(messageCollection);
            }
        });

        return chatMessageDtoPage;
    }

    private ChatMessageDto convertEntityToDto(MessageCollection messageCollection) {
        return ChatMessageDto.builder()
                .room_id(messageCollection.getRoomId())
                .message_type(messageCollection.getType())
                .sender_id(messageCollection.getSenderId())
                .content(messageCollection.getContent())
                .created_at(messageCollection.getCreatedAt())
                .message_id(messageCollection.get_id())
                .build();
    }

}
