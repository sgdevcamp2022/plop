package smilegate.plop.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.user.dto.response.ResponseDto;
import smilegate.plop.user.dto.response.ResponseFriend;
import smilegate.plop.user.model.FriendshipCode;
import smilegate.plop.user.security.JwtTokenProvider;
import smilegate.plop.user.service.FriendService;

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
    @DeleteMapping("/friend")
    public ResponseEntity<ResponseDto> deleteFriend(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> targetInput) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String target = targetInput.get("target").toString();
        log.error(target);
        ResponseFriend friend = friendService.deleteFriend(jwt, target, FriendshipCode.REJECTED.value());
        ResponseDto responseDto;
        if (friend != null)
            responseDto = new ResponseDto<>("SUCCESS", "reject friend successfully", friend);
        else
            responseDto = new ResponseDto<>("FAIL", "reject friend request failed", friend);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
