package smilegate.plop.user.domain.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smilegate.plop.user.domain.user.UserEntity;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity,Long> {
    FriendEntity findBySenderId(String userId);
    FriendEntity findByReceiverId(String userId);

    // 두 유저 간 친구 관계 조회
//    @Query(value = "SELECT * from friend where " +
//            "(friend.sender_id = :senderId AND friend.receiver_id = :receiverId) " +
//            "OR (friend.sender_id = :receiverId AND friend.receiver_id = :senderId)")
    FriendEntity findFriendEntityBySenderIdAndReceiverId(String senderId, String receiverId);
//    FriendEntity findAllFriends(
//            @Param("senderId") Long sender,
//            @Param("receiverId") Long receiver);

}
