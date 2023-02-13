package smilegate.plop.push.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smilegate.plop.push.client.RegisterClient;
import smilegate.plop.push.dto.request.RequestMessage;
import smilegate.plop.push.dto.response.ResponseDto;
import smilegate.plop.push.dto.response.ResponseMessage;
import smilegate.plop.push.dto.response.ResponseUser;
import smilegate.plop.push.security.JwtTokenProvider;
import smilegate.plop.push.service.NotificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@Slf4j
public class PushController {
    Environment env;
   private NotificationService notificationService;
    private JwtTokenProvider jwtTokenProvider;
   private RegisterClient registerClient;

   @Autowired
   public PushController(NotificationService notificationService, Environment env,
                         RegisterClient registerClient, JwtTokenProvider jwtTokenProvider){
       this.notificationService = notificationService;
       this.env=env;
       this.registerClient = registerClient;
       this.jwtTokenProvider = jwtTokenProvider;
   }
   @GetMapping("/health_check")
    public String status() {
       return String.format("It's Working in User Service On PORT %s", env.getProperty("local.server.port"));
   }
   @PostMapping("/send")
    public ResponseEntity<ResponseMessage> sendNotification(
            @RequestBody RequestMessage message) {
       log.error(message.toString());
       notificationService.sendByTokenList(message);
       ResponseMessage responseMessage = new ResponseMessage("Send Notification succcessfully", message.getTitle(),message.getBody());
       return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
   }
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerNotification(
            @RequestHeader("AUTHORIZATION") String bearerToken,
            @RequestBody Map<String,Object> tokenId) {
        log.error(tokenId.toString());
        String jwt = jwtTokenProvider.removeBearer(bearerToken);
        String token = tokenId.get("tokenId").toString();
        ResponseUser responseUser = notificationService.registerFcmToken(jwt, token);
        ResponseDto responseDto = new ResponseDto<>("SUCCESS", "register token successfully", responseUser);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
