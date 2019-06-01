package at.ac.tuwien.sepm.groupphase.backend.schedule;

import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.rest.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.rest.HolidaysEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Component
public class participantsList {

    private static final Logger LOGGER = LoggerFactory.getLogger(HolidaysEndpoint.class);

    private final EventEndpoint eventEndpoint;
    @Autowired

    public participantsList (EventEndpoint eventEndpoint) {
        this.eventEndpoint = eventEndpoint;
    }

    //Cron needs to be changed for everyday at 23:00
    @Scheduled(cron = "0,30 * * * * *")
    @Transactional
    public void greeting() throws BackendException, FileNotFoundException, DocumentException {
        LOGGER.info("Initiating Spring Scheduled Task: participants lists");
        try {

            LOGGER.info("Fetching EventList");
            List<EventDto> eventList = eventEndpoint.getAllEvents();

            LOGGER.info("Filtering EventList for Events that need to have a participationList sent");
            List<EventDto> sendList = new LinkedList<>();
            for(int i = 0; i < eventList.size(); i++){
                if(eventList.get(i).getRoomUses().get(0).getBegin().isBefore(LocalDateTime.now().plusDays(2)) &&
                        eventList.get(i).getRoomUses().get(0).getBegin().isAfter(LocalDateTime.now().plusDays(1))){

                    sendList.add(eventList.get(i));
                }
            }

            Font headlineFont = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
            Font listFont = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph chunkHead;
            Paragraph chunkBottom;
            String participantsListText = "";

            LOGGER.info("For every Event create participants Document and send it to the corresponding trainer");
            for(int i = 0; i < sendList.size(); i++){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_mm_dd-hh_mm");
                String docname = LocalDateTime.now().format(formatter) + "_" + sendList.get(i).getName() + ".pdf";
                LOGGER.info("Docname: " + docname);
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(docname));
                document.open();
                chunkHead = new Paragraph("Teilnehmerliste für das Event: " + sendList.get(i).getName()
                        + "\n Beginnt am: " + sendList.get(i).getRoomUses().get(0).getBegin() + "\n\n", headlineFont);

                for(int j = 0; j <sendList.get(i).getCustomerDtos().size(); j++) {
                    participantsListText += "\n" + sendList.get(i).getCustomerDtos().get(j).getFirstName()
                        + " " + sendList.get(i).getCustomerDtos().get(j).getLastName()
                        + " " + sendList.get(i).getCustomerDtos().get(j).getEmail()
                        + " " + sendList.get(i).getCustomerDtos().get(j).getPhone();
                }

                chunkBottom = new Paragraph(participantsListText, listFont);
                document.add(chunkHead);
                document.add(chunkBottom);
                document.close();

            }






        } catch(BackendException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        }
    }
}
