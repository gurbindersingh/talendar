package at.ac.tuwien.sepm.groupphase.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {

    public NotFoundException(String info) {
        super(info);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String info, Throwable cause) {
        super(info,cause);
    }
}
