package at.ac.tuwien.sepm.groupphase.backend.testObjects.exceptions;

import org.springframework.dao.DataAccessException;

public class TestJpaException extends DataAccessException {

    public TestJpaException (String msg) {
        super(msg);
    }


    public TestJpaException (String msg, Throwable cause) {
        super(msg, cause);
    }
}
