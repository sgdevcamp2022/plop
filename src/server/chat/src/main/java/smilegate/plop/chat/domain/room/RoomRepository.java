package smilegate.plop.chat.domain.room;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<RoomCollection,String> , RoomMongoTemplateRepository{
    Optional<RoomCollection> findBy_id(String _id);

    Optional<RoomCollection> findByRoomId(String roomId);
}
