package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
        super("Ids mismatch");
    }

    public UserIdMismatchException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserIdMismatchException(final String message) {
        super(message);
    }

    public UserIdMismatchException(final Throwable cause) {
        super(cause);
    }
}
