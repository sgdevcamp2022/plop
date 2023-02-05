package smilegate.plop.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    public void setValues(String email, String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, token);
        //values.set(token, email , Duration.ofMinutes(3));
    }
    public void setValuesWithTTL(String email, String token, long minutes) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, token , Duration.ofMinutes(3));
    }
    public String getValues(String email) {
        ValueOperations<String,String> values = redisTemplate.opsForValue();
        return values.get(email);
    }


    public void delValues(String email) {
        redisTemplate.delete(email);
    }
}
