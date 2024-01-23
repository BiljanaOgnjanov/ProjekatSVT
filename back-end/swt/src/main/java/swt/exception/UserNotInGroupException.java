package swt.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class UserNotInGroupException extends BaseException {

    public UserNotInGroupException() {
        super("You are not a member of group", FORBIDDEN);
    }
}
