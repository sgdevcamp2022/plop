package smilegate.plop.push.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import smilegate.plop.push.dto.response.ResponseDto;

import java.util.Map;

@FeignClient(name="register", url="http://localhost:8000/user/v1")
public interface RegisterClient {
//    @PostMapping("/register")
//    ResponseDto register(String bearerToken, Map<String,Object> tokenId);
}
