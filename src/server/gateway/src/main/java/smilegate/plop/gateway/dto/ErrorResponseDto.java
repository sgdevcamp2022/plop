package smilegate.plop.gateway.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
@Builder
public class ErrorResponseDto  {
    private final int status;
    private final String errorCode;
    private final String description;
    private final String errorMsg;
}