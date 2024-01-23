package swt.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import swt.exception.BaseException;
import swt.util.ApiResponse;

import java.time.LocalDateTime;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BaseExceptionHandling {

    private static final Logger LOGGER = LogManager.getLogger(BaseExceptionHandling.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse> handleException(BaseException exception) {

        LOGGER.info(exception + " thrown");
        return new ResponseEntity<>(
                new ApiResponse(
                        false,
                        exception.getMessage(),
                        LocalDateTime.now()),
                exception.getStatus()
        );
    }
}
