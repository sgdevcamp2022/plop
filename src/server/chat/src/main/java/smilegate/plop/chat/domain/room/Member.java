package smilegate.plop.chat.domain.room;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Member {

    private String userId;
    private String lastReadMsgId;
    private LocalDateTime enteredAt;

    @Builder
    public Member(String userId, LocalDateTime enteredAt) {
        this.userId = userId;
        this.enteredAt = enteredAt;
    }
}
