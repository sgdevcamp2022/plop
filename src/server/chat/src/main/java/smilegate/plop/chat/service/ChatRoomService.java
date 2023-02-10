package smilegate.plop.chat.service;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.domain.chat.ChatMessageRepository;
import smilegate.plop.chat.domain.chat.MessageCollection;
import smilegate.plop.chat.domain.room.*;
import smilegate.plop.chat.dto.*;
import smilegate.plop.chat.dto.request.ReqDmDto;
import smilegate.plop.chat.dto.request.ReqGroupDto;
import smilegate.plop.chat.dto.request.ReqInviteDto;
import smilegate.plop.chat.dto.response.RespMyChatRoom;
import smilegate.plop.chat.dto.response.RespRoomDto;
import smilegate.plop.chat.exception.CustomAPIException;
import smilegate.plop.chat.exception.ErrorCode;

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

        return convertEntityToDto(savedRoom);
    }

    private String createGroupTitle(String creator, List<String> list){
        return creator + String.join(", ",list);
    }
    private void validateSizeOfGroup(ReqGroupDto reqGroupDto){
        if (reqGroupDto.getMembers().size() <= 1){
            log.info("ErrorCode: {}","GROUP_MEMBER_SIZE_ERROR");
            throw new CustomAPIException(ErrorCode.GROUP_MEMBER_SIZE_ERROR, "초대된 멤버가 1명이하입니다.");
        }
    }
    public RespRoomDto createGroup(ReqGroupDto reqGroupDto){
        validateSizeOfGroup(reqGroupDto);

        List<Member> members = new ArrayList<>();
        String title = createGroupTitle(reqGroupDto.getCreator(), reqGroupDto.getMembers());

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

    /**
     * 방마다 최신 메시지 받기
     * 1. roomid 에 해당하는 메시지 중
     * 2. 가장 최신 sort
     * 3. limit 1 개
     */
    public List<RespMyChatRoom> findMyRoomsByUserId(String userId){
        List<RoomCollection> roomCollectionList = roomRepository.findMyRoomsByUserId(userId);

        List<RespMyChatRoom> respMyChatRooms = new ArrayList<>();

        for(RoomCollection roomCollection : roomCollectionList){
            String roomId = roomCollection.getRoomId();
            MessageCollection messageCollection = chatMessageRepository.getLastMessage(roomId);

            LastMessage lastMessage = LastMessage.builder()
                            .message_id(messageCollection.get_id()).sender_id(messageCollection.getSenderId())
                            .content(messageCollection.getContent()).created_at(messageCollection.getCreatedAt())
                            .build();
            respMyChatRooms.add(RespMyChatRoom.builder()
                    .room_id(roomId).title(roomCollection.getTitle())
                    .members(roomCollection.getMembers())
                    .last_message(lastMessage)
                    .build());
        }

        return respMyChatRooms;
    }

    public boolean outOfTheRoom(String roomId, String userId) {
        long count = roomRepository.outOfTheRoom(roomId,userId).getModifiedCount();
        if(count >= 1){
            return true;
        }
        return false;
    }

    public RespRoomDto getChatRoomInfo(String roomId) {
        RoomCollection roomCollection = roomRepository.findByRoomId(roomId).orElseThrow(
                ()-> {
                    log.info("ErrorCode: {}, roomid: {}","ROOM_NOT_FOUND_ERROR", roomId);
                    throw new CustomAPIException(ErrorCode.ROOM_NOT_FOUND_ERROR, "채팅방이 없음");
                }
        );
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
