package swt.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ItemNotFoundException extends BaseException {

    public ItemNotFoundException(String item) {
        super(item + " not found", NOT_FOUND);
    }
}
