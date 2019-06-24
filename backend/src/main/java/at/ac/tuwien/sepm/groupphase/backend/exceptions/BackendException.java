package at.ac.tuwien.sepm.groupphase.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BackendException extends Exception{

    public BackendException(Throwable cause) {
        super(cause);
    }

    public BackendException(String info, Throwable cause) {
        super(info,cause);
    }
}
