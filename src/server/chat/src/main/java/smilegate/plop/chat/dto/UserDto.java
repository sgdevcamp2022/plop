package smilegate.plop.chat.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private String userId;
    private String email;
    private String password;
    private String encryptedPwd;
    private String nickname;
    private String img;
    private int state;
    private String role;
}