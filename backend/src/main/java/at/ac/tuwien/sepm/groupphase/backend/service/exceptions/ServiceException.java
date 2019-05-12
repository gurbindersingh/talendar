package at.ac.tuwien.sepm.groupphase.backend.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// can be specified to let spring automatically mep thrown exception to accurate HTTP response
//
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServiceException extends Exception {

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String info, Throwable cause) {
        super(info, cause);
    }
}
