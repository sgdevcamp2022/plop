package smilegate.plop.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.domain.chat.ChatMessageRepository;
import smilegate.plop.chat.domain.chat.MessageCollection;
import smilegate.plop.chat.domain.room.Member;
import smilegate.plop.chat.domain.room.RoomCollection;
import smilegate.plop.chat.domain.room.RoomIdCreator;
import smilegate.plop.chat.domain.room.RoomRepository;
import smilegate.plop.chat.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ChatRoomService {
    private final RoomRepository roomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomService(RoomRepository roomRepository, ChatMessageRepository chatMessageRepository) {
        this.roomRepository = roomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public RoomCollection createDmRoom(ChatMessageDto chatMessageDto){
        List<Member> members = new ArrayList<>();
        members.add(new Member(chatMessageDto.getSender_id(), LocalDateTime.now()));
        members.add(new Member(chatMessageDto.getMessage_to(), LocalDateTime.now()));

        return roomRepository.save(RoomCollection.builder()
                .roomId(chatMessageDto.getRoom_id())
                .title(chatMessageDto.getMessage_to())
                .members(members)
                .managers(Collections.singletonList(chatMessageDto.getSender_id()))
                .build());
    }


    public RespRoomDto createGroup(ReqGroupDto reqGroupDto){
        List<Member> members = new ArrayList<>();
        String title = convertMembersListToString(reqGroupDto.getMembers());

        reqGroupDto.getMembers().forEach(m -> members.add(new Member(m,LocalDateTime.now())));
        members.add(new Member(reqGroupDto.getCreator(), LocalDateTime.now()));

        RoomCollection savedRoom = roomRepository.save(RoomCollection.builder()
                .title(title)
                .members(members)
                .managers(Collections.singletonList(reqGroupDto.getCreator()))
                .roomId(RoomIdCreator.createRoomId())
                .build()
        );

        return new RespRoomDto(savedRoom.getRoomId(),savedRoom.getTitle(),savedRoom.getMembers(),savedRoom.getManagers());
    }

    public boolean inviteMembers(ReqInviteDto reqInviteDto){
        long count = roomRepository.inviteMembers(reqInviteDto).getModifiedCount();
        if (count >= 1) {
            return true;
        }
        return false;
    }

    public APIMessage findMyRoomsByUserId(String userId){
        List<RoomCollection> roomCollectionList = roomRepository.findMyRoomsByUserId(userId);

        // 방마다 최신 메시지 받기
        // 1. roomid 에 해당하는 메시지 중
        // 2. 가장 최신 sort
        // 3. limit 1 개
        List<String> roomIds = null;
        List<MessageCollection> messageCollectionList = chatMessageRepository.getAllLastMessage(roomIds);

        return new APIMessage(APIMessage.ResultEnum.success,roomCollectionList);
    }

    public boolean outOfTheRoom(String roomId, String userId) {
        long count = roomRepository.outOfTheRoom(roomId,userId).getModifiedCount();
        System.out.println("삭제: "+count);
        if(count >= 1){
            return true;
        }
        return false;
    }

    public String convertMembersListToString(List<String> list){
        return String.join(", ",list);
    }

    public RespRoomDto getChatRoomInfo(String roomId) {
        RoomCollection roomCollection = roomRepository.findByRoomId(roomId).get();

        return new RespRoomDto(roomCollection.getRoomId(),
                roomCollection.getTitle(),
                roomCollection.getMembers(),
                roomCollection.getManagers());
    }
}
