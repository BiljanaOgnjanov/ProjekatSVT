package swt.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class WrongPasswordException extends BaseException {

    public WrongPasswordException() {
        super("Wrong password", FORBIDDEN);
    }
}
