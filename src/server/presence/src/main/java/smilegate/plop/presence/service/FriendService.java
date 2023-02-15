package smilegate.plop.presence.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smilegate.plop.presence.controller.UserProxy;
import smilegate.plop.presence.dto.UserDto;
import smilegate.plop.presence.dto.response.ResponseDto;
import smilegate.plop.presence.dto.response.ResponseProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            return new ArrayList<>();
        }catch (NullPointerException exception){
            return new ArrayList<>();
        }

        return friends;
    }
}
