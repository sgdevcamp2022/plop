package smilegate.plop.presence.model.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smilegate.plop.presence.exception.JwtTokenIncorrectStructureException;

import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${token.secret_key}")
    private String SECRET_KEY;


    public JwtUser getUserInfo(String token) {
        if(isAccessToken(token))
        {
            Map<String, Object> payloads = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            JwtUser user = JwtUser.builder()
                    .email(payloads.get("email").toString())
                    .userId(payloads.get("userId").toString())
                    .nickname(payloads.get("nickname").toString())
                    .build();
            return user;
        }
        throw new JwtTokenIncorrectStructureException("토큰 인증 실패");
    }

    public Boolean isAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);

        }catch (ExpiredJwtException jwtException){
            throw new JwtTokenIncorrectStructureException("토큰 만료");
        }
        catch (SignatureException | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException jwtException) {
            throw new JwtTokenIncorrectStructureException("토큰 인증 실패");
        }
        return true;
    }

    public String removeBearer(String bearerToken) {
        return  bearerToken.replace("Bearer ", "");
    }
}
