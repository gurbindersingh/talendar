package at.ac.tuwien.sepm.groupphase.backend.service;


import org.springframework.stereotype.Component;

@Component
public interface ICustomerService {

    /**
     * Set wantsEmail to false
     * @param email to find whose variable (wants emeail) has to be set to false
     */
    void setWantsEmailFalse(String email);
}
