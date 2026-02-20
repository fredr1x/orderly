package pp.userservice.exception;

import org.springframework.http.HttpStatus;

public class KeycloakException extends ApplicationException {

    public KeycloakException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
