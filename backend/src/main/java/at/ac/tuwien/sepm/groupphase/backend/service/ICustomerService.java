package at.ac.tuwien.sepm.groupphase.backend.service;


import org.springframework.stereotype.Component;

@Component
public interface ICustomerService {

    void setWantsEmailFalse(String email);
}
