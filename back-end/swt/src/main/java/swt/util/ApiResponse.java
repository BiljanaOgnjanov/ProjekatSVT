package swt.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
}
