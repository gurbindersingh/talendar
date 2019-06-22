package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.service.ICustomerService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:8080" })
@RestController
@RequestMapping("/api/v1/talendar/customers")
public class CustomerEndpoint {
    private static Logger LOGGER = LoggerFactory.getLogger(CustomerEndpoint.class);
    private final ICustomerService customerService;

    @Autowired
    public CustomerEndpoint(
        ICustomerService customerService
    ) {
        this.customerService = customerService;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{email}")
    @ResponseStatus(HttpStatus.OK)
    public void setWantsEmailFalse(@PathVariable("email") String email){
        customerService.setWantsEmailFalse(email);
    }
}
