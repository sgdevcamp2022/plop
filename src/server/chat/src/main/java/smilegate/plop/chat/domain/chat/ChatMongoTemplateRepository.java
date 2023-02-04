package smilegate.plop.chat.domain.chat;

import java.util.List;

public interface ChatMongoTemplateRepository {
    MessageCollection getLastMessage(String roomId);
    List<MessageCollection> getNewMessages(String roomId, String readMsgId);
    List<MessageCollection> getAllMessagesAtRoom(String roomId);
}
