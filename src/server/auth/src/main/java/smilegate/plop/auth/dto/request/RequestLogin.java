package smilegate.plop.auth.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {
//    @NotNull(message = "Email cannot be null")
//    @Size(min = 2, message = "Email not be less than two characters")
//    @Email
    private String idOrEmail;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be equals or greater than 8 chracters")
    private String password;
}