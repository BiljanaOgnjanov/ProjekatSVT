package swt.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import swt.exception.BaseException;
import swt.util.ApiResponse;

import java.time.LocalDateTime;

@ControllerAdvice
public class RuntimeErrorHandling {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse> baseExceptionHandler(BaseException exception) {
        return new ResponseEntity<>(new ApiResponse(false, exception.getMessage(), LocalDateTime.now()), exception.getStatus());
    }
}
