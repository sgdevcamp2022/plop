package smilegate.plop.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smilegate.plop.user.domain.friend.FriendEntity;
import smilegate.plop.user.domain.friend.FriendRepository;
import smilegate.plop.user.domain.user.UserEntity;
import smilegate.plop.user.domain.user.UserRepository;
import smilegate.plop.user.dto.response.ResponseFriend;
import smilegate.plop.user.dto.response.ResponseProfile;
import smilegate.plop.user.model.FriendshipCode;
import smilegate.plop.user.model.JwtUser;
import smilegate.plop.user.security.JwtTokenProvider;

import java.util.ArrayList;
import java.util.List;

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
        // 이메일인 경우
        if (target.contains("@"))
            receiverEntity = userRepository.findByEmail(target);
        // 아이디인 경우
        else
            receiverEntity = userRepository.findByUserId(target);
        if (receiverEntity == null)
            return null;

        FriendEntity friendEntity;
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

    public ResponseFriend deleteFriend(String jwt, String target) {
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
    public List<ResponseProfile> friendList(String jwt) {
        JwtUser sender = jwtTokenProvider.getUserInfo(jwt);
        UserEntity senderEntity = userRepository.findByEmail(sender.getEmail());
        if (senderEntity == null)
            return null;

        List<FriendEntity> friendEntityList = friendRepository.findBySenderIdOrReceiverIdAndStatus(
                senderEntity.getId(),
                FriendshipCode.ACCCEPTED.value()
        );
        log.error(friendEntityList.toString());
        log.error(senderEntity.toString());
        List<ResponseProfile> responseFriendList = new ArrayList<>();
        if (friendEntityList == null) {
            return null;
        } else {
            for(FriendEntity friend : friendEntityList) {
                UserEntity user;
                if (friend.getSenderId().equals(senderEntity.getId())) { //내가 요청한 경우(sender) -> 수락한 사람이 친구 (receiver)
                    user = userRepository.findById(friend.getReceiverId()).orElse(null);
                } else { // 내가 수락한 경우(receiver) -> 요청한 사람이 친구 (sender)
                    user = userRepository.findById(friend.getSenderId()).orElse(null);
                }
                if (user == null)
                    return null;

                ResponseProfile userProfile = new ResponseProfile(user.getUserId(), user.getEmail(), user.getProfile());
                responseFriendList.add(userProfile);
            }
            log.error(responseFriendList.toString());
            return responseFriendList;
        }
    }

    public List<ResponseProfile> requestFriendList(String jwt) {
        JwtUser sender = jwtTokenProvider.getUserInfo(jwt);
        UserEntity senderEntity = userRepository.findByEmail(sender.getEmail());
        if (senderEntity == null)
            return null;

        List<FriendEntity> friendEntityList = friendRepository.findBySenderIdAndStatus(
                senderEntity.getId(),
                FriendshipCode.REQUESTED.value()
        );
        List<ResponseProfile> responseFriendList = new ArrayList<>();
        if (friendEntityList == null) {
            return null;
        } else {
            for(FriendEntity friend : friendEntityList) {
                UserEntity receiver = userRepository.findById(friend.getReceiverId()).orElse(null);
                if (sender == null)
                    return null;

                ResponseProfile user = new ResponseProfile(receiver.getUserId(), receiver.getEmail(), receiver.getProfile());
                responseFriendList.add(user);
            }
            return responseFriendList;
        }
    }
    public List<ResponseProfile> responseFriendList(String jwt) {
        JwtUser receiver = jwtTokenProvider.getUserInfo(jwt);
        UserEntity receiverEntity = userRepository.findByEmail(receiver.getEmail());
        if (receiverEntity == null)
            return null;

        List<FriendEntity> friendEntityList = friendRepository.findByReceiverIdAndStatus(
                receiverEntity.getId(),
                FriendshipCode.REQUESTED.value()
        );
        List<ResponseProfile> responseFriendList = new ArrayList<>();
        if (friendEntityList == null) {
            return null;
        } else {
            for(FriendEntity friend : friendEntityList) {
                UserEntity sender = userRepository.findById(friend.getSenderId()).orElse(null);
                if (sender == null)
                    return null;

                ResponseProfile user = new ResponseProfile(sender.getUserId(), sender.getEmail(), sender.getProfile());
                responseFriendList.add(user);
            }
            return responseFriendList;
        }
    }


}
