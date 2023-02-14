package smilegate.plop.chat.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smilegate.plop.chat.dto.request.RequestUsers;
import smilegate.plop.chat.dto.response.ResponsePresenceUsers;

@FeignClient(name = "presence-service", url = "localhost:8021")
public interface PresenceProxy {
    @PostMapping("/presence/v1/offline-users")
    ResponsePresenceUsers offlineUsers(@RequestBody RequestUsers requestUsers);
}
