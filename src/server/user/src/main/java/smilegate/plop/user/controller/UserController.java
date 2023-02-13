package smilegate.plop.user.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import smilegate.plop.user.domain.user.UserEntity;
import smilegate.plop.user.dto.request.RequestProfile;
import smilegate.plop.user.dto.response.ResponseDto;
import smilegate.plop.user.dto.response.ResponseFriend;
import smilegate.plop.user.dto.response.ResponseProfile;
import smilegate.plop.user.dto.response.ResponseUser;
import smilegate.plop.user.model.FriendshipCode;
import smilegate.plop.user.security.JwtTokenProvider;
import smilegate.plop.user.service.FriendService;
import smilegate.plop.user.service.UserService;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/")
@Slf4j
public class UserController {
    private Environment env;
    private UserService userService;
    private JwtTokenProvider jwtTokenProvider;
    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;

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
            RequestProfile profile) {
        // jwt를 통해 본인이 맞는지 확인해야 할듯 로직 추가 필요
//        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        ResponseProfile user = userService.putProfile(profile);
        ResponseDto responseDto;
        if (user != null)
            responseDto = new ResponseDto<>("SUCCESS", "change profile successfully", user);
        else
            responseDto = new ResponseDto<>("FAIL", "change profile request failed", user);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
//    @PutMapping("/upload")
//    public ResponseEntity<ResponseDto> upload(RequestProfile requestProfile) throws IOException {
//        MultipartFile multipartFile = requestProfile.getImg();
//        String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
//
//        ObjectMetadata objMeta = new ObjectMetadata();
//        objMeta.setContentLength(multipartFile.getInputStream().available());
//
//        amazonS3Client.putObject(S3Bucket, s3FileName, multipartFile.getInputStream(), objMeta);
//        String uri = amazonS3Client.getUrl(S3Bucket, s3FileName).toString();
//        ResponseProfile responseProfile = new ResponseProfile(requestProfile.getTarget(),requestProfile.getTarget(),
//                Map.of("nickname",requestProfile.getNickname(),
//                        "img",uri));
//        ResponseDto responseDto =  new ResponseDto<>("SUCCESS", "change profile successfully", responseProfile);
//
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//
//    }
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
