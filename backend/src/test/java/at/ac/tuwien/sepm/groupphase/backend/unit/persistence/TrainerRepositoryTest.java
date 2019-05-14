package at.ac.tuwien.sepm.groupphase.backend.unit.persistence;

import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.pojo.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TrainerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TrainerRepository trainerRepository;

    private Trainer VALID_TRAINER = new Trainer("Test FN", "Test LN", LocalDate.now().minusDays(1), "01 234 56 7", "testFn.testLn@test.com", LocalDateTime.now(), LocalDateTime.now());
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


    @Test
    public void test_saveValidTrainer_shouldBeFound() {
        Trainer toBeSaved = trainerRepository.save(VALID_TRAINER);

        Trainer foundInDB = entityManager.find(Trainer.class, toBeSaved.getId());

        assertEquals(toBeSaved, foundInDB);
    }

    /**
     * ASSURE Invalid Values Are Not Allowed For Persistence
     */

    @Test
    public void test_saveInvalidTrainer_wrongAge_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_TOOLOW_AGE));
    }


    @Test
    public void test_saveInvalidTrainer_wrongNumber_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_NO_REAL_PHONE));
    }

    @Test
    public void test_saveInvalidTrainer_wrongEmail_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_NO_REAL_EMAIL));
    }

    @Test
    public void test_saveInvalidTrainer_wrongCreation_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_FUTURE_CREATION_TIME));
    }

    @Test
    public void test_saveInvalidTrainer_wrongUpdate_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_FUTURE_UPDATE_TIME));
    }

    /**
     * ASSURE ALL FIELDS ARE SET (Not blank if Strings, or Non Null For Non Text Types)
     */

    @Test
    public void test_saveInvalidTrainer_missingFN_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_MISSING_FN));
    }

    @Test
    public void test_saveInvalidTrainer_missingLN_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_MISSING_LN));
    }

    @Test
    public void test_saveInvalidTrainer_missingAge_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_MISSING_BIRTHDAY));
    }

    @Test
    public void test_saveInvalidTrainer_missingPhone_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_MISSING_PHONE));
    }

    @Test
    public void test_saveInvalidTrainer_missingMail_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_MISSING_MAIL));
    }

    @Test
    public void test_saveInvalidTrainer_missingCreated_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_MISSING_CREATED));
    }

    @Test
    public void test_saveInvalidTrainer_missingUpdated_shouldThrowException() {
        assertThrows(ConstraintViolationException.class, () -> trainerRepository.save(INVALID_TRAINER_MISSING_UPDATE));
    }


}
