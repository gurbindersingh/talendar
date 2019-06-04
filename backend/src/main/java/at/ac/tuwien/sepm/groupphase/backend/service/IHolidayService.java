package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidaysDto;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public interface IHolidayService {    /**
     * This method will save the given instance of holiday.
     *
     * @param holiday the given holiday to be saved
     * @return the persistently saved instance is returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    Holiday save (Holiday holiday) throws ServiceException, ValidationException;

    /**
     * This method will save the given instance of holiday.
     *
     * @param holidaysDto containing the trainer creating the holidays and contains an ExpressionString which can be parsed into a valid list of start/end localdatetimes
     * @return all persistently saved holidays will be returned
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    LinkedList<Holiday> saveHolidays (HolidaysDto holidaysDto) throws ServiceException, ValidationException;

    /**
     * This method will save the given instance of holiday.
     *
     * @param holidaysDto containing the trainer creating the holidays and contains an ExpressionString which can be parsed into a valid list of start/end localdatetimes
     * @return a LinkedList of Holiday Objects, each of which needs to be persistently saved
     * @throws ServiceException will be thrown if any error occurs during data processing that leads to an unsuccessful operation
     * @throws ValidationException will be thrown if the given instance has invalid properties. The cause will be reported.
     */
    LinkedList<Holiday> cronExpressionToHolidaysList (HolidaysDto holidaysDto) throws ServiceException, ValidationException;

}
