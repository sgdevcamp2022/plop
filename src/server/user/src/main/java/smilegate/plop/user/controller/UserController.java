package smilegate.plop.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.user.domain.user.UserEntity;
import smilegate.plop.user.dto.response.ResponseDto;
import smilegate.plop.user.dto.response.ResponseFriend;
import smilegate.plop.user.dto.response.ResponseProfile;
import smilegate.plop.user.dto.response.ResponseUser;
import smilegate.plop.user.model.FriendshipCode;
import smilegate.plop.user.security.JwtTokenProvider;
import smilegate.plop.user.service.FriendService;
import smilegate.plop.user.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class UserController {
    private Environment env;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(Environment env, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.env = env;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @GetMapping("/profile")
    public ResponseEntity<ResponseDto> getProfile(
            @RequestParam(name = "target") String target) {
//        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        ResponseProfile user = userService.getProfile(target);
        ResponseDto responseDto;
        if (user != null)
            responseDto = new ResponseDto<>("SUCCESS", "get profile successfully", user);
        else
            responseDto = new ResponseDto<>("FAIL", "get profile request failed", user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PutMapping("/profile")
    public ResponseEntity<ResponseDto> changeProfile(
            @RequestParam String target,
            @RequestParam String img,
            @RequestParam String nickname) {
        // jwt를 통해 본인이 맞는지 확인해야 할듯 로직 추가 필요
//        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        ResponseProfile user = userService.putProfile(target, nickname,img);
        ResponseDto responseDto;
        if (user != null)
            responseDto = new ResponseDto<>("SUCCESS", "change profile successfully", user);
        else
            responseDto = new ResponseDto<>("FAIL", "change profile request failed", user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @GetMapping("/search")
    public ResponseEntity<ResponseDto> searchUser(
            @RequestParam String target) {
//        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        List<ResponseProfile> users = userService.searchUser(target);
        ResponseDto responseDto;
        if (users != null)
            responseDto = new ResponseDto<>("SUCCESS", "search users successfully", users);
        else
            responseDto = new ResponseDto<>("FAIL", "search users request failed", users);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    @PostMapping("register")
    public ResponseEntity<ResponseDto> registerFcmToken(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> tokenId
    ) {
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String token = tokenId.get("tokenId").toString();
        ResponseUser responseUser = userService.registerFcmToken(jwt, token);
        ResponseDto responseDto = new ResponseDto<>("SUCCESS", "register token successfully", responseUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
