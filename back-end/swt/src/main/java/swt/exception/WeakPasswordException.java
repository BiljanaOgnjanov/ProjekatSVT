package swt.exception;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class WeakPasswordException extends BaseException {

    public WeakPasswordException() {
        super("Weak password", UNPROCESSABLE_ENTITY);
    }
}
