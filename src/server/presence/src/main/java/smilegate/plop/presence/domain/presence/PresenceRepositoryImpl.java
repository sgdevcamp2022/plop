package smilegate.plop.presence.domain.presence;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class PresenceRepositoryImpl implements PresenceTemplateRepository{

    private final MongoTemplate mongoTemplate;

    public PresenceRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<PresenceCollection> findByUserIdInFriends(List<String> friends) {
        Query query = Query.query(Criteria.where("userId").in(friends))
                .addCriteria(Criteria.where("status").is("online"));
        return mongoTemplate.find(query, PresenceCollection.class);
    }
}
