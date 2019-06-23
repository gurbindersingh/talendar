package at.ac.tuwien.sepm.groupphase.backend.tests.unit.service;


import at.ac.tuwien.sepm.groupphase.backend.schedule.ScheduleCaller;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class scheduledTaskTests {

    @Autowired
    private ScheduleCaller scheduleCaller;

    @Test
    private void validity_test_call_daily(){

    }

    @Test
    private void validity_test_call_weekly(){

    }

    @Test
    private void validity_test_call_monthly(){

    }
}
