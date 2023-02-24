package smilegate.plop.chat.domain.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<MessageCollection,String>, ChatMongoTemplateRepository {
}
