package swt.exception;

import static org.springframework.http.HttpStatus.CONFLICT;

public class GroupRequestAlreadyProcessedException extends BaseException {

    public GroupRequestAlreadyProcessedException() {
        super("Group request is already processed", CONFLICT);
    }
}
