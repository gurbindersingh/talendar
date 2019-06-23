package at.ac.tuwien.sepm.groupphase.backend.tests.unit.service;


import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.schedule.ScheduleCaller;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ScheduledTaskTests {

    private static Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskTests.class);

    @Autowired
    private ScheduleCaller scheduleCaller;

    @Autowired
    private TestDataGenerator testDataGenerator;

    @Test
    public void validity_test_call_daily() throws Exception{
        testDataGenerator.fillDatabase(10);
        scheduleCaller.callDaily();
        assert(true);
    }

    @Test
    public void validity_test_call_weekly() throws Exception{
        testDataGenerator.fillDatabase(10);
        scheduleCaller.callWeekly();
        assert(true);
    }

    @Test
    public void validity_test_call_monthly()throws Exception{
        testDataGenerator.fillDatabase(10);
        scheduleCaller.callMonthly();
        assert(true);
    }
}
