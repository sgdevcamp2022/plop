package smilegate.plop.user.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RequestProfile {
    String target;
    String nickname;
    MultipartFile img;
}
