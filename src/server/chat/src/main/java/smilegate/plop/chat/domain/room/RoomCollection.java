package smilegate.plop.chat.domain.room;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

//@QueryEntity
@Document(collection = "rooms")
public class RoomCollection {
    @Id
    private String _id;
    private String roomId;
    private String title;
    private LocalDateTime lastModifiedAt;
    // embedded document
    private List<Member> members;
    private List<String> managers;


    @Builder
    public RoomCollection(String roomId, String title, List<Member> members, List<String> managers) {
        this.roomId = roomId;
        this.title = title;
        this.lastModifiedAt = LocalDateTime.now();
        this.members = members;
        this.managers = managers;
    }

    public String makeTempRoomId(){
        return RoomIdCreator.createRoomId();
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public String get_id() {
        return _id;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<String> getManagers() {
        return managers;
    }
}
