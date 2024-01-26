package swt.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedEditException extends BaseException {

    public UnauthorizedEditException(String text) {
        super("Unauthorized to edit this " + text, UNAUTHORIZED);
    }
}
