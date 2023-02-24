package smilegate.plop.user.domain.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import smilegate.plop.user.domain.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity,Long> {
    FriendEntity findBySenderId(long senderId);
    FriendEntity findByReceiverId(long receiverId);
    Optional<FriendEntity> findBySenderIdAndReceiverId(long senderId, long receiverId);
    FriendEntity findBySenderIdAndReceiverIdAndStatus(long senderId, long receiverId, int status);
    List<FriendEntity> findBySenderIdAndStatus(long senderId, int status);
    List<FriendEntity> findByReceiverIdAndStatus(long receiverId, int status);

    @Query(value = "SELECT f from friend f " +
            "where (f.senderId=:id OR f.receiverId=:id) AND f.status=:status")
    List<FriendEntity> findBySenderIdOrReceiverIdAndStatus(@Param("id") long id, @Param("status") int status);

    // 두 유저 간 친구 관계 조회
//    @Query(value = "SELECT * from friend where " +
//            "(friend.sender_id = :senderId AND friend.receiver_id = :receiverId) " +
//            "OR (friend.sender_id = :receiverId AND friend.receiver_id = :senderId)")

    //FriendEntity findBySenderIdOrReceiverIdAndStatus(String senderId, String receiverId, int status);
    FriendEntity findFriendEntityBySenderIdAndReceiverId(String senderId, String receiverId);
//    FriendEntity findAllFriends(
//            @Param("senderId") Long sender,
//            @Param("receiverId") Long receiver);

}
