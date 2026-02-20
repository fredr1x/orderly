package pp.userservice.exception;

import org.springframework.http.HttpStatus;

public class EmailInUseException extends ApplicationException {

    public EmailInUseException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
