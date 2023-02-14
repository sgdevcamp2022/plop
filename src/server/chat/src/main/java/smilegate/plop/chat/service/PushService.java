package smilegate.plop.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import smilegate.plop.chat.controller.PushProxy;
import smilegate.plop.chat.dto.ChatMessageDto;
import smilegate.plop.chat.dto.request.RequestMessage;
import smilegate.plop.chat.dto.response.ResponsePresenceUsers;

@Slf4j
@Service
@Async("push") // 비동기 추가
@RequiredArgsConstructor
public class PushService {
    private final PresenceService presenceService;
    private final PushProxy pushProxy;

    public void pushMessageToUsers(ChatMessageDto chatMessageDto){

        // 유저 접속상태 확인후 미접속일 때 알림서버에 요청
        ResponsePresenceUsers offlineUsers = presenceService.getOfflineUsers(chatMessageDto.getRoom_id());

        if(offlineUsers.getMembers().size() >= 1){
            pushProxy.sendNotification(RequestMessage.builder()
                    .title(chatMessageDto.getSender_id())
                    .body(chatMessageDto.getContent())
                    .target(offlineUsers.getMembers())
                    .build());
            log.info("오프라인 유저에게 푸시알림 요청");
        }
    }
}
