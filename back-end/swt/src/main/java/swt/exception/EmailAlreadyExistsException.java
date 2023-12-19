package swt.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(String email) {
        super("User with the email '" + email + "' is already registered", CONFLICT);
    }
}
