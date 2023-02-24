package smilegate.plop.user.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smilegate.plop.user.dto.UserDto;
import smilegate.plop.user.exception.NotAccessTokenException;
import smilegate.plop.user.model.JwtUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${token.access_expired_time}")
    private long ACCESS_EXPIRED_TIME;

    @Value("${token.refresh_expired_time}")
    private long REFRESH_EXPIRED_TIME;

    @Value("${token.secret_key}")
    private String SECRET_KEY;

    public JwtUser getUserInfo(String token) {
        Map<String, Object> payloads = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        try {
            JwtUser user = JwtUser.builder()
                    .email(payloads.get("email").toString())
                    .userId(payloads.get("userId").toString())
                    .nickname(payloads.get("nickname").toString())
                    .build();
            return user;
        } catch (NullPointerException e) {
            throw new NotAccessTokenException(token + " is not access token");
        }
    }

    public String removeBearer(String bearerToken) {
        return  bearerToken.replace("Bearer ", "");
    }

    public Long getAccessExpiredTime() {
        return ACCESS_EXPIRED_TIME;
    }
    public Long getRefreshExpiredTime() {
        return REFRESH_EXPIRED_TIME;
    }

    public String getKey() {
        return SECRET_KEY;
    }
}
