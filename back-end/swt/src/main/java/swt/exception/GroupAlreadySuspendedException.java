package swt.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class GroupAlreadySuspendedException extends BaseException {
    public GroupAlreadySuspendedException() {
        super("Group is already suspended", CONFLICT);
    }
}
