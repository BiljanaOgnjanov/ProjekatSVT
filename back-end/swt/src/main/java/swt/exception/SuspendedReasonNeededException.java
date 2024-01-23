package swt.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class SuspendedReasonNeededException extends BaseException {
    public SuspendedReasonNeededException() {
        super("Suspension reason is required", BAD_REQUEST);
    }
}
