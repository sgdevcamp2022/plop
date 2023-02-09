package smilegate.plop.presence.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseProfile {
    private String userId;
    private String email;
    private Map<String,Object> profile;
}