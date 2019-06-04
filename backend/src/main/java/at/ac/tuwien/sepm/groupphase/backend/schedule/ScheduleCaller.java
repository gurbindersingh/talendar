package at.ac.tuwien.sepm.groupphase.backend.schedule;

import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import com.itextpdf.text.DocumentException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduleCaller {

    private ParticipantsList participantsList;
    private RedactInactiveUsers redactInactiveUsers;

    public ScheduleCaller (ParticipantsList participantsList, RedactInactiveUsers redactInactiveUsers){
        this.participantsList = participantsList;
        this. redactInactiveUsers = redactInactiveUsers;

    }


    @Scheduled(cron = "30 * * * * *")
    public void callDaily() throws BackendException, DocumentException, EmailException, IOException {
        participantsList.createParticipantsList();
    }


    @Scheduled(cron = "0 * * * * *")
    public void callWeekly() throws BackendException {
        redactInactiveUsers.redact();
    }

}
