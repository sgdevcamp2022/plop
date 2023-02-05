package smilegate.plop.auth.controller;


import lombok.extern.slf4j.Slf4j;
import smilegate.plop.auth.dto.request.RequestEmailVerification;
import smilegate.plop.auth.dto.request.RequestUser;
import smilegate.plop.auth.dto.request.RequestVerificationCode;
import smilegate.plop.auth.dto.response.ResponseDto;
import smilegate.plop.auth.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.auth.dto.response.ResponseJWT;
import smilegate.plop.auth.dto.response.ResponseUser;
import smilegate.plop.auth.security.JwtTokenProvider;
import smilegate.plop.auth.service.AuthService;
import smilegate.plop.auth.service.MailService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class AuthController {
    private Environment env;
    private AuthService authService;

    private JwtTokenProvider jwtTokenProvider;
    private MailService mailService;

    @Autowired
    public AuthController(Environment env, AuthService userService,
                          JwtTokenProvider jwtTokenProvider, MailService mailService) {
        this.env = env;
        this.authService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mailService = mailService;
    }
    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto> reissue(@RequestHeader("AUTHORIZATION") String bearerToken,
                                               @RequestBody Map<String,Object> requestEmail) {
        // 게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);

        String email = requestEmail.get("email").toString();
        log.error(email);
        ResponseJWT responseJwt = authService.reissue(email,jwt);
        if (responseJwt.equals(null)) {
            ResponseDto responseDto = new ResponseDto("FAIL", "reissue failed", responseJwt);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseDto);
        }
        else {
            ResponseDto responseDto = new ResponseDto("SUCCESS", "reissue successfully", responseJwt);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }
    }
    @PostMapping("/email/code")
    public ResponseEntity<ResponseDto> sendVerificationCode(@RequestBody RequestEmailVerification info) {
        mailService.send(info, "plop 이메일 인증 요청입니다.");

        ResponseDto responseDto = new ResponseDto<>("SUCCESS", "send verification code successfully", "");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    @PostMapping("/email/verify")
    public ResponseEntity<ResponseDto> verifyEmail(@RequestBody RequestVerificationCode verificationCode) {
        boolean verificationResult = mailService.verifyCode(verificationCode);
        if (verificationResult) {
            ResponseDto responseDto = new ResponseDto<>("SUCCESS", "send verification code successfully", "");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } else {
            ResponseDto responseDto = new ResponseDto<>("FAIL", "verification code is not correct", "");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }
    }
}