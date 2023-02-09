package smilegate.plop.presence.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smilegate.plop.presence.controller.UserProxy;
import smilegate.plop.presence.dto.UserDto;
import smilegate.plop.presence.dto.response.ResponseDto;
import smilegate.plop.presence.dto.response.ResponseProfile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserProxy userProxy;

    public List<String> getMyFriends(String jwt){
        ResponseDto responseDtos = userProxy.getMyFriends(jwt);
        List<String> friends = responseDtos.getData().stream().map(ResponseProfile::getUserId).collect(Collectors.toList());
        return friends;
    }
}
