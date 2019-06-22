package at.ac.tuwien.sepm.groupphase.backend.schedule;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Component
public class RedactInactiveTrainers {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedactInactiveTrainers.class);

    private final TrainerRepository trainerRepository;

    @Autowired
    public RedactInactiveTrainers (TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }
    @Transactional
    public void redact() {
        List<Trainer> trainers = trainerRepository.findAll();
        List<Trainer> redactThese = new LinkedList<>();
        for(int i = 0; i < trainers.size(); i++) {
            //TODO: Codelogic für trainers mit deleted flag und updates mehr als 30 jahre her in redactThese reinspeicher.
        }
        redactThese = changeValuesToRedact(redactThese);

        try {
            for(int i = 0; i < redactThese.size(); i++) {
                trainerRepository.save(redactThese.get(i));
            }
        } catch(Exception e){
            LOGGER.error("Failed to save back the redacted Trainers");
        }
    }

    public List<Trainer> changeValuesToRedact (List<Trainer> redactThese) {
        for(int i = 0; i < redactThese.size(); i++) {
            redactThese.get(i).setPhone("redacted");
            redactThese.get(i).setBirthday(LocalDate.of(0, 0, 0));
            redactThese.get(i).setEmail("redacted@redacted.at");
            redactThese.get(i).setLastName("redacted");
            redactThese.get(i).setFirstName("redacted");
        }
        return redactThese;
    }
}
