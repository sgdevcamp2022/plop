package smilegate.plop.presence.config.kafka;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresenceMessage {
    private String user_id;
    private String status;
    private List<String> friends;
}
