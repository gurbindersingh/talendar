package at.ac.tuwien.sepm.groupphase.backend.exceptions;

public class TimeNotAvailableException extends Exception {


    public TimeNotAvailableException (Throwable cause) {super(cause);}

    public TimeNotAvailableException(String info){super(info);}

    public TimeNotAvailableException(String info, Throwable cause){super(info, cause);}
}
