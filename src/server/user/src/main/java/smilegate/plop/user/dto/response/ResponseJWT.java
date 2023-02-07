package smilegate.plop.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseJWT {
    private String accessToken;
    private String refreshToken;
    private Date accessExpire;
    private Date refreshExpire;
}
