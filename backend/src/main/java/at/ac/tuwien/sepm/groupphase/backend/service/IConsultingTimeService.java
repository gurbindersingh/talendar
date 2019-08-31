package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.ConsultingTime;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.ConsultingTimesDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidaysDto;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public interface IConsultingTimeService {    /**
     * This method will save the given instance of holiday.
     *
     * @param consultingTime the given cT to be saved
     * @return the persistently saved instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    ConsultingTime save(ConsultingTime consultingTime) throws ServiceException, ValidationException;

    /**
     * This method will load the given instance of holiday.
     *
     * @param id is id of the trainer who's cT are requested
     * @return the list of found cTs
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws NotFoundException will be thrown if the given trainer id could not be found or no holidays could be found.
     */
    LinkedList<ConsultingTime> getAllConsultingTimesByTrainerId(Long id) throws ServiceException,
                                                                  NotFoundException;

    /**
     * This method will get all cTs
     * @return the list of found cTs
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws NotFoundException will be thrown if no holidays could be found.
     */
    LinkedList<ConsultingTime> getAllConsultingTimes() throws ServiceException,
                                                NotFoundException;

    /**
     * This method will save the given instance of cT.
     *
     * @param consultingTimesDto containing the trainer creating the holidays and contains an ExpressionString which can be parsed into a valid list of start/end localdatetimes
     * @return all persistently saved CTs will be returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    LinkedList<ConsultingTime> saveConsultingTimes(ConsultingTimesDto consultingTimesDto) throws ServiceException, ValidationException;

    /**
     * This method will save the given instance of cT.
     *
     * @param consultingTimesDto containing the trainer creating the holidays and contains an ExpressionString which can be parsed into a valid list of start/end localdatetimes
     * @return a LinkedList of ConsultingTimes Objects, each of which needs to be persistently saved
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    LinkedList<ConsultingTime> cronExpressionToConsultingTimesList(ConsultingTimesDto consultingTimesDto) throws ServiceException, ValidationException;

}
