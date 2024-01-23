package swt.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class GroupCreationException extends BaseException {

    public GroupCreationException() {
        super("Error creating group", INTERNAL_SERVER_ERROR);
    }
}
