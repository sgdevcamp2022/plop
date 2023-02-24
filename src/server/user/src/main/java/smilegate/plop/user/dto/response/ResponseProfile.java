package smilegate.plop.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ResponseProfile {
    private String userId;
    private String email;
    private Map<String,Object> profile;
}
