package smilegate.plop.chat.domain.room;

import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import smilegate.plop.chat.dto.request.ReqDmDto;
import smilegate.plop.chat.dto.request.ReqInviteDto;
import smilegate.plop.chat.exception.CustomAPIException;
import smilegate.plop.chat.exception.ErrorCode;

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
        RoomCollection rc = mongoTemplate.findOne(Query.query(Criteria.where("roomId").is(reqInviteDto.getRoom_id())), RoomCollection.class);
        if(rc == null){
            throw new CustomAPIException(ErrorCode.ROOM_NOT_FOUND_ERROR, "존재하지않는 채팅방-"+reqInviteDto.getRoom_id());
        }
        Query query = Query.query(Criteria.where("roomId").is(reqInviteDto.getRoom_id()));
        Update update = new Update().push("members").each(convertToMembersList(reqInviteDto.getMembers()));
        update.set("title",rc.getTitle()+","+String.join(",",reqInviteDto.getMembers()));
        return mongoTemplate.updateFirst(query, update, RoomCollection.class);
    }

    @Override
    public List<RoomCollection> findMyRoomsByUserId(String userId) {
        Query query = Query.query(Criteria.where("members").elemMatch(
                Criteria.where("userId").is(userId)
        ));

        query.fields().exclude("_id");
        query.fields().exclude("managers");

        return mongoTemplate.find(query,RoomCollection.class);
    }

    @Override
    public UpdateResult outOfTheRoom(String roomId, String userId) {
        Query query = Query.query(Criteria.where("roomId").is(roomId));

        Update update = new Update();
        update.pull("members", new Query(Criteria.where("userId").in(userId)) );

        return mongoTemplate.updateFirst(query,update,"rooms");
    }

    @Override
    public RoomCollection matchDmMembers(ReqDmDto reqDmDto) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("type").is(RoomType.DM),
                Criteria.where("members").elemMatch(Criteria.where("userId").is(reqDmDto.getCreator())),
                Criteria.where("members").elemMatch(Criteria.where("userId").is(reqDmDto.getMessage_to())));

        return mongoTemplate.findOne(new Query(criteria),RoomCollection.class);
    }


    public List<Member> convertToMembersList(List<String>list){
        List<Member> members = new ArrayList<>();
        list.forEach(s -> members.add(new Member(s, LocalDateTime.now())));
        return members;
    }
}
