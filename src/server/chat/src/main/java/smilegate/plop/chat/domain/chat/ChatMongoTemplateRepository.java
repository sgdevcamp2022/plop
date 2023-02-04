package smilegate.plop.chat.domain.chat;

import java.util.List;

public interface ChatMongoTemplateRepository {
    List<MessageCollection> getAllLastMessage(List<String> roomIds);

    List<MessageCollection> getNewMessages(String roomId, String readMsgId);

    List<MessageCollection> getAllMessagesAtRoom(String roomId);
}
