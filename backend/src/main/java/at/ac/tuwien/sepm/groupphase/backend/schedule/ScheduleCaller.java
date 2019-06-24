package at.ac.tuwien.sepm.groupphase.backend.schedule;

import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.AlgorithmService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ScheduleCaller {

    private ParticipantsList participantsList;
    private RedactInactiveUsers redactInactiveUsers;
    private RedactInactiveTrainers redactInactiveTrainers;
    private GarbageImageCollector garbageImageCollector;
    private AlgorithmService algorithmService;

    @Autowired
    public ScheduleCaller (ParticipantsList participantsList,
                           RedactInactiveUsers redactInactiveUsers,
                           RedactInactiveTrainers redactInactiveTrainers,
                           AlgorithmService algorithmService,
                           GarbageImageCollector garbageImageCollector){
        this.participantsList = participantsList;
        this.redactInactiveUsers = redactInactiveUsers;
        this.redactInactiveTrainers = redactInactiveTrainers;
        this.algorithmService = algorithmService;
        this.garbageImageCollector = garbageImageCollector;
    }


    //Daily 23:00
    @Scheduled(cron = "0 0 23 * * ?")
    public void callDaily() throws BackendException, DocumentException, EmailException, IOException{
        participantsList.createParticipantsList();
    }


    //Weekly 02:00 on sunday
    @Scheduled(cron = "0 0 2 ? * SUN")
    public void callWeekly() throws BackendException {
        redactInactiveUsers.redact();
    }

    //Monthly 00:00, on the first day of every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public void callMonthly() throws BackendException {
        redactInactiveTrainers.redact();
        // sometimes files can not be deleted on the fly, therfore garbage collect later
        garbageImageCollector.deleteLegacyProfilePictures();
    }

    @Scheduled(cron = "0 0 3 ? * MON")
    public void callAlgorithm()throws EmailException{
        algorithmService.algorithm();
    }
}
