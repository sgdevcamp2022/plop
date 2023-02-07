package smilegate.plop.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smilegate.plop.user.domain.friend.FriendEntity;
import smilegate.plop.user.domain.friend.FriendRepository;
import smilegate.plop.user.domain.user.UserEntity;
import smilegate.plop.user.domain.user.UserRepository;
import smilegate.plop.user.dto.UserDto;
import smilegate.plop.user.dto.response.ResponseFriend;
import smilegate.plop.user.model.JwtUser;
import smilegate.plop.user.security.JwtTokenProvider;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
public class FriendService {
    UserRepository userRepository;
    FriendRepository friendRepository;

    JwtTokenProvider jwtTokenProvider;

    @Autowired
    public FriendService(UserRepository userRepository, FriendRepository friendRepository,
                         JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseFriend requestFriend(String jwt, String target, int status) {
        UserEntity senderEntity = null;
        UserEntity receiverEntity = null;
        // 친구 요청을 하는 엔티티 - jwt를 통해 email로 검색
        JwtUser sender = jwtTokenProvider.getUserInfo(jwt);
        senderEntity = userRepository.findByEmail(sender.getEmail());
        if (senderEntity == null)
            return null;

        // 친구 요청을 받는 엔티티 - 이메일 혹은 아이디로 검색
        if (target.contains("@")) // 이메일인 경우
            receiverEntity = userRepository.findByEmail(target);
        else // 아이디인 경우
            receiverEntity = userRepository.findByUserId(target);
        if (receiverEntity == null)
            return null;

        FriendEntity friend = FriendEntity.builder()
                .senderId(senderEntity.getId())
                .receiverId(receiverEntity.getId())
                .status(status)
                .build();
        FriendEntity savedFriend = friendRepository.save(friend);
        ResponseFriend responseFriend = savedFriend.toResponseFriend(senderEntity.getEmail(), receiverEntity.getEmail());

        return responseFriend;
    }
    // 요청/취소 : sender(jwt) - receiver(target)
    // 수락/거절 : receiver(jwt) - sender(target)
//    public ResponseFriend updateStatus(String jwt, String target, int status) {
//        UserEntity senderEntity = null;
//        UserEntity receiverEntity = null;
//        // 친구 요청을 하는 엔티티 - jwt를 통해 email로 검색
//        JwtUser sender = jwtTokenProvider.getUserInfo(jwt);
//        senderEntity = userRepository.findByEmail(sender.getEmail());
//        if (senderEntity == null)
//            throw new EntityNotFoundException();
//
//        // 친구 요청을 받는 엔티티 - 이메일 혹은 아이디로 검색
//        if (target.contains("@")) // 이메일인 경우
//            receiverEntity = userRepository.findByEmail(target);
//        else // 아이디인 경우
//            receiverEntity = userRepository.findByUserId(target);
//        if (receiverEntity == null)
//            throw new EntityNotFoundException();
//
//        friendRepository.findFriendEntityBySenderIdAndReceiverId(se)
//        friendRepository.
//    }

}
