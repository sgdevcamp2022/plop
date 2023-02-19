package smilegate.plop.auth.dbtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import smilegate.plop.auth.domain.UserEntity;
import smilegate.plop.auth.domain.UserRepository;
import smilegate.plop.auth.dto.request.RequestLogin;
import smilegate.plop.auth.dto.response.ResponseJWT;
import smilegate.plop.auth.service.AuthService;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth DB test")
@SpringBootTest
@Nested
public class DbTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @DisplayName("role enum check")
    @Test
    @Transactional // TEST에서는 Transactional을 통해 자동 롤백
    void roleEnumTest() {
        UserEntity user = initTestUser("test2");
        userRepository.saveAndFlush(user);
        UserEntity savedUser = userRepository.findByUserIdOrEmail("test2","test2");
        System.out.println(savedUser);
        assertAll(
                () -> assertEquals(user,savedUser),
                () -> assertTrue((ChronoUnit.HOURS.between(savedUser.getCreatedAt(),LocalDateTime.now()) < 1), "생성 시간이 수정되어야 함")
        );
        assertEquals(user,savedUser);
    }
    @Test
    @DisplayName("jpa time auditing check")
    @Transactional
    void jpaAuditTest(){
        // 롤백으로 인해 수행 X -> rollback false
        UserEntity user = initTestUser("test");
        RequestLogin requestLogin = new RequestLogin("test","test");
        ResponseJWT jwt = authService.logIn(requestLogin);
        UserEntity auditedUser = userRepository.findByUserIdOrEmail("test","test");
        System.out.println(jwt);
        System.out.println(auditedUser);
        assertAll(
                () -> assertNotNull(auditedUser),
                () -> assertTrue((ChronoUnit.HOURS.between(auditedUser.getLoginAt(),LocalDateTime.now()) < 1), "로그인 시간이 수정되어야 함"),
                () -> assertTrue((ChronoUnit.HOURS.between(auditedUser.getUpdatedAt(),LocalDateTime.now()) < 1), "업데이트 시간이 수정되어야 함")
        );
    }

    UserEntity initTestUser(String data) {
        String encryptedPwd = bCryptPasswordEncoder.encode(data);
        UserEntity user = UserEntity.builder()
                .userId(data)
                .encryptedPwd(encryptedPwd)
                .state(1)
//                .role("t") // role enum test
                .role("ROLE_USER")
                .email(data)
                .profile(Map.of(data,data))
                .device(Map.of(data,data))
                .fcmToken(data)
                .build();

        return user;
    }
}