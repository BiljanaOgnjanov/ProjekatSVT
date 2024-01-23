package swt.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;

public class UserNotGroupAdministratorException extends BaseException {

    public UserNotGroupAdministratorException() {
        super("You are not an administrator of group", FORBIDDEN);
    }
}
