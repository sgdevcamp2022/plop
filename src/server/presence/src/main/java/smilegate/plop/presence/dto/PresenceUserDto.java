package smilegate.plop.presence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PresenceUserDto {
    private String user_id;
    private String status;

    @Builder
    public PresenceUserDto(String user_id, String status) {
        this.user_id = user_id;
        this.status = status;
    }
}
