package smilegate.plop.presence.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.presence.config.kafka.PresenceMessage;
import smilegate.plop.presence.config.kafka.Producer;
import smilegate.plop.presence.dto.PresenceUserDto;
import smilegate.plop.presence.dto.response.ResponsePresenceUsers;
import smilegate.plop.presence.model.jwt.JwtTokenProvider;
import smilegate.plop.presence.service.FriendService;
import smilegate.plop.presence.service.PresenceService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/presence")
@RequiredArgsConstructor
public class PresenceController {

    private final JwtTokenProvider jwtTokenProvider;
    private final PresenceService presenceService;
    private final FriendService friendService;
    private final Producer producer;

    private String getTokenToUserId(String jwt){
        return jwtTokenProvider.getUserInfo(jwtTokenProvider.removeBearer(jwt)).getUserId();
    }

    @Operation(summary = "친구리스트API", description = "online인 나의 친구 id 리스트 조회")
    @GetMapping("/v1/users")
    public ResponseEntity<ResponsePresenceUsers> presenceUsers(@RequestHeader("Authorization") String jwt){
        /**
         * 유저서버를 통해 친구리스트API 요청
          */
        List<String> friends = friendService.getMyFriends(jwt);
        if(friends.isEmpty()) return new ResponseEntity<>(new ResponsePresenceUsers(), HttpStatus.OK);
        return new ResponseEntity<>(presenceService.getUsersPresence(friends),HttpStatus.OK);
    }

    @Operation(summary = "접속상태를 'online'으로 변경", description = "나의 접속상태를 'online'으로 변경하고 웹소켓으로 해당 정보 친구들에게 전달")
    @PutMapping("/v1/on")
    public ResponseEntity<String> sendPresenceOn(@RequestHeader("Authorization") String jwt){
        String userId = getTokenToUserId(jwt);
        PresenceUserDto presenceUserDto = presenceService.presenceOn(userId);

        List<String> friends = friendService.getMyFriends(jwt);

        PresenceMessage presenceMessage = PresenceMessage.builder()
                        .user_id(presenceUserDto.getUser_id())
                        .status(presenceUserDto.getStatus())
                        .friends(friends)
                        .build();

        producer.sendMessage(presenceMessage);

        return ResponseEntity.ok("success");
    }

    @Operation(summary = "접속상태를 'offline'으로 변경", description = "나의 접속상태를 'offline'으로 변경하고 웹소켓으로 해당 정보 친구들에게 전달")
    @PutMapping("/v1/off")
    public ResponseEntity<String> sendPresenceOff(@RequestHeader("Authorization") String jwt){
        String userId = getTokenToUserId(jwt);
        PresenceUserDto presenceUserDto = presenceService.presenceOff(userId);

        List<String> friends = friendService.getMyFriends(jwt);
        PresenceMessage presenceMessage = PresenceMessage.builder()
                .user_id(presenceUserDto.getUser_id())
                .status(presenceUserDto.getStatus())
                .friends(friends)
                .build();

        producer.sendMessage(presenceMessage);

        return ResponseEntity.ok("success");
    }
}
