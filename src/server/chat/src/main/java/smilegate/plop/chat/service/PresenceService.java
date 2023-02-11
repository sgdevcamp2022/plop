package smilegate.plop.chat.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.controller.PresenceProxy;
import smilegate.plop.chat.domain.room.Member;
import smilegate.plop.chat.dto.request.RequestUsers;
import smilegate.plop.chat.dto.response.RespRoomDto;
import smilegate.plop.chat.dto.response.ResponsePresenceUsers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceService {
    private final PresenceProxy presenceProxy;
    private final ChatRoomService chatRoomService;

    /**
     * 채팅방의 멤버들의 id 리스트를 가져와서
     * "접속상태 서버"를 통해 online 상태인 유저들을 조회(PresenceProxy)
     */
    public ResponsePresenceUsers getOfflineUsers(String roomId){
        RespRoomDto chatRoomInfo = chatRoomService.getChatRoomInfo(roomId);
        List<String> members = chatRoomInfo.getMembers().stream().map(Member::getUserId).collect(Collectors.toList());

        try{
            return presenceProxy.offlineUsers(new RequestUsers(members));
        }catch (Exception ex){ // ConnectException
            log.error("ConnectException - 오프라인유저 요청실패: {}",ex.getMessage());
            return new ResponsePresenceUsers(new ArrayList<>());
        }
    }
}
