package smilegate.plop.chat.domain.chat;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
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

    @Override
    public List<MessageCollection> getNewMessages(String roomId, String readMsgId) {
        ObjectId mObjId = new ObjectId(readMsgId);
        LocalDateTime createAt = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(mObjId)), MessageCollection.class).getCreatedAt();

        List<MessageCollection> messageCollectionList = mongoTemplate.find(
                Query.query(Criteria.where("roomId").is(roomId))
                        .addCriteria(Criteria.where("createdAt").gt(createAt)).with(
                                Sort.by(Sort.Direction.DESC,"createdAt"))
                , MessageCollection.class);

        return messageCollectionList;
    }
}
