package at.ac.tuwien.sepm.groupphase.backend.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

public class BackendException extends Exception{
    public BackendException(Throwable cause) {
        super(cause);
    }

    public BackendException(String info, Throwable cause) {
        super(info,cause);
    }
}
