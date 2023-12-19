package swt.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class InvalidTokenException extends BaseException {

    public InvalidTokenException() {
        super("Invalid JWT", UNAUTHORIZED);
    }
}
