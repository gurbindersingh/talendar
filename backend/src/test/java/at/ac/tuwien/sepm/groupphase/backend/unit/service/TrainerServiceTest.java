package at.ac.tuwien.sepm.groupphase.backend.unit.service;


import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.pojo.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.service.ITrainerService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TrainerServiceTest {

    @Autowired
    private ITrainerService trainerService;

    @MockBean
    private TrainerRepository trainerRepository;

    private Trainer VALID_TRAINER = new Trainer("Test FN", "Test LN", LocalDate.now().minusYears(20), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private static Trainer PERSISTED_TRAINER =  new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_MISSING_FN = new Trainer("", "Test LN", LocalDate.now().minusDays(1) , "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_MISSING_LN = new Trainer("Test FN", "", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_MISSING_BIRTHDAY = new Trainer("Test FN", "LN", null, "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_MISSING_PHONE = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_MISSING_MAIL = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_MISSING_CREATED = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn@test.com", null, LocalDateTime.now());
    private Trainer INVALID_TRAINER_MISSING_UPDATE = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), null);

    private Trainer INVALID_TRAINER_TOOLOW_AGE = new Trainer("Test FN", "Test LN", LocalDate.now(), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_NO_REAL_PHONE = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 ABC 34", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_NO_REAL_EMAIL = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn", LocalDateTime.now(), LocalDateTime.now());
    private Trainer INVALID_TRAINER_FUTURE_CREATION_TIME = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now().plusDays(1), LocalDateTime.now());
    private Trainer INVALID_TRAINER_FUTURE_UPDATE_TIME = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now().plusDays(1));


    @BeforeAll
    public static void init() {
        PERSISTED_TRAINER.setId(1l);
    }

    @Test
    public void test_saveTrainer_valid_shouldSucceed() throws Exception{
        when(trainerRepository.save(VALID_TRAINER)).thenReturn(PERSISTED_TRAINER);

        Trainer persistedTrainerAsReturnedByMock = trainerService.save(VALID_TRAINER);

        assertNotNull(persistedTrainerAsReturnedByMock.getId());
    }


    /***
     * When The Persistence Level Throws An Exception This Exception Is Accurately Passed On
     */

    public void test_backednThrowsEception_serviceShouldHandleIt() {
        when(trainerRepository.saveAll(Mockito.any())).thenThrow(PersistenceException.class);

        assertThrows(ServiceException.class, () -> trainerService.save(Mockito.any()));
    }

    /**
     * Now Do Not Consider Persistence
     * Service Level Has To Detect Invalid Entities
     */

    @Test
    public void test_saveInvalidTrainer_wrongAge_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_TOOLOW_AGE));
    }


    @Test
    public void test_saveInvalidTrainer_wrongNumber_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_NO_REAL_PHONE));
    }

    @Test
    public void test_saveInvalidTrainer_wrongEmail_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_NO_REAL_EMAIL));
    }

    @Test
    public void test_saveInvalidTrainer_wrongCreation_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_FUTURE_CREATION_TIME));
    }

    @Test
    public void test_saveInvalidTrainer_wrongUpdate_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_FUTURE_UPDATE_TIME));
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

    @Test
    public void test_saveInvalidTrainer_missingCreated_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_MISSING_CREATED));
    }

    @Test
    public void test_saveInvalidTrainer_missingUpdated_shouldThrowException() {
        assertThrows(ValidationException.class, () -> trainerService.save(INVALID_TRAINER_MISSING_UPDATE));
    }



}
