package smilegate.plop.user.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class RequestVerificationCode {
    private String userId;

    @Email
    private String email;

    private String verificationCode;
}

