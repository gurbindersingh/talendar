package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
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
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Component
public class InfoMail {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventMapper eventMapper;

    public InfoMail (EventMapper eventMapper) {
        this.eventMapper = eventMapper;
    }

    public Properties createProps () {
        Properties props = System.getProperties();
        String from = "testingsepmstuffqse25@gmail.com";
        String password = "This!is!a!password!";
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
        String to = "admin@admin.admin";
        String from = "testingsepmstuffqse25@gmail.com";
        String password = "This!is!a!password!";
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
        String to = "admin@admin.admin";
        String from = "testingsepmstuffqse25@gmail.com";
        String password = "This!is!a!password!";
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
            String from = "testingsepmstuffqse25@gmail.com";
            String password = "This!is!a!password!";
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
}
