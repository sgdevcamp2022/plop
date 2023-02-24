package smilegate.plop.chat.model.jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtUser {
    String email;
    String userId;
    String nickname;
}
