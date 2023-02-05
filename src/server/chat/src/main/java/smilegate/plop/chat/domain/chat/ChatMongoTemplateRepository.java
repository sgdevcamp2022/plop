package smilegate.plop.chat.domain.chat;

import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatMongoTemplateRepository {
    MessageCollection getLastMessage(String roomId);
    List<MessageCollection> getNewMessages(String roomId, String readMsgId);
    List<MessageCollection> getAllMessagesAtRoom(String roomId);
    Page<MessageCollection> findByRoomIdWithPagingAndFiltering(String roomId, int page, int size);
}
