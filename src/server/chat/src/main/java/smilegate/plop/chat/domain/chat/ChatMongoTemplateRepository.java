package smilegate.plop.chat.domain.chat;

import java.util.List;

public interface ChatMongoTemplateRepository {
    List<MessageCollection> getAllLastMessage(List<String> roomIds);

}
