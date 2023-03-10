package smilegate.plop.presence.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smilegate.plop.presence.controller.UserProxy;
import smilegate.plop.presence.dto.response.ResponseDto;
import smilegate.plop.presence.dto.response.ResponseProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserProxy userProxy;

    public List<String> getMyFriends(String jwt){
        List<String> friends;
        try{
            ResponseDto responseDtos = userProxy.getMyFriends(jwt);
            friends = responseDtos.getData().stream().map(ResponseProfile::getUserId).collect(Collectors.toList());
        }catch (FeignException exception){
            log.error("getMyFriends, 유저서버와 연결되지 않음");
            return new ArrayList<>();
        }catch (NullPointerException exception){
            log.info("getMyFriends, 친구정보가 없음");
            return new ArrayList<>();
        }

        return friends;
    }
}
