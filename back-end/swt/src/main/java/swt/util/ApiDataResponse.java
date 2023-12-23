package swt.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ApiDataResponse extends ApiResponse {

    private Object data;

    public ApiDataResponse(boolean success, String message, LocalDateTime timestamp, Object data) {
        super(success, message, timestamp);
        this.data = data;
    }
}
