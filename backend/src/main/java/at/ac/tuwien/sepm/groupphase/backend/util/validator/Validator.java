package at.ac.tuwien.sepm.groupphase.backend.util.validator;


import at.ac.tuwien.sepm.groupphase.backend.enums.Room;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.CancelationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Holiday;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;

import at.ac.tuwien.sepm.groupphase.backend.Entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Component
public class Validator {
    // matches any kind of regular phone number format
    // e.g. 0660 123 45 67, +(43) 01 234-56-78
    private String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$";
    private String emailRegex =
        "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    private Pattern phonePattern = Pattern.compile(phoneRegex);
    private Pattern emailPattern = Pattern.compile(emailRegex);


    public void validateEvent(Event event) throws InvalidEntityException {
        LocalDateTime now = LocalDateTime.now();

        // Validation for common stuff
        if(event.getId() != null){
            throw new InvalidEntityException("");
        }
        if(event.getCreated() == null || event.getCreated().isAfter(now)) {
            throw new InvalidEntityException("");
        }
        if(event.getUpdated() == null || event.getUpdated().isAfter(now)) {
            throw new InvalidEntityException("");
        }
        if(event.getCreated().isAfter(event.getUpdated())) {
            throw new InvalidEntityException("");
        }
        if(event.getName() == null || event.getName().isBlank()) {
            throw new InvalidEntityException("Name nicht gesetzt");
        }
        if(event.getRoomUses() == null) {
            throw new InvalidEntityException("");
        }
        // Validator for Birthdays
        if(event.getEventType() == EventType.Birthday) {
            if(event.getHeadcount() < 5) {
                throw new InvalidEntityException("Anzahl der Kinder die kommen ist kleiner 5");
            }
            if(event.getHeadcount() > 20) {
                throw new InvalidEntityException("Anzahl der Kinder die kommen ist größer als 20");
            }
            if(event.getAgeToBe() < 0) {
                throw new InvalidEntityException("Alter ist negativ");
            }
            if(event.getAgeToBe() > 20) {
                throw new InvalidEntityException("Alter ist größer als 20");
            }
            if(event.getCustomers() == null || event.getCustomers().size() != 1) {
                throw new InvalidEntityException("Kunde nicht gesetzt");
            }
            if(event.getBirthdayType() == null || event.getBirthdayType().isBlank()) {
                throw new InvalidEntityException("Geburtstagtyp nicht gesetzt");
            }
            try {
                for(Customer x : event.getCustomers()
                ) {
                    validateCustomer(x);
                }
            }
            catch(InvalidEntityException e) {
                throw e;
            }

            try {
                for(RoomUse r : event.getRoomUses()
                ) {
                    validateRoomUse(r);
                }
            }
            catch(InvalidEntityException e) {
                throw e;
            }
        }

        // Validator for Consultation
        if(event.getEventType() == EventType.Consultation) {
            if(event.getTrainer() == null) {
                throw new InvalidEntityException("Ein Trainer muss ausgewählt werden");
            }
            if(event.getRoomUses() == null || event.getRoomUses().size() != 1) {
                throw new InvalidEntityException("Ein Raum muss ausgewählt werden");
            }
            if(event.getCustomers() == null || event.getCustomers().size() != 1) {
                throw new InvalidEntityException("Ein Kunde muss gesetzt sein");
            }
            try {
                for(Customer x : event.getCustomers()) {
                    validateCustomer(x);
                }
                for(RoomUse x : event.getRoomUses()) {
                    validateRoomUse(x);
                }
            }
            catch(InvalidEntityException e) {
                throw e;
            }
        }

        // Validator for Course
        if(event.getEventType() == EventType.Course) {
            if(event.getEndOfApplication() == null) {
                throw new InvalidEntityException("Anmeldefrist ist nicht gesetzt");
            } else if(event.getEndOfApplication().isBefore(now)) {
                throw new InvalidEntityException("Anmeldefrist liegt nicht in der Zukunft");
            }

            if(event.getPrice() == null) {
                throw new InvalidEntityException("Preis ist nicht gesetzt");
            } else if(event.getPrice() < 0) {
                throw new InvalidEntityException("Preis ist negativ");
            }

            if(event.getMaxParticipants() == null) {
                throw new InvalidEntityException("Maximale Teilnehmeranzahl ist nicht gesetzt");
            } else if(event.getMaxParticipants() < 5) {
                throw new InvalidEntityException("Maximale Teilnehmeranzahl ist kleiner als 5");
            }

            if(event.getMaxParticipants() > 30) {
                throw new InvalidEntityException("Maximale Teilnehmeranzahl ist größer als 30");
            }

            if(event.getMinAge() == null) {
                throw new InvalidEntityException("Minimum Alter ist nicht gesetzt");
            }

            if(event.getMaxAge() == null) {
                throw new InvalidEntityException("Maximum Alter ist nicht gesetzt");
            }

            if(event.getMinAge() > event.getMaxAge()) {
                throw new InvalidEntityException("Minimum Alter ist größer als maximum Alter");
            }

            if(event.getMinAge() < 5) {
                throw new InvalidEntityException("Minimum Alter ist kleiner als 5");
            }

            if(event.getMaxAge() > 100) {
                throw new InvalidEntityException("Maximum Alter ist größer als 100");
            }

            if(event.getDescription() == null || event.getDescription().isBlank()) {
                throw new InvalidEntityException("Beschreibung ist nicht gesetzt");
            }

            if(event.getTrainer() == null || event.getTrainer().getId() == null) {
                throw new InvalidEntityException("Trainer Id ist nicht gesetzt");
            }
            if(event.getCustomers() != null && !event.getCustomers().isEmpty()) {
                throw new InvalidEntityException("Kundenliste ist nicht leer");
            }

            try {
                for(RoomUse r : event.getRoomUses()
                ) {
                    validateRoomUse(r);
                }
            }
            catch(InvalidEntityException e) {
                throw e;
            }
        }

        // Validator for Rent
        if(event.getEventType() == EventType.Rent) {

            if(event.getCustomers().size() != 1) {
                throw new InvalidEntityException("");
            }
            try {
                for(Customer c : event.getCustomers()) {
                    validateCustomer(c);
                }
                for(RoomUse r : event.getRoomUses()) {
                    validateRoomUse(r);
                }
            }
            catch(InvalidEntityException ie) {
                throw ie;
            }
        }
    }


