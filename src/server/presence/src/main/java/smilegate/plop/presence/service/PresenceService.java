package smilegate.plop.presence.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import smilegate.plop.presence.domain.presence.PresenceCollection;
import smilegate.plop.presence.domain.presence.PresenceRepository;
import smilegate.plop.presence.dto.PresenceUserDto;
import smilegate.plop.presence.dto.response.ResponsePresenceUsers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceService {

    private final PresenceRepository presenceRepository;

    public ResponsePresenceUsers getOnlineUsersPresence(List<String> users){
        List<PresenceCollection> presenceCollections = presenceRepository.findByUserIdInUsers(users);
        List<String> members = presenceCollections.stream().map(PresenceCollection::getUserId).collect(Collectors.toList());
        return new ResponsePresenceUsers(members);
    }

    public ResponsePresenceUsers getOfflineUsersPresence(List<String> users){
        List<PresenceCollection> presenceCollections = presenceRepository.findOfflineByUserIdInUsers(users);
        List<String> members = presenceCollections.stream().map(PresenceCollection::getUserId).collect(Collectors.toList());
        return new ResponsePresenceUsers(members);
    }


    public PresenceUserDto presenceOn(String userId) {
        return changePresence(userId, "online");
    }

    public PresenceUserDto presenceOff(String userId) {
        return changePresence(userId, "offline");
    }

    private PresenceUserDto convertToDto(PresenceCollection presenceCollection){
        return PresenceUserDto.builder()
                .user_id(presenceCollection.getUserId())
                .status(presenceCollection.getStatus())
                .build();
    }

    public PresenceUserDto changePresence(String userId, String status){
        PresenceCollection presenceCollection = presenceRepository.findByUserId(userId);
        if(presenceCollection == null){
            PresenceCollection saved = presenceRepository.save(PresenceCollection.builder()
                    .userId(userId)
                    .status(status)
                    .lastActiveAt(LocalDateTime.now())
                    .build()
            );
            return convertToDto(saved);
        }
        presenceCollection.setStatus(status);
        presenceCollection.setLastActiveAt(LocalDateTime.now());
        return convertToDto(presenceRepository.save(presenceCollection));
    }
}
