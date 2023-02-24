package smilegate.plop.presence.domain.presence;

import java.util.List;

public interface PresenceTemplateRepository {
    List<PresenceCollection> findByUserIdInUsers(List<String> users);
    List<PresenceCollection> findOfflineByUserIdInUsers(List<String> users);
}
