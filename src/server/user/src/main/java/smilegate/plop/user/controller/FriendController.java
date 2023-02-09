package smilegate.plop.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.user.dto.response.ResponseDto;
import smilegate.plop.user.dto.response.ResponseFriend;
import smilegate.plop.user.dto.response.ResponseProfile;
import smilegate.plop.user.model.FriendshipCode;
import smilegate.plop.user.security.JwtTokenProvider;
import smilegate.plop.user.service.FriendService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class FriendController {
    private Environment env;
    private FriendService friendService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FriendController(Environment env, FriendService friendService, JwtTokenProvider jwtTokenProvider) {
        this.env = env;
        this.friendService = friendService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service On PORT %s", env.getProperty("local.server.port"));
    }
    @GetMapping("/friend/request")
    public ResponseEntity<ResponseDto> requestFriendList(
            @RequestHeader("AUTHORIZATION") String bearerToken) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        List<ResponseProfile> friend = friendService.requestFriendList(jwt);
        ResponseDto responseDto;
        if (friend != null )
            responseDto = new ResponseDto<>("SUCCESS", "query friend request list successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "query friend request list failed", friend);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/friend/request")
    public ResponseEntity<ResponseDto> requestFriend(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> targetInput) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String target = targetInput.get("target").toString();
        log.error(target);
        ResponseFriend friend = friendService.requestFriend(jwt, target,FriendshipCode.REQUESTED.value());
        ResponseDto responseDto;
        if (friend != null )
            responseDto = new ResponseDto<>("SUCCESS", "send friend request successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "send friend request failed", friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @DeleteMapping("/friend/request")
    public ResponseEntity<ResponseDto> cancelFriend(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> targetInput) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String target = targetInput.get("target").toString();
        log.error(target);
        ResponseFriend friend = friendService.requestFriend(jwt, target, FriendshipCode.NONE.value());
        ResponseDto responseDto;
        if (friend != null)
            responseDto = new ResponseDto<>("SUCCESS", "cancel friend request successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "cancel friend request failed", friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @GetMapping("/friend/response")
    public ResponseEntity<ResponseDto> responseFriendList(
            @RequestHeader("AUTHORIZATION") String bearerToken) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        List<ResponseProfile> friend = friendService.responseFriendList(jwt);
        ResponseDto responseDto;
        if (friend != null )
            responseDto = new ResponseDto<>("SUCCESS", "query friend response list successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "query friend response list failed", friend);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PostMapping("/friend/response")
    public ResponseEntity<ResponseDto> acceptFriend(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> targetInput) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String target = targetInput.get("target").toString();
        log.error(target);
        ResponseFriend friend = friendService.responseFriend(jwt, target,FriendshipCode.ACCCEPTED.value());
        ResponseDto responseDto;
        if (friend != null )
            responseDto = new ResponseDto<>("SUCCESS", "accept friend successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "accept friend failed", friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @DeleteMapping("/friend/response")
    public ResponseEntity<ResponseDto> rejectFriend(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> targetInput) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String target = targetInput.get("target").toString();
        log.error(target);
        ResponseFriend friend = friendService.responseFriend(jwt, target, FriendshipCode.REJECTED.value());
        ResponseDto responseDto;
        if (friend != null)
            responseDto = new ResponseDto<>("SUCCESS", "reject friend successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "reject friend request failed", friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @GetMapping("/friend")
    public ResponseEntity<ResponseDto> getFriends(
            @RequestHeader("AUTHORIZATION") String bearerToken) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        List<ResponseProfile> friends = friendService.friendList(jwt);
        ResponseDto responseDto;
        if (friends != null)
            responseDto = new ResponseDto<>("SUCCESS", "get friend list successfully", friends);
        else
            responseDto = new ResponseDto<>("FAIL", "get friend list request failed", friends);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @DeleteMapping("/friend")
    public ResponseEntity<ResponseDto> deleteFriend(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> targetInput) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String target = targetInput.get("target").toString();
        log.error(target);
        ResponseFriend friend = friendService.deleteFriend(jwt, target);
        ResponseDto responseDto;
        if (friend != null)
            responseDto = new ResponseDto<>("SUCCESS", "reject friend successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "reject friend request failed", friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
