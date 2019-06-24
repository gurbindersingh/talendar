package at.ac.tuwien.sepm.groupphase.backend.service.exceptions;

public class FileDeletionException extends Exception{

    public FileDeletionException(String info) {
        super(info);
    }

    public FileDeletionException(Throwable cause) {
        super(cause);
    }

    public FileDeletionException(String info, Throwable cause) {
        super(info, cause);
    }
}
