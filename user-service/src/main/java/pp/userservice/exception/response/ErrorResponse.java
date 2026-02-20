package pp.userservice.exception.response;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    int statusCode;
    String message;
    String path;
    Instant timestamp;
}
