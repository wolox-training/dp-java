package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super("Book Already Owned");
    }
}
