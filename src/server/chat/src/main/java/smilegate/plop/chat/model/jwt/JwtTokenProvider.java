package smilegate.plop.chat.model.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smilegate.plop.chat.dto.UserDto;
import smilegate.plop.chat.exception.JwtTokenIncorrectStructureException;

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

    public String createAccessToken(UserDto userDto) {
        Map<String, Object> payloads = new HashMap<>();
        payloads.put("nickname",userDto.getNickname());
        payloads.put("userId", userDto.getUserId());
        payloads.put("email",userDto.getEmail());

        Date accessExpire = new Date(System.currentTimeMillis() + ACCESS_EXPIRED_TIME);

        String accessToken = Jwts.builder()
                .setSubject("access-token")
                .setClaims(payloads)
                .setExpiration(accessExpire)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
        return accessToken;
    }

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
