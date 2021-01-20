package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super("Ids mismatch");
    }
}
