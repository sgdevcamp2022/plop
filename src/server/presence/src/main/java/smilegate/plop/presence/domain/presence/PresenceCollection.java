package smilegate.plop.presence.domain.presence;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Document(collection = "presence")
public class PresenceCollection {
    @Id
    private String _id;
    @Field("userId")
    private String userId;
    @Field("status")
    private String status;
    @Field("lastActiveAt")
    private LocalDateTime lastActiveAt;

    @Builder
    public PresenceCollection(String userId, String status, LocalDateTime lastActiveAt) {
        this.userId = userId;
        this.status = status;
        this.lastActiveAt = lastActiveAt;
    }
}
