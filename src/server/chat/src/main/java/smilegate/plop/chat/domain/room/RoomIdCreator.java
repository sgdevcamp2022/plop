package smilegate.plop.chat.domain.room;

import java.util.UUID;

public class RoomIdCreator {
    public static String createRoomId(){
        return UUID.randomUUID().toString();
    }
}
