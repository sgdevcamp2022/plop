package smilegate.plop.chat.domain.room;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import smilegate.plop.chat.dto.ReqInviteDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomRepositoryImpl implements RoomMongoTemplateRepository{
    private final MongoTemplate mongoTemplate;

    public RoomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UpdateResult inviteMembers(ReqInviteDto reqInviteDto) {
        Query query = Query.query(Criteria.where("roomId").is(reqInviteDto.getRoom_id()));
        Update update = new Update().push("members").each(convertToMembersList(reqInviteDto.getMembers()));
        return mongoTemplate.updateFirst(query, update, RoomCollection.class);
    }

    @Override
    public List<RoomCollection> findMyRoomsByUserId(String userId) {
        Query query = Query.query(Criteria.where("members").elemMatch(
                Criteria.where("userId").is(userId)
        ));

        query.fields().include("roomId");
        query.fields().include("title");

        return mongoTemplate.find(query,RoomCollection.class);
    }

    @Override
    public UpdateResult outOfTheRoom(String roomId, String userId) {
        Query query = Query.query(Criteria.where("roomId").is(roomId));

        Update update = new Update();
        update.pull("members", new Query(Criteria.where("userId").in(userId)) );

        return mongoTemplate.updateFirst(query,update,"rooms");
    }


    public List<Member> convertToMembersList(List<String>list){
        List<Member> members = new ArrayList<>();
        list.stream().forEach(s -> members.add(new Member(s, LocalDateTime.now())));
        return members;
    }
}
