package pp.userservice.exception;

import org.springframework.http.HttpStatus;

public class RegistrationException extends ApplicationException {

    public RegistrationException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
