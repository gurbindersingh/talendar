package at.ac.tuwien.sepm.groupphase.backend.service.exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException (String info) {
        super(info);
    }
}
