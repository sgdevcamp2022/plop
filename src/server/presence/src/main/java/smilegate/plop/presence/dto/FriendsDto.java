package smilegate.plop.presence.dto;

import lombok.Data;

import java.util.List;

@Data
public class FriendsDto {
    private List<UserDto> profiles;
}
