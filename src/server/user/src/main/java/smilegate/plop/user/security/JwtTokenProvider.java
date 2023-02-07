package smilegate.plop.user.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import smilegate.plop.user.dto.UserDto;
import smilegate.plop.user.exception.JwtTokenIncorrectStructureException;
import smilegate.plop.user.model.JwtUser;
import smilegate.plop.user.service.RedisService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private RedisService redisService;

    public JwtTokenProvider(RedisService redisService) {
        this.redisService = redisService;
    }
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

    public String createRefreshToken(UserDto userDto) {

        Date refreshExpire = new Date(System.currentTimeMillis() + REFRESH_EXPIRED_TIME);

        String refreshToken = Jwts.builder()
                .setSubject("refresh-token")
                .setExpiration(refreshExpire)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        redisService.setValues(userDto.getEmail(),refreshToken);

        return refreshToken;
    }

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
