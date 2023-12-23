package swt.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedPostEditException extends BaseException {

    public UnauthorizedPostEditException() {
        super("Unauthorized to edit this post", UNAUTHORIZED);
    }
}
