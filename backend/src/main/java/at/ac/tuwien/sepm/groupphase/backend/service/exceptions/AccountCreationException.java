package at.ac.tuwien.sepm.groupphase.backend.service.exceptions;

public class AccountCreationException extends Exception {

    public AccountCreationException(String info) {
        super(info);
    }

    public AccountCreationException(String info, Throwable cause) {
        super(info, cause);
    }
}
