package at.ac.tuwien.sepm.groupphase.backend.tests.unit.service;


import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrainerServiceTest {

    @Autowired
    private ITrainerService trainerService;
    @Autowired
    private Validator validator;

    @MockBean
    private TrainerRepository trainerRepository;

    @Autowired
    private static FakeData trainerFaker = new FakeData();

    /**
     * PREPARE Trainer Instances Used For Testing Of Service Layer (INCLUDING VALIDATION TEST)
     *
     * Create Initial All Valid Trainers
     */

    private static Trainer VALID_INCOMING_TRAINER = trainerFaker.fakeTrainerEntity();
    private static Trainer PERSISTED_TRAINER = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_MISSING_FN = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_MISSING_LN = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_MISSING_BIRTHDAY = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_MISSING_PHONE = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_MISSING_MAIL = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_MISSING_CREATED = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_MISSING_UPDATE = trainerFaker.fakeTrainerEntity();

    private static Trainer INVALID_TRAINER_TOO_LOW_AGE = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_NO_REAL_PHONE = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_NO_REAL_EMAIL = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_FUTURE_CREATION_TIME = trainerFaker.fakeTrainerEntity();
    private static Trainer INVALID_TRAINER_FUTURE_UPDATE_TIME = trainerFaker.fakeTrainerEntity();


    /**
     * Prepare Each Instance Individually
     */

    @BeforeAll
    public static void init() {
        // a incoming trainer has no assigned ID, no creation or updated stamp
        VALID_INCOMING_TRAINER.setId(null);
        VALID_INCOMING_TRAINER.setCreated(null);
        VALID_INCOMING_TRAINER.setUpdated(null);

        // remove one property each of each type of invalid trainers
        INVALID_TRAINER_MISSING_FN.setFirstName("");
        INVALID_TRAINER_MISSING_LN.setLastName("");
        INVALID_TRAINER_MISSING_BIRTHDAY.setBirthday(null);
        INVALID_TRAINER_MISSING_PHONE.setPhone("");
        INVALID_TRAINER_MISSING_MAIL.setEmail("");
        INVALID_TRAINER_MISSING_CREATED.setCreated(null);
        INVALID_TRAINER_MISSING_UPDATE.setUpdated(null);
        INVALID_TRAINER_TOO_LOW_AGE.setBirthday(LocalDate.now().minusYears(15));
        INVALID_TRAINER_NO_REAL_PHONE.setPhone("123abc456");
        INVALID_TRAINER_NO_REAL_EMAIL.setEmail("testFn.testLn");
        INVALID_TRAINER_FUTURE_CREATION_TIME.setCreated(LocalDateTime.now().plusDays(1));
        INVALID_TRAINER_FUTURE_UPDATE_TIME.setUpdated(LocalDateTime.now().plusDays(1));
    }


    /**
     * Test If Given Valid Trainer Instance (Incoming) Is Accepted By The Service Layer
     * After The Service Handles This Request, The Trainer's Created And Updated Stamps Have
     * To Be Set
     */

    @Test
    public void test_saveValidTrainer_TrainerShouldBeAccepted() throws Exception {
        // just mock it out because we only test service logic
        when(trainerRepository.save(any(Trainer.class))).thenReturn(PERSISTED_TRAINER);
        trainerService.save(VALID_INCOMING_TRAINER);

        assertNotNull(VALID_INCOMING_TRAINER.getCreated());
        assertNotNull(VALID_INCOMING_TRAINER.getUpdated());
        assertFalse(VALID_INCOMING_TRAINER.getCreated().isAfter(VALID_INCOMING_TRAINER.getUpdated()));
    }


    /**
     * Service Level Has To Detect Invalid Entities
     */

    @Test
    public void test_saveInvalidTrainer_wrongAge_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_TOO_LOW_AGE));
    }


    @Test
    public void test_saveInvalidTrainer_wrongNumber_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_NO_REAL_PHONE));
    }

    @Test
    public void test_saveInvalidTrainer_wrongEmail_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_NO_REAL_EMAIL));
    }


    /**
     * ASSURE ALL FIELDS ARE SET (Not blank if Strings, or Non Null For Non Text Types)
     */

    @Test
    public void test_saveInvalidTrainer_missingFN_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_MISSING_FN));
    }

    @Test
    public void test_saveInvalidTrainer_missingLN_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_MISSING_LN));
    }

    @Test
    public void test_saveInvalidTrainer_missingAge_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_MISSING_BIRTHDAY));
    }

    @Test
    public void test_saveInvalidTrainer_missingPhone_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_MISSING_PHONE));
    }

    @Test
    public void test_saveInvalidTrainer_missingMail_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_MISSING_MAIL));
    }





    /**
     * UPDATE TESTED
     *
     *
     * Test Update Mechanism: Consider Repeating Each Test Is Not Sensible.
     *
     * Premise. Validations Are All Done Within A Separate Class, Either This Class Fails On Save
     * Too Or Updates Could Only Fail If The Validator Is Not Applied
     *
     * ==> just test that there is a validator throwing exception on bad arguments in the first place
     */

    @Test
    public void test_updateWithInvalidTrainer_missingEmail_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.update(INVALID_TRAINER_MISSING_MAIL));
    }

    @Test
    public void test_updateWithInvalidTrainer_missingPhone_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.update(INVALID_TRAINER_MISSING_PHONE));
    }





    /**
     * Test Validator Exclusively: Because Date Stamps Are Set Within Service Correctly Which Makes
     * It Impossible To Test Validator Correctness
     * Thus Simple Test Validator Directly
     */

    @Test
    public void test_saveInvalidTrainer_missingCreated_shouldThrowException() {
        assertThrows(InvalidEntityException.class, () -> validator.validateTrainer(INVALID_TRAINER_MISSING_CREATED));
    }

    @Test
    public void test_saveInvalidTrainer_missingUpdated_shouldThrowException() {
        assertThrows(InvalidEntityException.class, () ->validator.validateTrainer(INVALID_TRAINER_MISSING_UPDATE));
    }

    @Test
    public void test_saveInvalidTrainer_wrongCreation_shouldThrowException() {
        assertThrows(InvalidEntityException.class, () -> validator.validateTrainer(INVALID_TRAINER_FUTURE_CREATION_TIME));
    }

    @Test
    public void test_saveInvalidTrainer_wrongUpdate_shouldThrowException() {
        assertThrows(InvalidEntityException.class, () -> validator.validateTrainer(INVALID_TRAINER_FUTURE_UPDATE_TIME));
    }

}
