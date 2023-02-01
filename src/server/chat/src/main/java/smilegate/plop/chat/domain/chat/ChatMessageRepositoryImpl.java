package smilegate.plop.chat.domain.chat;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ChatMessageRepositoryImpl implements ChatMongoTemplateRepository{

    private final MongoTemplate mongoTemplate;

    public ChatMessageRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<MessageCollection> getAllLastMessage(List<String> roomIds) {
        Query query = Query.query(Criteria.where("roomId").in(roomIds));

        return null;
    }
}
