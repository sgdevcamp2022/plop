package smilegate.plop.auth.controller;

import lombok.extern.slf4j.Slf4j;
import smilegate.plop.auth.dto.response.ResponseDto;
import smilegate.plop.auth.dto.UserDto;
import smilegate.plop.auth.dto.request.RequestUser;
import smilegate.plop.auth.dto.response.ResponseJWT;
import smilegate.plop.auth.dto.response.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.auth.security.JwtTokenProvider;
import smilegate.plop.auth.service.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class AuthController {
    private Environment env;
    private AuthService authService;

    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(Environment env, AuthService userService, JwtTokenProvider jwtTokenProvider) {
        this.env = env;
        this.authService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in User Service On PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signUp(@RequestBody RequestUser user) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(user, UserDto.class);
        UserDto savedUser = authService.createUser(userDto);

        ResponseUser responseUser = mapper.map(savedUser, ResponseUser.class);
        ResponseDto responseDto = new ResponseDto<>("SUCCESS", "signup successfully", responseUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    /*
    POST - login

     */

    @DeleteMapping("/logout")
    public ResponseEntity<ResponseDto> logOut(@RequestHeader("AUTHORIZATION") String bearerToken) {
        //게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        //access token 인지
        if (!jwtTokenProvider.isAccessToken(jwt)) {
            Map<String, Object> result = new HashMap<>();
            result.put("token",bearerToken);
            ResponseDto responseDto = new ResponseDto("FAIL", "token isn't valid access token", result);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        UserDto userDto = authService.logOut(jwt);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        ResponseDto responseDto = new ResponseDto("SUCCESS", "logout successfully", responseUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
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
    @PutMapping("/withdrawal")
    public ResponseEntity<ResponseDto> withdrawal(@RequestHeader("AUTHORIZATION") String bearerToken) {
        //게이트웨이에서 이미 인증 토큰의 유효성을 검증하였음.
        String jwt = jwtTokenProvider.removeBearer(bearerToken);

        if (!jwtTokenProvider.isAccessToken(jwt)) {
            Map<String, Object> result = new HashMap<>();
            result.put("token",bearerToken);
            ResponseDto responseDto = new ResponseDto("FAIL", "token isn't valid access token", result);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        UserDto userDto = authService.withdrawal(jwt);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
        ResponseDto responseDto = new ResponseDto("SUCCESS", "withdrawal successfully", responseUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}