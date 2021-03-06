package at.ac.tuwien.sepm.groupphase.backend.tests.unit.service;


import at.ac.tuwien.sepm.groupphase.backend.datagenerator.TestDataGenerator;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.AlgorithmService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.InfoMail;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AlgorithmTests {

    private static Logger LOGGER = LoggerFactory.getLogger(AlgorithmTests.class);

    @Mock
    private InfoMail infoMail;

    @Autowired
    @InjectMocks
    private AlgorithmService algorithmService;

    @Autowired
    private TestDataGenerator testDataGenerator;


    @Test
    public void dry_Run(){
        try {
            algorithmService.algorithm();
        }catch(EmailException e){
            LOGGER.error("test_Run Email error");
        }
    }

    @Test
    public void small_Run(){

        testDataGenerator.fillDatabase(40);
        try {
            algorithmService.algorithm();
        }catch(EmailException e){
            LOGGER.error("test_Run Email error");
        }
    }

    @Test
    public void big_Run(){
        testDataGenerator.fillDatabase(100);
        try {
            algorithmService.algorithm();
        }catch(EmailException e){
            LOGGER.error("test_Run Email error");
        }
    }
}
