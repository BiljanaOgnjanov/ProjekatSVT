package swt.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class UsernameAlreadyExistsException extends BaseException {
    public UsernameAlreadyExistsException(String username) {
        super("User with the username '" + username + "' is already registered", CONFLICT);
    }
}
