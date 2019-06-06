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
    private RedactInactiveTrainers redactInactiveTrainers;

    public ScheduleCaller (ParticipantsList participantsList,
                           RedactInactiveUsers redactInactiveUsers,
                           RedactInactiveTrainers redactInactiveTrainers){
        this.participantsList = participantsList;
        this.redactInactiveUsers = redactInactiveUsers;
        this.redactInactiveTrainers = redactInactiveTrainers;
    }


    //Change to daily 23:00
    @Scheduled(cron = "0 0 23 * * ?")
    public void callDaily() throws BackendException, DocumentException, EmailException, IOException {
        participantsList.createParticipantsList();
    }


    //Change to weekly 02:00 on sunday
    @Scheduled(cron = "0 0 2 ? * SUN")
    public void callWeekly() throws BackendException {
        redactInactiveUsers.redact();
    }

    //Monthly 00:00, on the first day of every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public void callMonthly() throws BackendException {
        redactInactiveTrainers.redact();
    }
}