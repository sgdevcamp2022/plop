package smilegate.plop.push.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ErrorResponseDto  {
//    private final int errorCode;
//    private final String message;
   private final int status;
   private final String errorCode;
   private final String description;
   private final String errorMsg;
}
