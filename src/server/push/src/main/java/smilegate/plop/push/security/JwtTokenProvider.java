package smilegate.plop.push.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smilegate.plop.push.dto.UserDto;
import smilegate.plop.push.exception.JwtTokenIncorrectStructureException;
import smilegate.plop.push.model.JwtUser;

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
        try {
            Map<String, Object> payloads = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            JwtUser user = JwtUser.builder()
                    .email(payloads.get("email").toString())
                    .userId(payloads.get("userId").toString())
                    .nickname(payloads.get("nickname").toString())
                    .build();
            return user;
        } catch (JwtTokenIncorrectStructureException jwtTokenIncorrectStructureException) {
            throw jwtTokenIncorrectStructureException;
        }
    }

    public Boolean isAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);

        } catch (SignatureException | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException jwtException) {
            throw jwtException;
        }
        return true;
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
