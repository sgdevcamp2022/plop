package smilegate.plop.gateway.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtUser {
    String email;
    String userId;
    String nickname;
}
