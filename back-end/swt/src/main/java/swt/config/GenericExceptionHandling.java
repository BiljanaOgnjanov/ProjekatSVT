package swt.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import swt.util.ApiResponse;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GenericExceptionHandling {

    private static final Logger LOGGER = LogManager.getLogger(BaseExceptionHandling.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {

        if (exception instanceof NoResourceFoundException || exception instanceof HttpRequestMethodNotSupportedException) {
            LOGGER.warn("Wrong URL or HTTP method", exception);
            return new ResponseEntity<>(
                    new ApiResponse(
                            false,
                            "Resource not found",
                            LocalDateTime.now()),
                    NOT_FOUND
            );
        }

        if (exception instanceof AccessDeniedException) {
            LOGGER.warn("Unauthorized access attempt: User attempted to access data without sufficient permissions");

            return new ResponseEntity<>(
                    new ApiResponse(
                            false,
                            "Access denied",
                            LocalDateTime.now()),
                    FORBIDDEN
            );
        }

        LOGGER.error("Unexpected exception", exception);
        return new ResponseEntity<>(
                new ApiResponse(
                        false,
                        "Oops, an unexpected error has occurred! Sorry for the inconvenience, please try again later.",
                        LocalDateTime.now()),
                INTERNAL_SERVER_ERROR
        );
    }
}
