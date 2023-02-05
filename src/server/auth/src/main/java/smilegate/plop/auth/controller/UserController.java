package smilegate.plop.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smilegate.plop.auth.dto.request.RequestEmailVerification;
import smilegate.plop.auth.dto.response.ResponseDto;
import smilegate.plop.auth.dto.UserDto;
import smilegate.plop.auth.dto.request.RequestUser;
import smilegate.plop.auth.dto.response.ResponseUser;
import smilegate.plop.auth.security.JwtTokenProvider;
import smilegate.plop.auth.service.AuthService;
import smilegate.plop.auth.service.MailService;

@RestController
@RequestMapping("/")
@Slf4j
public class UserController {
    private Environment env;
    private AuthService authService;
    private MailService mailService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(Environment env, AuthService userService, MailService mailService, JwtTokenProvider jwtTokenProvider) {
        this.env = env;
        this.authService = userService;
        this.mailService = mailService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/email/code")
    public ResponseEntity<ResponseDto> verifyEmail(@RequestBody RequestEmailVerification info) {
        mailService.send(info, "plop 이메일 인증 요청입니다.");

        ResponseDto responseDto = new ResponseDto<>("SUCCESS", "send code successfully", "");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
