package smilegate.plop.presence.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ResponseDto {
    private final String result;
    private final String message;
    private final List<ResponseProfile> data;
}