package at.ac.tuwien.sepm.groupphase.backend.tests.unit.service;



import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IEventService;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.Trainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EventServiceTest {

    @Autowired
    private IEventService eventService;
    @Autowired
    private Validator validator;
    @Autowired
    private static FakeData faker = new FakeData();
    @Autowired
    private static ITrainerService trainerService;

    @MockBean
    private EventRepository eventRepository;

    private static EventDto VALID_INCOMING_BIRTHDAY = faker.fakeBirthday();


    @BeforeAll
    public static void init(){
        //fill the database with a few trainers
        //postTrainers();

    }

    @Test
    public void test_saveValidEvent_EventShouldBeAccepted(){

    }
    public static void postTrainers(){
        for(int i = 0; i <= 100; i++){
            Trainer t = faker.fakeNewTrainerEntity();
            try {
                trainerService.save(t);
            }catch(ValidationException e){
                System.out.println("System Validation Error creating Test Data");
            }catch(ServiceException e){
                System.out.println("System Error Creating Test Data");
            }
        }
    }
}
