package smilegate.plop.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUserId(String userId);
    UserEntity findByEmail(String email);
    UserEntity findByUserIdOrEmail(String userId, String email);

    List<UserEntity> findByUserIdContainingOrEmailContaining(String userId, String email);

    List<UserEntity> findByIdIn(List<Long> userIdList);
}
