package smilegate.plop.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.domain.room.Member;
import smilegate.plop.chat.domain.room.RoomCollection;
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

    public ChatRoomService(RoomRepository roomRepository ) {
        this.roomRepository = roomRepository;
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

        reqGroupDto.getMembers().stream().forEach(m -> members.add(new Member(m,LocalDateTime.now())));
        members.add(new Member(reqGroupDto.getCreator(), LocalDateTime.now()));

        RoomCollection savedRoom = roomRepository.save(RoomCollection.builder()
                .title(title)
                .members(members)
                .managers(Collections.singletonList(reqGroupDto.getCreator()))
                .build()
        );

        return new RespRoomDto(savedRoom.get_id(),savedRoom.getTitle(),savedRoom.getMembers(),savedRoom.getManagers());
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

}
