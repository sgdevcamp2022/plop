package smilegate.plop.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.dto.response.ResponsePresenceUsers;

@Slf4j
@Service
@Async("push")
@RequiredArgsConstructor
public class PushService {
    private final PresenceService presenceService;

    public void pushMessageToUsers(ChatMessageDto chatMessageDto){
        // 비동기 추가
        // 유저 접속상태 확인후 미접속일 때 알림서버에 요청
        ResponsePresenceUsers offlineUsers = presenceService.getOfflineUsers(chatMessageDto.getRoom_id());
        // 푸시 요청
        if(offlineUsers.getMembers().size() >= 1){
            log.info("오프라인 유저에게 푸시알림 요청");

        }
    }
}
