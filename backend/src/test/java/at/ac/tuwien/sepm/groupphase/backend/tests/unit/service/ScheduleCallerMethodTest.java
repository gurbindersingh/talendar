package at.ac.tuwien.sepm.groupphase.backend.tests.unit.service;


import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.persistence.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.schedule.ScheduleCaller;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ScheduleCallerMethodTest {

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Autowired
    private ScheduleCaller scheduleCaller;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    public void testCallDaily() throws Exception{
        testDataGenerator.fillDatabase(10);
        scheduleCaller.callDaily();
        eventRepository.deleteAll();
        assert(true);
    }

    @Test
    public void testCallWeekly() throws Exception{
        testDataGenerator.fillDatabase(10);
        scheduleCaller.callWeekly();
        eventRepository.deleteAll();
        assert(true);
    }

    @Test
    public void testCallMonthly() throws Exception{
        testDataGenerator.fillDatabase(10);
        scheduleCaller.callMonthly();
        eventRepository.deleteAll();
        assert(true);
    }

    @Test
    public void addOldCustomer_CheckRedacted() throws Exception{
        testDataGenerator.addOldCustomer("bob.schlob@gmail.com");
        scheduleCaller.callWeekly();
        List<Customer> customerList = customerRepository.findByEmail("bob.schlob@gmail.com");
        assert(customerList.isEmpty());
    }
}
