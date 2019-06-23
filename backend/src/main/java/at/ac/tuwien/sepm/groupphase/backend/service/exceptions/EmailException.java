package at.ac.tuwien.sepm.groupphase.backend.service.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailException extends Exception{

    public EmailException(String info){super(info);}
}
