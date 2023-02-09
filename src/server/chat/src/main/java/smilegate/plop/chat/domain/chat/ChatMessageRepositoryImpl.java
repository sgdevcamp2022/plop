package smilegate.plop.chat.domain.chat;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ChatMessageRepositoryImpl implements ChatMongoTemplateRepository{

    private final MongoTemplate mongoTemplate;

    public ChatMessageRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public MessageCollection getLastMessage(String roomId) {

        List<MessageCollection> messageCollectionList = mongoTemplate.find(Query.query(Criteria.where("roomId").is(roomId))
                .with(Sort.by(Sort.Direction.DESC,"createdAt")).limit(1), MessageCollection.class);

        if(messageCollectionList.size() == 0){
            messageCollectionList.add(new MessageCollection());
        }

        return messageCollectionList.get(0);
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

    @Override
    public List<MessageCollection> getAllMessagesAtRoom(String roomId) {
        Query query = Query.query(Criteria.where("roomId").is(roomId)).with(
                Sort.by(Sort.Direction.DESC,"createdAt"));
        return mongoTemplate.find(query,MessageCollection.class);
    }

    @Override
    public Page<MessageCollection> findByRoomIdWithPagingAndFiltering(String roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Query query = new Query()
                .with(pageable)
                .skip(pageable.getPageSize() * pageable.getPageNumber()) // offset : ~5, ~10
                .limit(pageable.getPageSize());
        query.addCriteria(Criteria.where("roomId").is(roomId));

        List<MessageCollection> messageCollections = mongoTemplate.find(query, MessageCollection.class);
        Page<MessageCollection> messageCollectionPage = PageableExecutionUtils.getPage(
                messageCollections,
                pageable,
                ()-> mongoTemplate.count(query.skip(-1).limit(-1), MessageCollection.class) // 정확한 도큐먼트 갯수 구하기 위함
        );

        return messageCollectionPage;
    }
}
