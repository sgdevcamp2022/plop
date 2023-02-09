package smilegate.plop.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smilegate.plop.user.domain.friend.FriendEntity;
import smilegate.plop.user.domain.friend.FriendRepository;
import smilegate.plop.user.domain.user.UserEntity;
import smilegate.plop.user.domain.user.UserRepository;
import smilegate.plop.user.dto.UserDto;
import smilegate.plop.user.dto.response.ResponseProfile;
import smilegate.plop.user.dto.response.ResponseUser;
import smilegate.plop.user.model.JwtUser;
import smilegate.plop.user.security.JwtTokenProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    UserRepository userRepository;
    FriendRepository friendRepository;

    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, FriendRepository friendRepository,
               JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.friendRepository =friendRepository;
        this.jwtTokenProvider =jwtTokenProvider;
    }

    public ResponseProfile getProfile(String target) {
        UserEntity user = userRepository.findByUserIdOrEmail(target,target);

        if (user == null) {
            return null;
        } else {
            ResponseProfile responseProfile = new ResponseProfile(
                    user.getUserId(), user.getEmail(),user.getProfile());
            return responseProfile;
        }
    }

    public ResponseProfile putProfile(String target, String nickname, String img) {
        UserEntity user = userRepository.findByUserIdOrEmail(target,target);

        if (user==null) {
            return null;
        } else {
            user.setProfile(Map.of(
                    "nickname" , nickname,
                    "img" , img
            ));
            UserEntity savedUser = userRepository.save(user);
            ResponseProfile responseProfile = new ResponseProfile(
                    savedUser.getUserId(), savedUser.getEmail(), savedUser.getProfile()
            );
            return responseProfile;
        }
    }

    public List<ResponseProfile> searchUser(String target) {
        List<UserEntity> users = userRepository.findByUserIdContainingOrEmailContaining(target,target);
        log.error(users.toString());

        List<ResponseProfile> responseUserList = new ArrayList<>();
        if (users == null)
            return null;
        else {
            for(UserEntity userEntity : users) {

                ResponseProfile user = new ResponseProfile(
                        userEntity.getUserId(), userEntity.getEmail(), userEntity.getProfile());
                responseUserList.add(user);
            }
            return responseUserList;
        }
    }
    public ResponseUser registerFcmToken(String jwt, String tokenId) {
        JwtUser sender = jwtTokenProvider.getUserInfo(jwt);
        UserEntity user = userRepository.findByUserId(sender.getUserId());
        user.setFcmToken(tokenId);
        userRepository.save(user);

        return new ResponseUser(
                user.getEmail(),user.getProfile().get("nickname").toString(),user.getUserId());
    }
}
