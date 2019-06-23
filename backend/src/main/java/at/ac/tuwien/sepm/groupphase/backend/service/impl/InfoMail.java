package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.*;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.enums.Room;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.EventMapper;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


@Component
public class InfoMail {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventMapper eventMapper;
    private final MailData mailData;

    public InfoMail (EventMapper eventMapper, MailData mailData) {
        this.eventMapper = eventMapper;
        this.mailData = mailData;
    }

    public Properties createProps () {
        Properties props = System.getProperties();
        String from = mailData.getSenderMail();
        String password = mailData.getSenderPassword();
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.pwd", password);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.auth", "true");
        return props;
    }

    public void sendAdminEventInfoMail(Event event, String subject, String type) throws EmailException {
        //TODO: Edit adminadresse to be the actual adminadresse.
        String to = mailData.getAdminMail();
        String from = mailData.getSenderMail();
        String password = mailData.getSenderPassword();
        String host = "smtp.gmail.com";
        Session session = Session.getDefaultInstance(createProps());
        try{
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            mimeMessage.setSubject("Änderung im System: " + subject);
            mimeMessage.setText(adminEventText(event, type));

            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        }catch(MessagingException e){
            throw new EmailException(" " + e.getMessage());
        }
    }
    public String adminEventText(Event event, String type) {
        if(type.equals("newEvent")){
            return
            "Hallo!\n\nEs wurde ein neues Event erstellt!\n Das Event: "
            + event.getName() + ", das am " + event.getRoomUses().get(0).getBegin().format(formatter) +
            " stattfinded, befinded sich ab jetzt im Talender!\n\n Mit freundlichen Grüßen,\nIhr Talenderteam!";
        } else if(type.equals("deleteEvent")){
            return
                "Hallo!\n\nEs wurde ein Event storniert!\n Das Event: "
                + event.getName() + ", das am " + event.getRoomUses().get(0).getBegin().format(formatter) +
                " stattfinded, wurde storniert!\n\n Mit freundlichen Grüßen,\nIhr Talenderteam!";

        }
        return "Hallo!\n\nNeue Änderungen fanden im System statt die nicht identifiziert werden konnten!\nBitte " +
               "geben Sie ihrem Talenderteam bescheid!\n\nMit freundlichen Grüßen,\nIhr Talenderteam";
    }


    public void sendAdminTrainerInfoMail(Trainer trainer, String subject, String type) throws EmailException {
        //TODO: Edit adminadresse to be the actual adminadresse.
        String to = mailData.getAdminMail();
        String from = mailData.getSenderMail();
        String password = mailData.getSenderPassword();
        String host = "smtp.gmail.com";
        Session session = Session.getDefaultInstance(createProps());
        try{
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            mimeMessage.setSubject("Änderung im System: " + subject);
            mimeMessage.setText(adminTrainerText(trainer, type));

            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        }catch(MessagingException e){
            throw new EmailException(" " + e.getMessage());
        }
    }
    public String adminTrainerText(Trainer trainer, String type) {
        if(type.equals("newTrainer")){
            return
                "Hallo!\n\nEs wurde ein neuer Trainer angelegt!\n Der Trainer: "
                + trainer.getFirstName() + " " + trainer.getLastName() + ", ist ab jetzt im System vorhanden!" +
                "\n\n Mit freundlichen Grüßen,\nIhr Talenderteam!";
        } else if(type.equals("deleteTrainer")){
            return
                "Hallo!\n\nEs wurde ein Trainer gelöscht!\n Der Trainer: "
                + trainer.getFirstName() + " " + trainer.getLastName() + ", ist ab jetzt nicht mehr im System vorhanden!" +
                "\n\n Mit freundlichen Grüßen,\nIhr Talenderteam!";

        }
        return "Hallo!\n\nNeue Änderungen fanden im System statt die nicht identifiziert werden konnten!\nBitte " +
               "geben Sie ihrem Talenderteam bescheid!\n\nMit freundlichen Grüßen,\nIhr Talenderteam";
    }

    public void informCustomers(Event event) throws EmailException {
        EventDto eventDto = eventMapper.entityToEventDto(event);
        for(int i = 0; i < event.getCustomers().size(); i++){
            CustomerDto customerDto = eventDto.getCustomerDtos().get(i);
            String to = customerDto.getEmail();
            String from = mailData.getSenderMail();
            String password = mailData.getSenderPassword();
            String host = "smtp.gmail.com";
            Session session = Session.getDefaultInstance(createProps());
            try{
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

                mimeMessage.setSubject("Stornierung des Events: " + event.getName());
                mimeMessage.setText("Hallo " + customerDto.getFirstName() + " " + customerDto.getLastName() +
                                    "\n\nDas Event: " + event.getName() + ", dass am " +
                                    event.getRoomUses().get(i).getBegin().format(formatter) + " stattgefunden hätte," +
                                    " wurde leider storniert.\nDas bedeutet es findet nicht statt und Sie wurden " +
                                    "dementsprechend abgemeldet.\nFür meine Informationen fragen Sie bitte bei der " +
                                    "Systemadministratorin nach!\n\nMit freundlichen Grüßen,\nIhr Talenderteam!");
                Transport transport = session.getTransport("smtp");
                transport.connect(host, 587, from, password);
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                transport.close();
            }catch(MessagingException e){
                throw new EmailException(" " + e.getMessage());
            }
        }
    }

    public void marketingMail(AlgoCustomer algoCustomer, Event event) throws EmailException{
        String to = algoCustomer.getEmail();
        String from = "testingsepmstuffqse25@gmail.com";
        String password = "This!is!a!password!";
        String host = "smtp.gmail.com";
        Session session = Session.getDefaultInstance(createProps());
        String url = null;
        String urlCancel = null;
        try{
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            url = "http://localhost:4200/course/sign?id=" + event.getId();
            urlCancel = "http://localhost:4200/cancelsubscription?email=" + algoCustomer.getEmail();
            URL urll = null;
            URL url2 = null;
            try {
                urll = new URL(url);
                url2 = new URL(urlCancel);
            }
            catch(MalformedURLException e) {
                throw new MessagingException("Malformed Url exception: " + e.getMessage(), e);
            }
            mimeMessage.setSubject("Angebote für Sie vom Talentengarten!");
            mimeMessage.setText("Hallo " + algoCustomer.getAssociated().get(0).getFirstName() + " " + algoCustomer.getAssociated().get(0).getLastName() +
                                "\n\nDas Event: " + event.getName() + ", dass am " +
                                event.getRoomUses().get(0).getBegin().format(formatter) + " stattfindet könnte Sie interessieren!\n"
                                + "Falls Sie sich anmelden wollen, klicken sie einfach auf diesen Link: " + urll + "\n\n\n"
                                + "Falls sie kein Werbung mehr kriegen wollen, können Sie sich mittels diesen links abmelden: " + url2);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        }catch(MessagingException e){
            throw new EmailException(e.getMessage());
        }

    }

    public void birthdayMail(AlgoCustomer algoCustomer, LocalDateTime bday){
        String to = algoCustomer.getEmail();
        String from = "testingsepmstuffqse25@gmail.com";
        String password = "This!is!a!password!";
        String host = "smtp.gmail.com";
        Session session = Session.getDefaultInstance(createProps());
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            String urlCancel = "http://localhost:4200/cancelsubscription?email=" + algoCustomer.getEmail();
            String urlBirthday = "http://localhost:4200/birthday/book";
            URL url2 = null;
            URL urlB = null;
            try {
                url2 = new URL(urlCancel);
                urlB = new URL(urlBirthday);
            }
            catch(MalformedURLException e) {
                throw new MessagingException("Malformed Url exception: " + e.getMessage(), e);
            }
            mimeMessage.setSubject("Sie haben bald ein Geburtstag in der Familie...");
            mimeMessage.setText("Hallo " + algoCustomer.getAssociated().get(0).getFirstName() + " " + algoCustomer.getAssociated().get(0).getLastName() +
                                "\n\nFeiern sie bei uns den Geburtstag am " + bday.format(formatter)
                                + " Hier können Sie sich Anmelden: "  + urlBirthday + "\n\n\n"
                                + "Falls sie kein Werbung mehr kriegen wollen, können Sie sich mittels diesen links abmelden: " + url2);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        }catch(MessagingException e){

        }
    }

    public void sendCancelationMail(String email, Event event, Customer customer) throws
                                                                                  EmailException {
        String to = email;
        String from = mailData.getSenderMail();
        String password = mailData.getSenderPassword();
        String host = "smtp.gmail.com";
        Properties props = System.getProperties();
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.pwd", password);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            switch(event.getEventType()) {
                case Rent:
                    mimeMessage.setSubject("Sie haben erfolgreich einen Raum bei uns gemietet");
                    break;
                case Course:
                    mimeMessage.setSubject("Sie haben sich erfolgreich angemeldet");
                    break;
                case Consultation:
                    mimeMessage.setSubject("Sie haben erfolgreich einen Beratungstermin erstellt");
                    break;
                default:
                    mimeMessage.setSubject("Sie haben erfolgreich einen Geburtstag erstellt");
                    break;
            }
            mimeMessage.setText(createCancelationMessage(event, customer));
            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        }
        catch(MessagingException e) {
            throw new EmailException(" " + e.getMessage());
        }
    }


    private String createCancelationMessage(Event event, Customer customer) throws
                                                                            MessagingException {

        String url;
        if(event.getEventType() == EventType.Course) {
            url = "http://localhost:4200/event/cancel?id=" +
                  event.getId() +
                  "&emailId=" +
                  customer.getEmailId();
        } else {
            url = "http://localhost:4200/event/cancel?id=" + event.getId();
        }
        URL urll = null;
        try {
            urll = new URL(url);
        }
        catch(MalformedURLException e) {
            throw new MessagingException("Malformed Url exception: " + e.getMessage(), e);
        }

        String msg = "";

        msg += "Hallo " + customer.getFirstName() + " " + customer.getLastName() + "!\n\n";
        switch(event.getEventType()) {
            case Rent:
                RoomUse roomForRent = event.getRoomUses().get(0);
                msg += "Hiermit bestätigen wir das Sie erfolgreich einen Raum gemietet haben.\n\n";
                msg += "Raum " + translateEnumToGerman(roomForRent.getRoom()) + "\n";
                msg += "Von " +
                       roomForRent.getBegin()
                                  .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                       " bis " +
                       roomForRent.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")) +
                       "\n\n";
                msg +=
                    "\nFalls Sie dieses Event stornieren wollen, klicken Sie bitte einfach auf diesen link: \n";
                break;
            case Course:
                msg += "Hiermit bestätigen wir Ihre Anmeldung zum \"" + event.getName() + "\"";
                msg += "\nEnde der Abmeldefrist: ";
                msg += event.getEndOfApplication().format(formatter) + "\n";
                msg += "\nFalls Sie sich abmelden wollen, klicken Sie auf diesen Link: \n";
                break;
            case Consultation:
                RoomUse roomForConsultation = event.getRoomUses().get(0);
                msg +=
                    "Hiermit bestätigen wir das Sie erfolgreich einen Beratungstermin erstellt haben.\n\n";
                msg += "Raum " + translateEnumToGerman(roomForConsultation.getRoom()) + "\n";
                msg += "Von " +
                       roomForConsultation.getBegin()
                                          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                       " bis " +
                       roomForConsultation.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")) +
                       "\n";
                msg += "Trainer " +
                       event.getTrainer().getFirstName() +
                       " " +
                       event.getTrainer().getLastName() +
                       "\n";
                msg +=
                    "\nFalls Sie dieses Event stornieren wollen, klicken Sie bitte einfach auf diesen link: \n";
                break;
            default:
                msg +=
                    "Hiermit bestätigen wir das Sie erfolgreich einen Geburtstag bei uns erstellt haben.\n\n";
                RoomUse roomForBirthDay = event.getRoomUses().get(0);
                msg += "Art " + translateBirthDayTypeToGerman(event.getBirthdayType()) + "\n";
                msg += "Raum " + translateEnumToGerman(roomForBirthDay.getRoom()) + "\n";
                msg += "Von " +
                       roomForBirthDay.getBegin()
                                      .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                       " bis " +
                       roomForBirthDay.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")) +
                       "\n";
                msg += "Trainer " +
                       event.getTrainer().getFirstName() +
                       " " +
                       event.getTrainer().getLastName() +
                       "\n";
                msg +=
                    "\nFalls Sie dieses Event stornieren wollen, klicken Sie bitte einfach auf diesen link: \n";
                break;
        }

        msg += urll;
        msg += "\n\nMit freundlichen Grüßen,\nIhr Talenderteam";

        return msg;
    }


    private static String translateEnumToGerman(Room room) {
        switch(room) {
            case GroundFloor:
                return "Erdgeschoss";
            case Green:
                return "Grün";
        }
        return "Orange";
    }


    private static String translateBirthDayTypeToGerman(String birthdayType) {
        switch(birthdayType) {
            case "Rocket":
                return "Raketen Geburtstag";
            case "Photo":
                return "Photo Geburtstag";
            case "DryIce":
                return "Trockeneis Geburtstag";
            case "Painting":
                return "Malen Geburtstag";
        }
        return "Superhelden Geburtstag";
    }


    private String translateEnumWithArtikel(EventType eventType) {
        switch(eventType) {
            case Birthday:
                return "ein Geburtstag";
            case Consultation:
                return "einen Beratungstermin";
            case Rent:
                return "eine Mietung";
            case Course:
                return "einen Kurs";
        }
        return "";
    }
}
