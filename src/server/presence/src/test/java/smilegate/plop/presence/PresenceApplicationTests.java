package smilegate.plop.presence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import smilegate.plop.presence.dto.PresenceUserDto;
import smilegate.plop.presence.dto.response.ResponsePresenceUsers;
import smilegate.plop.presence.service.PresenceService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PresenceApplicationTests {
	private static final Logger LOGGER  = LoggerFactory.getLogger(PresenceApplicationTests.class);

	@Autowired
	PresenceService presenceService;

	@Test
	void 접속유저n명생성(){
		int size = 500;
		for(int i = 1; i<size;i++){
			presenceService.presenceOn("imuser"+i);
		}
	}
	@Test
	void 오프라인n명생성(){
		int size = 500;
		for(int i = 1; i<size;i++){
			presenceService.presenceOff("imuser"+i);
		}
	}

	@Test
	void 유저접속on() {
		PresenceUserDto presenceUserDto = presenceService.presenceOn("imuser1");
		assertNotNull(presenceUserDto);
	}

	@Test
	void 유저off(){
		PresenceUserDto presenceUserDto = presenceService.presenceOff("imuser2");
		assertNotNull(presenceUserDto);
	}

	@Test
	@DisplayName("내친구접속상태찾기")
	void 내친구접속상태찾기_imuser3이_오프라인(){
		// given
		List<String> friends = new ArrayList<>();
		friends.add("imuser2"); friends.add("imuser3"); friends.add("imuser4");

		// when
		presenceService.presenceOn("imuser2"); presenceService.presenceOn("imuser4");
		presenceService.presenceOff("imuser3");

		//then
		ResponsePresenceUsers responsePresenceUsers = presenceService.getUsersPresence(friends);
		assertEquals(2,responsePresenceUsers.getMembers().size());
	}



}
