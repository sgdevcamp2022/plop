package smilegate.plop.chat.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import smilegate.plop.chat.dto.request.RequestMessage;

@FeignClient(name = "push-service")
public interface PushProxy {
    @PostMapping("/push/v1/send")
    void sendNotification(@RequestBody RequestMessage message);
}
