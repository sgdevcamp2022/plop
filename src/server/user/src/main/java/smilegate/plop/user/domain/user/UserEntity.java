package smilegate.plop.user.domain.user;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import smilegate.plop.user.dto.UserDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ToString
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "user")

public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userid", nullable = false, unique = true)
    private String userId;
    @Column(nullable = false,length = 50, unique = true)
    private String email;
    @Column(name = "password")
    private String encryptedPwd;
    @Type(type = "json")
    @Column(name = "profile", columnDefinition = "longtext")
    private Map<String, Object> profile = new HashMap<>();
    @Column(name = "state")
    private Integer state;
    @Column(name = "user_role")
    private String role;

    @Type(type = "json")
    @Column(name = "device", columnDefinition = "longtext")
    private Map<String, Object> device = new HashMap<>();


    @Column(name = "access_at")
    private LocalDateTime accessAt;

    @Column(name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "fcm_token")
    private String fcmToken;

    public UserDto toUserDto() {
        UserDto userDto = UserDto.builder()
                .email(this.getEmail())
                .userId(this.getUserId())
                .encryptedPwd(this.getEncryptedPwd())
                .state(this.getState())
                .img(this.getProfile().get("img").toString())
                .nickname(this.getProfile().get("nickname").toString())
                .role(this.getRole())
                .build();
        return userDto;
    }
}
