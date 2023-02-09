package smilegate.plop.presence.domain.presence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PresenceRepository extends MongoRepository<PresenceCollection, String>, PresenceTemplateRepository {
    PresenceCollection findByUserId(String userId);
}