    public void validateRoomUse(RoomUse entity) throws InvalidEntityException {
        LocalDateTime now = LocalDateTime.now();
        if(entity.getBegin().isAfter(entity.getEnd())) {
            throw new InvalidEntityException("Das Von-Datum findet später als Bis-Datum statt");
        }
        if(entity.getBegin().getHour() < 8 || entity.getBegin().getHour() > 22) {
            throw new InvalidEntityException(
                "Das Von-Datum findet außerhalb der Öffnungszeiten statt");
        }
        if(entity.getEnd().getHour() < 8 || entity.getEnd().getHour() > 22) {
            throw new InvalidEntityException(
                "Das End-Datum findet außerhalb der Öffnungszeiten statt");
        }
        if(entity.getBegin().isBefore(now)) {
            throw new InvalidEntityException("Das Von-Datum findet in der Vegangenheit statt");
        }
    }


    public void validateCustomer(Customer entity) throws InvalidEntityException {
        if(entity.getPhone() == null || !phonePattern.matcher(entity.getPhone()).find()) {
            throw new InvalidEntityException("Telefonnummer ist ungültig");
        }

        if(entity.getEmail() == null || !emailPattern.matcher(entity.getEmail()).find()) {
            throw new InvalidEntityException("E-Mail Adresse ist ungültig");
        }
        if(entity.getFirstName() == null || entity.getFirstName().isBlank()) {
            throw new InvalidEntityException("Vorname ist nicht gesetzt");
        }
        if(entity.getLastName() == null || entity.getLastName().isBlank()) {
            throw new InvalidEntityException("Nachname ist nicht gesetzt");
        }
    }


