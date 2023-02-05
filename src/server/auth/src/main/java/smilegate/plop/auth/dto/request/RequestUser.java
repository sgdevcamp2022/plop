package smilegate.plop.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {
    @NotNull(message = "ID cannot be null")
    @Size(min=2, message = "ID not be less than two characters")
    private String userId;

    @NotNull(message = "Email cannot be null")
    @Size(min=2, message = "Email not be less than two characters")
    @Email
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min=2, message = "Name not be less than two characters")
    private String nickname;

    @NotNull(message = "Password cannot be null")
    @Size(min=8, message = "Password not be less than two characters")
    private String password;
}