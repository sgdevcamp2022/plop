package smilegate.plop.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.domain.chat.ChatMessageRepository;
import smilegate.plop.chat.domain.chat.MessageCollection;
import smilegate.plop.chat.domain.room.*;
import smilegate.plop.chat.dto.*;
import smilegate.plop.chat.dto.request.ReqDmDto;
import smilegate.plop.chat.dto.request.ReqGroupDto;
import smilegate.plop.chat.dto.request.ReqInviteDto;
import smilegate.plop.chat.dto.response.RespRoomDto;

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

    public RoomCollection matchDmMembers(ReqDmDto reqDmDto){
        return roomRepository.matchDmMembers(reqDmDto);
    }

    public RespRoomDto createDmRoom(ReqDmDto reqDmDto){
        // type이 dm이고 멤버가 일치하면 해당 방정보를 전송
        RoomCollection savedRoom = matchDmMembers(reqDmDto);
        if(savedRoom == null){
            List<Member> members = new ArrayList<>();
            members.add(new Member(reqDmDto.getCreator(),LocalDateTime.now()));
            members.add(new Member(reqDmDto.getMessage_to(),LocalDateTime.now()));

            savedRoom = roomRepository.save(RoomCollection.builder()
                    .members(members)
                    .type(RoomType.DM)
                    .roomId(RoomIdCreator.createRoomId())
                    .build());
        }

        return RespRoomDto.builder()
                .room_id(savedRoom.getRoomId())
                .type(savedRoom.getType())
                .members(savedRoom.getMembers())
                .build();
    }


    public RespRoomDto createGroup(ReqGroupDto reqGroupDto){
        List<Member> members = new ArrayList<>();
        String title = convertMembersListToString(reqGroupDto.getMembers());

        reqGroupDto.getMembers().forEach(m -> members.add(new Member(m,LocalDateTime.now())));
        members.add(new Member(reqGroupDto.getCreator(), LocalDateTime.now()));

        RoomCollection savedRoom = roomRepository.save(RoomCollection.builder()
                .title(title)
                .members(members)
                .type(RoomType.GROUP)
                .managers(Collections.singletonList(reqGroupDto.getCreator()))
                .roomId(RoomIdCreator.createRoomId())
                .build()
        );

        return convertEntityToDto(savedRoom);
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

        return convertEntityToDto(roomCollection);
    }

    private RespRoomDto convertEntityToDto(RoomCollection roomCollection) {
        return RespRoomDto.builder()
                .room_id(roomCollection.getRoomId())
                .title(roomCollection.getTitle())
                .type(roomCollection.getType())
                .members(roomCollection.getMembers())
                .managers(roomCollection.getManagers())
                .createdAt(roomCollection.getCreatedAt())
                .build();
    }
}
