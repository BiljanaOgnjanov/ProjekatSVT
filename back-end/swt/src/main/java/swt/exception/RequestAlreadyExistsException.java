package swt.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class RequestAlreadyExistsException extends BaseException {

    public RequestAlreadyExistsException(String message) {
        super("You already have " + message  + " request", CONFLICT);
    }
}
