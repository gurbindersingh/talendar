package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import at.ac.tuwien.sepm.groupphase.backend.persistence.HolidayRepository;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidaysDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IHolidayService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import jdk.dynalink.linker.LinkerServices;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;


@Service
public class HolidayService implements IHolidayService {

    private final static Logger LOGGER = LoggerFactory.getLogger(HolidayService.class);

    private final HolidayRepository holidayRepository;
    private final TrainerRepository trainerRepository;
    private final Validator validate;

    @Autowired
    public HolidayService (HolidayRepository holidayRepository, TrainerRepository trainerRepository, Validator validate) {
        this.holidayRepository = holidayRepository;
        this.trainerRepository = trainerRepository;
        this.validate = validate;
    }


    @Override
    public Holiday save (Holiday holiday) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new holiday: {}", holiday);

        try {
            validate.validateHoliday(holiday);
            if(!this.trainerRepository.existsById(holiday.getTrainer().getId())){
                InvalidEntityException e = new InvalidEntityException("Trainer existiert nicht!");
                LOGGER.error("attempt to save holiday with trainer that doesnt exist");
                throw new ValidationException(e.getMessage(), e);
            }
        }
        catch(InvalidEntityException e) {
            LOGGER.error("validity violation: {}", e.getMessage(), e);
            throw new ValidationException(e.getMessage(), e);
        }

        try {
            holiday.setTrainer(trainerRepository.getOne(holiday.getTrainer().getId()));
            return holidayRepository.save(holiday);
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }

    public LinkedList<Holiday> saveHolidays (HolidaysDto holidaysDto) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new holidays: {}", holidaysDto);
        LinkedList<Holiday> holidays = cronExpressionToHolidaysList(holidaysDto);
        LinkedList<Holiday> resultList = new LinkedList<>();

        for(int i = 0; i < holidays.size(); i++){

            try {
                validate.validateHoliday(holidays.get(i));
            }
            catch(InvalidEntityException e) {
                LOGGER.error("validity violation: {}", e.getMessage(), e);
                throw new ValidationException(e.getMessage(), e);
            }
            try {
                LOGGER.info("Prepare save of new holidays {}", holidays.get(i));
                resultList.add(holidayRepository.save(holidays.get(i)));

            } catch(Exception e) {
                throw new ServiceException(e);
            }
        }

        return resultList;
    }

    //2019-05-30T13:30:00
    // toggle=5 repeatAt(O1-O4 for the 4 options) repeatX endAt endX
    public LinkedList<Holiday> cronExpressionToHolidaysList (HolidaysDto holidaysDto) throws ServiceException, ValidationException{
        LOGGER.info("Cron expression will be resolved now!");
        if(!this.trainerRepository.existsById(holidaysDto.getTrainerid())){
            InvalidEntityException e = new InvalidEntityException("Trainer existiert nicht!");
            LOGGER.error("attempt to save holidays with trainer that doesnt exist");
            throw new ValidationException(e.getMessage(), e);
        }
        Trainer trainer = (Trainer)trainerRepository.getOne(holidaysDto.getTrainerid());
        LOGGER.info("Trainer is: " + trainer);
        try {
            //Create LinkedLists and split cronExpression
            LinkedList<Holiday> resultList = new LinkedList<>();
            LinkedList<LocalDateTime> startLocalDateTimes = new LinkedList<>();
            LinkedList<LocalDateTime> endLocalDateTimes = new LinkedList<>();
            String[] cronSplit = holidaysDto.getCronExpression().split(" ");




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
            if(cronSplit[8]=="Nie"){
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

            //Create HolidayList out of Start and End lists + trainerId
            LOGGER.info("Trainerid is: " + holidaysDto.getTrainerid());
            for(int i = 0; i < startLocalDateTimes.size(); i++){
                Holiday h = new Holiday(trainer, holidaysDto.getTitle(), holidaysDto.getDescription(),
                                        startLocalDateTimes.get(i), endLocalDateTimes.get(i));
                resultList.add(h);
            }

            return resultList;
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }
}
