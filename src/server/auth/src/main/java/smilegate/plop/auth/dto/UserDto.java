package smilegate.plop.auth.dto;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
