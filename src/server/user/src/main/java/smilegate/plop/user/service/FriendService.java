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
import smilegate.plop.user.model.FriendshipCode;
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
        // 친구 요청을 하는 엔티티 - jwt를 통해 email로 검색
        JwtUser sender = jwtTokenProvider.getUserInfo(jwt);
        UserEntity senderEntity = userRepository.findByEmail(sender.getEmail());
        if (senderEntity == null)
            return null;

        // 친구 요청을 받는 엔티티 - 이메일 혹은 아이디로 검색
        UserEntity receiverEntity = null;
        if (target.contains("@")) {// 이메일인 경우
            receiverEntity = userRepository.findByEmail(target);
            log.error(receiverEntity.toString());
        }

        else // 아이디인 경우
            receiverEntity = userRepository.findByUserId(target);
        if (receiverEntity == null)
            return null;

        FriendEntity friendEntity, savedFriendEntity;
        if (status == FriendshipCode.NONE.value()) { // 취소하고 싶은경우 삭제
            friendEntity = friendRepository.findBySenderIdAndReceiverIdAndStatus(
                    senderEntity.getId(), receiverEntity.getId(),FriendshipCode.REQUESTED.value());
//            friendEntity.setStatus(FriendshipCode.NONE.value());
            friendRepository.delete(friendEntity);
        } else { //FriendshipCode.REQUESTED.value() 신청하고 싶은 경우 새로 만들어서 1로 생성
            friendEntity = FriendEntity.builder()
                    .senderId(senderEntity.getId())
                    .receiverId(receiverEntity.getId())
                    .status(FriendshipCode.REQUESTED.value())
                    .build();
            if (friendEntity == null ) {
                return null;
            }
            friendRepository.save(friendEntity);
        }


        ResponseFriend responseFriend = ResponseFriend.builder()
                .sender(sender.getEmail())
                .receiver(receiverEntity.getEmail())
                .build();

        return responseFriend;
    }
    // 요청/취소 : sender(jwt) - receiver(target)
    // 수락/거절 : receiver(jwt) - sender(target)
    public ResponseFriend responseFriend(String jwt, String target, int status) {

        // 친구 요청을 하는 엔티티 - jwt를 통해 email로 검색
        JwtUser receiver = jwtTokenProvider.getUserInfo(jwt);
        UserEntity receiverEntity = userRepository.findByEmail(receiver.getEmail());
        if (receiverEntity == null)
            return null;

        // 친구 요청을 받는 엔티티 - 이메일 혹은 아이디로 검색
        UserEntity senderEntity = null;
        if (target.contains("@")) // 이메일인 경우
            senderEntity = userRepository.findByEmail(target);
        else // 아이디인 경우
            senderEntity = userRepository.findByUserId(target);
        if (senderEntity == null)
            return null;

        log.error(senderEntity.toString());
        log.error(receiverEntity.toString());
        FriendEntity friendEntity = friendRepository.findBySenderIdAndReceiverIdAndStatus(
                senderEntity.getId(), receiverEntity.getId(),FriendshipCode.REQUESTED.value());
        if (friendEntity == null)
            return null;
        // 수락 : 2 , 거절 : 3 (DB에서 삭제하지 않음-> 친구 요청한 사용자에게 알리기 위함)
        friendEntity.setStatus(status);
        FriendEntity savedFriend = friendRepository.save(friendEntity);
        ResponseFriend responseFriend = savedFriend.toResponseFriend(senderEntity.getEmail(), receiverEntity.getEmail());

        return responseFriend;
    }

    public ResponseFriend deleteFriend(String jwt, String target, int value) {
        // 친구 요청을 하는 엔티티 - jwt를 통해 email로 검색
        JwtUser receiver = jwtTokenProvider.getUserInfo(jwt);
        UserEntity receiverEntity = userRepository.findByEmail(receiver.getEmail());
        if (receiverEntity == null)
            return null;

        // 친구 요청을 받는 엔티티 - 이메일 혹은 아이디로 검색
        UserEntity senderEntity = null;
        if (target.contains("@")) // 이메일인 경우
            senderEntity = userRepository.findByEmail(target);
        else // 아이디인 경우
            senderEntity = userRepository.findByUserId(target);
        if (senderEntity == null)
            return null;

        return null;
    }

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
