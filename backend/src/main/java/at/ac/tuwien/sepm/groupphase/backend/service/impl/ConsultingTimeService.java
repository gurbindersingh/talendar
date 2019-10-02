package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.ConsultingTime;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.ConsultingTimeRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.HolidayRepository;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.ConsultingTimesDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidaysDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IConsultingTimeService;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


@Service
public class ConsultingTimeService implements IConsultingTimeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsultingTimeService.class);

    private final ConsultingTimeRepository consultingTimeRepository;
    private final TrainerRepository trainerRepository;
    private final Validator validate;

    @Autowired
    public ConsultingTimeService(ConsultingTimeRepository consultingTimeRepository, TrainerRepository trainerRepository, Validator validate) {
        this.consultingTimeRepository = consultingTimeRepository;
        this.trainerRepository = trainerRepository;
        this.validate = validate;
    }


    @Override
    public LinkedList<ConsultingTime> getAllConsultingTimesByTrainerId(Long id) throws ServiceException, NotFoundException {
        LOGGER.info("Get all consultingTimes by trainer id " + id);
        if(!this.trainerRepository.existsById(id)){
            InvalidEntityException e = new InvalidEntityException("Trainer existiert nicht!");
            LOGGER.error("No trainer found when searching for all consultingTimes by trainer id");
            throw new NotFoundException(e.getMessage(), e);
        }
        LinkedList<ConsultingTime> result = new LinkedList<>();
        List<ConsultingTime> db = consultingTimeRepository.findByTrainer_Id(id);
        for(int i = 0; i < db.size(); i++){
            result.add(db.get(i));
        }
        LOGGER.info("" + result);
        return result;
    }

    @Override
    public LinkedList<ConsultingTime> getAllConsultingTimes() throws ServiceException, NotFoundException {

        LinkedList<ConsultingTime> result = new LinkedList<>();
        List<ConsultingTime> db = consultingTimeRepository.findAll();
        for(int i = 0; i < db.size(); i++){
            result.add(db.get(i));
        }
        LOGGER.info("" + result);
        return result;
    }

    @Override
    public ConsultingTime save(ConsultingTime consultingTime) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new ConsultingTime: {}", consultingTime);

        try {
            validate.validateConsultingTime(consultingTime);
            consultingTime.setGroupId(Instant.now().toEpochMilli());
            if(!this.trainerRepository.existsById(consultingTime.getTrainer().getId())){
                InvalidEntityException e = new InvalidEntityException("Trainer existiert nicht!");
                LOGGER.error("attempt to save consultingTime with trainer that doesnt exist");
                throw new ValidationException(e.getMessage(), e);
            }
        }
        catch(InvalidEntityException e) {
            LOGGER.error("validity violation: {}", e.getMessage(), e);
            throw new ValidationException(e.getMessage(), e);
        }

        try {
            consultingTime.setTrainer(trainerRepository.getOne(consultingTime.getTrainer().getId()));
            return consultingTimeRepository.save(consultingTime);
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteByGroupId(Long id){
        consultingTimeRepository.deleteByGroupId(id);
    }
    @Override
    public LinkedList<ConsultingTime> saveConsultingTimes(
        ConsultingTimesDto consultingTimesDto) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new consulting Times: {}", consultingTimesDto);
        LinkedList<ConsultingTime> consultingTimes = cronExpressionToConsultingTimesList(consultingTimesDto);
        LinkedList<ConsultingTime> resultList = new LinkedList<>();

        for(int i = 0; i < consultingTimes.size(); i++){

            try {
                validate.validateConsultingTime(consultingTimes.get(i));
            }
            catch(InvalidEntityException e) {
                LOGGER.error("validity violation: {}", e.getMessage(), e);
                throw new ValidationException(e.getMessage(), e);
            }
            try {
                LOGGER.info("Prepare save of new consultingTime {}", consultingTimes.get(i));
                resultList.add(consultingTimeRepository.save(consultingTimes.get(i)));

            } catch(Exception e) {
                throw new ServiceException(e);
            }
        }

        return resultList;
    }

    //2019-05-30T13:30:00
    // toggle=5 repeatAt(O1-O4 for the 4 options) repeatX endAt endX
    @Override
    public LinkedList<ConsultingTime> cronExpressionToConsultingTimesList(ConsultingTimesDto consultingTimesDto) throws ServiceException, ValidationException{
        LOGGER.info("Cron expression will be resolved now!");
        if(!this.trainerRepository.existsById(consultingTimesDto.getTrainerid())){
            InvalidEntityException e = new InvalidEntityException("Trainer existiert nicht!");
            LOGGER.error("attempt to save consultingTimes with trainer that doesnt exist");
            throw new ValidationException(e.getMessage(), e);
        }
        Trainer trainer = (Trainer)trainerRepository.getOne(consultingTimesDto.getTrainerid());
        LOGGER.info("Trainer is: " + trainer);
        try {
            //Create LinkedLists and split cronExpression
            LinkedList<ConsultingTime> resultList = new LinkedList<>();
            LinkedList<LocalDateTime> startLocalDateTimes = new LinkedList<>();
            LinkedList<LocalDateTime> endLocalDateTimes = new LinkedList<>();
            String[] cronSplit = consultingTimesDto.getCronExpression().split(" ");




            //Turn CronExpression Into a StartDateTime and EndDatetime and add to the correct list
            String startMonth = (cronSplit[3].split("/"))[0];
            if(startMonth.length()<2){
                startMonth = "0" + startMonth;
            }
            String endMonth = (cronSplit[3].split("/"))[1];
            if(endMonth.length()<2){
                endMonth = "0" + endMonth;
            }
            String startDay = (cronSplit[2].split("/"))[0];
            if(startDay.length()<2){
                startDay = "0" + startDay;
            }
            String endDay = (cronSplit[2].split("/"))[1];
            if(endDay.length()<2){
                endDay = "0" + endDay;
            }
            String startMinute = (cronSplit[0].split("/"))[0];
            if(startMinute.length()<2){
                startMinute = "0" + startMinute;
            }
            String endMinute = (cronSplit[0].split("/"))[1];
            if(endMinute.length()<2){
                endMinute = "0" + endMinute;
            }
            String startHour = (cronSplit[1].split("/"))[0];
            if(startHour.length()<2){
                startHour = "0" + startHour;
            }
            String endHour = (cronSplit[1].split("/"))[1];
            if(endHour.length()<2){
                endHour = "0" + endHour;
            }


            String startTime = "T" + startHour + ":" + startMinute + ":00";
            String endTime = "T" + endHour + ":" +  endMinute + ":00";

            String startDate = (cronSplit[4].split("/"))[0] + "-" + startMonth + "-" + startDay;
            String endDate = (cronSplit[4].split("/"))[1] + "-" + endMonth + "-" + endDay;

            startLocalDateTimes.add(LocalDateTime.parse(startDate+startTime));
            endLocalDateTimes.add(LocalDateTime.parse(endDate+endTime));


            LOGGER.info("startLocalDateTimes:" + startLocalDateTimes);
            LOGGER.info("endLocal:" + endLocalDateTimes);
            //Use Rest of the cron expression to build up the list of holidayStartDateTimes and holidayEndDateTimes

            boolean toggle = Boolean.parseBoolean(cronSplit[5]);
            int repeatX = Integer.parseInt(cronSplit[7]);
            int endX = Integer.parseInt(cronSplit[9]);

            LocalDateTime startLast = startLocalDateTimes.getLast();
            LocalDateTime endLast = endLocalDateTimes.getLast();



            LOGGER.debug("Used Option: " + cronSplit[6]);
            LOGGER.debug("Used Option: " + cronSplit[6]);
            if(cronSplit[8].equals("Nie")){
                endX = 1000;
            }
            if(toggle) {
                for(int i = 0; i < endX; i++) {

                    LOGGER.debug("Comparing endX: " + endX + " with i: " + i);
                    if(cronSplit[6].equals("O1")) {
                        i = 1000;
                    } else if(cronSplit[6].equals("O2")) {
                        if(startLast.plusDays(repeatX).isAfter(startLocalDateTimes.getFirst().plusYears(2))) {
                            i = 1000;
                        } else {
                            startLast = startLast.plusDays(repeatX);
                            startLocalDateTimes.add(startLast);
                            endLast = endLast.plusDays(repeatX);
                            endLocalDateTimes.add(endLast);
                        }
                    } else if(cronSplit[6].equals("O3")) {
                        if(startLast.plusDays(repeatX).isAfter(startLocalDateTimes.getFirst().plusYears(2))) {
                            i = 1000;
                        } else {
                            startLast = startLast.plusWeeks(repeatX);
                            startLocalDateTimes.add(startLast);
                            endLast = endLast.plusWeeks(repeatX);
                            endLocalDateTimes.add(endLast);
                        }
                    } else {

                        if(startLast.plusDays(repeatX).isAfter(startLocalDateTimes.getFirst().plusYears(2))) {
                            i = 1000;
                        } else {
                            startLast = startLast.plusMonths(repeatX);
                            startLocalDateTimes.add(startLast);
                            endLast = endLast.plusMonths(repeatX);
                            endLocalDateTimes.add(endLast);
                        }
                    }
                }
            }

            LOGGER.info("resultStarts:" + startLocalDateTimes);
            LOGGER.info("resultEnds:" + endLocalDateTimes);

            //Create ConsultingTimeList out of Start and End lists + trainerId
            LOGGER.info("Trainerid is: " + consultingTimesDto.getTrainerid());
            Instant now = Instant.now();
            for(int i = 0; i < startLocalDateTimes.size(); i++){
                ConsultingTime c = new ConsultingTime(trainer, consultingTimesDto.getTitle(), consultingTimesDto.getDescription(),
                                        startLocalDateTimes.get(i), endLocalDateTimes.get(i), now.toEpochMilli() );
                resultList.add(c);
            }

            return resultList;
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }
}
