package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super("Book Already Owned");
    }

    public BookAlreadyOwnedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyOwnedException(final String message) {
        super(message);
    }

    public BookAlreadyOwnedException(final Throwable cause) {
        super(cause);
    }
}
