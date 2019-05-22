package at.ac.tuwien.sepm.groupphase.backend.exceptions;

public class TrainerNotAvailableException extends Exception{

    public TrainerNotAvailableException (Throwable cause) {super(cause);}

    public TrainerNotAvailableException(String info){super(info);}

    public TrainerNotAvailableException(String info, Throwable cause){super(info, cause);}
    
}
