package swt.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FieldBlankException extends BaseException {

    public FieldBlankException(String field) {
        super(field + " cannot be blank", BAD_REQUEST);
    }
}
