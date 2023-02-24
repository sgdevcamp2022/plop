package smilegate.plop.chat.domain.room;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
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
