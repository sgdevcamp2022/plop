package smilegate.plop.chat.domain.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import smilegate.plop.chat.domain.room.RoomCollection;
import smilegate.plop.chat.domain.room.RoomMongoTemplateRepository;

public interface ChatMessageRepository extends MongoRepository<MessageCollection,String>, ChatMongoTemplateRepository {
}
