package smilegate.plop.chat.domain.room;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<RoomCollection,String> , RoomMongoTemplateRepository{
    Optional<RoomCollection> findByRoomId(String roomId);

    boolean existsByRoomId(String roomId);
}
