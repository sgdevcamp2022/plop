package smilegate.plop.presence.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import smilegate.plop.presence.dto.response.ResponseDto;

@FeignClient(name = "user-service", url = "localhost:8000")
public interface UserProxy {

    /**
     * 헤더에 토큰을 담아 자신의 친구 리스트를
     * 유저서버에 요청하여 값을 받는다.
     */
    @GetMapping("/user/v1/friend")
    ResponseDto getMyFriends(@RequestHeader("Authorization") String jwt);
}
