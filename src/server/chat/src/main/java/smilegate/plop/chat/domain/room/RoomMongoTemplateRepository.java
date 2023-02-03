package smilegate.plop.chat.domain.room;

import com.mongodb.client.result.UpdateResult;
import smilegate.plop.chat.dto.request.ReqDmDto;
import smilegate.plop.chat.dto.request.ReqInviteDto;

import java.util.List;

public interface RoomMongoTemplateRepository {

    UpdateResult inviteMembers(ReqInviteDto reqInviteDto);

    List<RoomCollection> findMyRoomsByUserId(String userId);

    UpdateResult outOfTheRoom(String roomId, String userId);

    RoomCollection matchDmMembers(ReqDmDto reqDmDto);
}
