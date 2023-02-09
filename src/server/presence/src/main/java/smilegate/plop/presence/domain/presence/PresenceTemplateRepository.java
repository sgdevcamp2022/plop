package smilegate.plop.presence.domain.presence;

import java.util.List;

public interface PresenceTemplateRepository {
    List<PresenceCollection> findByUserIdInFriends(List<String> friends);
}