    public void validateTrainer(Trainer entity) throws InvalidEntityException {
        LocalDateTime now = LocalDateTime.now();

        if(entity.getFirstName() == null || entity.getFirstName().isBlank()) {
            throw new InvalidEntityException("Vorname ist nicht gesetzt");
        }

        if(entity.getLastName() == null || entity.getLastName().isBlank()) {
            throw new InvalidEntityException("Nachname ist nicht gesetzt");
        }

        if(entity.getBirthday() == null || entity.getBirthday().isAfter(LocalDate.now())) {
            throw new InvalidEntityException("Geburtstag liegt in der Zukunft");
        } else if(( ( LocalDate.now().getYear() - entity.getBirthday().getYear() ) <= 15 ) ||
                  ( ( LocalDate.now().getYear() - entity.getBirthday().getYear() ) > 120 )) {
            throw new InvalidEntityException("age must be a reasonable value");
        }

        if(entity.getPhone() == null || !phonePattern.matcher(entity.getPhone()).find()) {
            throw new InvalidEntityException("Telefonnummer ist ungültig");
        }

        if(entity.getEmail() == null || !emailPattern.matcher(entity.getEmail()).find()) {
            throw new InvalidEntityException("E-Mail Adresse ist ungültig");
        }

        if(entity.getCreated() == null || entity.getCreated().isAfter(now)) {
            throw new InvalidEntityException("");
        }

        if(entity.getUpdated() == null || entity.getUpdated().isAfter(now)) {
            throw new InvalidEntityException("");
        }

        if(entity.getCreated().isAfter(entity.getUpdated())) {
            throw new InvalidEntityException("");
        }
    }


    public void validateHoliday(Holiday holiday) throws InvalidEntityException {
        if(holiday.getTrainer() == null) {
            throw new InvalidEntityException("Trainer ist eventuell nicht eingeloggt");
        }
        if(holiday.getId() != null) {
            throw new InvalidEntityException("Frontend hat eine ID geschickt");
        }
        if(holiday.getTrainer().getId() == null) {
            throw new InvalidEntityException("Trainer ist eventuell nicht eingeloggt");
        }
        if(holiday.getHolidayStart().isAfter(holiday.getHolidayEnd())) {
            throw new InvalidEntityException("Das Von-Datum findet später als das Bis-Datum statt");
        }
        if(holiday.getHolidayStart().isBefore(LocalDateTime.now())) {
            throw new InvalidEntityException("Das Von-Datum findet in der Vergangenheit statt");
        }
    }


    public void validateCancelation(Event event) throws CancelationException {
        switch(event.getEventType()) {
            case Birthday:
                for(RoomUse r : event.getRoomUses()
                ) {
                    if(LocalDateTime.now().isAfter(r.getBegin().minusMonths(1))) {
                        throw new CancelationException(
                            "Geburtstage müssen 1 Monat bevor sie stattfinden storniert werden");
                    }
                }
                break;
            case Consultation:
                for(RoomUse r : event.getRoomUses()
                ) {
                    if(LocalDateTime.now().isAfter(r.getBegin().minusDays(1))) {
                        throw new CancelationException(
                            "Beratungstermine müssen 1 Tag bevor sie stattfinden storniert werden");
                    }
                }
                break;
            case Rent:
                for(RoomUse r : event.getRoomUses()
                ) {
                    if(LocalDateTime.now().isAfter(r.getBegin().minusDays(2))) {
                        throw new CancelationException(
                            "Mietungen müssen 2 Tage bevor sie stattfinden storniert werden");
                    }
                }
                break;
            default:
                throw new CancelationException("Irgendwas ist schiefgelaufen");
        }
    }
}
