package smilegate.plop.user.dbtest;

import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import smilegate.plop.user.domain.friend.FriendEntity;
import smilegate.plop.user.domain.friend.FriendRepository;
import smilegate.plop.user.domain.user.UserEntity;
import smilegate.plop.user.domain.user.UserRepository;
import smilegate.plop.user.dto.response.ResponseFriend;
import smilegate.plop.user.model.FriendshipCode;
import smilegate.plop.user.service.FriendService;
import smilegate.plop.user.service.UserService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Friendship Duplication Test")
@Nested
public class DuplicateFriendshipTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private FriendService friendService;

    //expired jhl8109 token for test
    private final String jhl8109Jwt = "eyJhbGciOiJIUzUxMiJ9.eyJuaWNrbmFtZSI6ImN1dGVqZWhvIiwiZXhwIjoxNjc2OTAxMzAxLCJ1c2VySWQiOiJqaGw4MTA5IiwiZW1haWwiOiJqaGw4MTA5QG5hdmVyLmNvbSJ9.PrIltBobYdM3_vJYvAgzmQUjglyuYvg3f7uA7tAxDnJqnbmwmXdiIq3g9vwtH7s_3GEWJtBCSpzLJHJa2GKftw";
    //expired test token for test
    private final String testJwt = "eyJhbGciOiJIUzUxMiJ9.eyJuaWNrbmFtZSI6InRlc3QiLCJleHAiOjE2NzY5MDI1NzIsInVzZXJJZCI6InRlc3QiLCJlbWFpbCI6InRlc3RAdGVzdC5jb20ifQ.VY4aRlpX1GPeAGMhiXpn0xTR7nYt-IlD_BKDaMkIQBo1huSMQS7BZ8ooXIFuzUTRi2MmgeKAEEdAYwFMjpmehw";
    @Test
    @DisplayName("중복 체크")
    @Transactional
    @Rollback(value = false)
    void senderAndReceiverCheck() {
        ResponseFriend friendship = friendService.requestFriend(testJwt,"jhl8109",1);
    }



}
