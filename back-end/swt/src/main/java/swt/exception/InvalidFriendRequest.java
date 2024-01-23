package swt.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InvalidFriendRequest extends BaseException {

    public InvalidFriendRequest() {
        super("The provided friend request ID is not valid or cannot be processed by the user", BAD_REQUEST);
    }
}
