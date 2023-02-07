package smilegate.plop.user.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class RequestEmailVerification {
    private String userId;

    @Email
    private String email;

//    @NotNull(message = "Name cannot be null")
//    @Size(min=2, message = "Name not be less than two characters")
//    private String nickname;
//
//    @NotNull(message = "Password cannot be null")
//    @Size(min=8, message = "Password not be less than two characters")
//    private String password;
}
