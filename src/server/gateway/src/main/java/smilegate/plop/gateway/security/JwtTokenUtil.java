package smilegate.plop.gateway.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import smilegate.plop.gateway.config.JwtConfig;
import smilegate.plop.gateway.exception.JwtTokenIncorrectStructureException;
import smilegate.plop.gateway.exception.JwtTokenMalformedException;
import smilegate.plop.gateway.exception.JwtTokenMissingException;

import java.util.Date;

//
//@Component
//@Slf4j
//public class JwtTokenUtil {
//    @Autowired
//    private JwtConfig config;
//
//    //토큰 재발급 시
//    public String generateToken(String id) {
//        Claims claims = Jwts.claims().setSubject(id);
//        long nowMillis = System.currentTimeMillis();
//        long expMillis = nowMillis + config.getValidity() * 1000 * 60;
//        Date exp = new Date(expMillis);
//        return Jwts.builder().setClaims(claims).setIssuedAt(new Date (nowMillis)).setExpiration(exp)
//                .signWith(SignatureAlgorithm.HS512,config.getSecret()).compact();
//
//}
//    public void validateToken(final String header) throws JwtTokenMalformedException, JwtTokenMissingException {
//        try {
//            String[] parts = header.split(" ");
//            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
//                throw new JwtTokenIncorrectStructureException("Incorrect Authentication Structure");
//            }
//
//            Jwts.parser().setSigningKey(config.getSecret()).parseClaimsJws(parts[1]);
//        } catch (SignatureException ex) {
//            throw new JwtTokenMalformedException("Invalid JWT signature");
//        } catch (MalformedJwtException ex) {
//            throw new JwtTokenMalformedException("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            throw new JwtTokenMalformedException("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            throw new JwtTokenMalformedException("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            throw new JwtTokenMalformedException("JWT claims string is empty");
//        }
//    }
//}
