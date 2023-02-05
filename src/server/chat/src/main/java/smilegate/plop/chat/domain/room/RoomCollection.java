package smilegate.plop.chat.domain.room;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
//@QueryEntity
@Document(collection = "rooms")
public class RoomCollection {
    @Id
    private String _id;
    private String roomId;
    private String title;
    private RoomType type;
    private List<Member> members;
    private List<String> managers;
    private LocalDateTime createdAt;

    @Builder
    public RoomCollection(String roomId, String title, RoomType type, List<Member> members, List<String> managers) {
        this.roomId = roomId;
        this.title = title;
        this.type = type;
        this.members = members;
        this.managers = managers;
        this.createdAt = LocalDateTime.now();
    }

    public String makeTempRoomId(){
        return RoomIdCreator.createRoomId();
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
