package swt.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super("Wrong username or password", FORBIDDEN);
    }
}
