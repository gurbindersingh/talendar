package at.ac.tuwien.sepm.groupphase.backend.schedule;

import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.rest.EventEndpoint;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.EmailException;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Header;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Component
public class ParticipantsList {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantsList.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MMMM_dd-hh_mm");
    private DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd MMMM yyyy - hh:mm")
                                                            .withLocale(
                                                                Locale.forLanguageTag("German"));

    private final EventEndpoint eventEndpoint;


    @Autowired

    public ParticipantsList(EventEndpoint eventEndpoint) {
        this.eventEndpoint = eventEndpoint;
    }


    //Cron needs to be changed for everyday at 23:00
    @Transactional
    public void createParticipantsList() throws BackendException, DocumentException, EmailException,
                                                IOException {
        LOGGER.info("Initiating Spring Scheduled Task: participants lists");
        try {

            LOGGER.info("Fetching EventList");
            List<EventDto> eventList = eventEndpoint.getAllEvents();

            LOGGER.info(
                "Filtering EventList for Events that need to have a participationList sent");
            List<EventDto> sendList = new LinkedList<>();
            for(int i = 0; i < eventList.size(); i++) {
                if(eventList.get(i)
                            .getRoomUses()
                            .get(0)
                            .getBegin()
                            .isBefore(LocalDateTime.now().plusDays(2)) &&
                   eventList.get(i)
                            .getRoomUses()
                            .get(0)
                            .getBegin()
                            .isAfter(LocalDateTime.now().plusDays(1))) {

                    sendList.add(eventList.get(i));
                }
            }

            Font headlineFont = FontFactory.getFont(FontFactory.COURIER, 20, Color.BLACK);
            Font listFont = FontFactory.getFont(FontFactory.COURIER, 14, Color.BLACK);
            Paragraph chunkHead;
            PdfPTable table = new PdfPTable(4);
            table.setSpacingAfter(11f);
            table.setSpacingBefore(11f);

            LOGGER.info(
                "For every Event create participants Document and send it to the corresponding trainer");
            for(int i = 0; i < sendList.size(); i++) {
                String docname = LocalDateTime.now().format(formatter) +
                                 "_" +
                                 sendList.get(i).getName() +
                                 ".pdf";
                LOGGER.info("Docname: " + docname);
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(docname));
                document.open();
                chunkHead =
                    new Paragraph("Teilnehmerliste für das Event: " +
                                  sendList.get(i).getName()
                                  +
                                  "\n Beginnt am: " +
                                  sendList.get(i)
                                          .getRoomUses()
                                          .get(0)
                                          .getBegin()
                                          .format(formatter2) +
                                  "\n\n", headlineFont);


                //table

                table.addCell("Vorname");
                table.addCell("Nachname");
                table.addCell("E-Mail Adresse");
                table.addCell("Telefon-Nummer");
                for(int j = 0; j < sendList.get(i).getCustomerDtos().size(); j++) {
                    table.addCell(sendList.get(i).getCustomerDtos().get(j).getFirstName());
                    table.addCell(sendList.get(i).getCustomerDtos().get(j).getLastName());
                    table.addCell(sendList.get(i).getCustomerDtos().get(j).getEmail());
                    table.addCell(sendList.get(i).getCustomerDtos().get(j).getPhone());
                }

                document.add(chunkHead);
                document.add(table);
                document.close();

                sendParticipationListEmail(sendList.get(i).getTrainer().getEmail(), docname,
                                           sendList.get(i)
                );
            }
            LOGGER.info("Participation Lists sent out successfully.");
        }
        catch(BackendException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException(e.getMessage(), e);
        }
    }


    public void sendParticipationListEmail(String emailTo, String filename, EventDto event) throws
                                                                                            EmailException,
                                                                                            IOException {
        LOGGER.info("Creating Email with participationList");
        String from = "testingsepmstuffqse25@gmail.com";
        String password = "This!is!a!password!";
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
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
            mimeMessage.setSubject("Teilnehmerliste: " + filename);

            //Creating multipart
            Multipart multipart = new MimeMultipart();
            MimeBodyPart message = new MimeBodyPart();
            message.setText(
                "Hallo!\n\nAnbei finden Sie die Teilnehmer-Liste für das bald stattfindende Event.\n" +
                "Wir wünschen Ihnen viel Erfolg und Spaß!\n\nLiebe Grüße,\nDas Talenderteam!");
            multipart.addBodyPart(message);
            //Attaching Pdf
            LOGGER.debug("Attatching Pdf");

            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(new File(filename), "application/pdf", null);
            multipart.addBodyPart(attachment);

            //Adding all the rechnungens to the multipart
            for(int i = 0; i < event.getCustomerDtos().size(); i++) {
                MimeBodyPart bill = new MimeBodyPart();
                bill.attachFile(new File(createBill(event.getCustomerDtos().get(i), event)),
                                "application/pdf", null
                );
                multipart.addBodyPart(bill);
            }
            //Adding multipart to the mail
            mimeMessage.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, password);
            LOGGER.debug("Attempting to send an Email...");
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            LOGGER.debug("Sending Email successful!");
            transport.close();
        }
        catch(MessagingException e) {
            throw new EmailException(" " + e.getMessage());
        }
    }


    public String createBill(CustomerDto customer, EventDto event) throws IOException,
                                                                          EmailException {
        //TODO: Wir müssen dafür sorgen, dass die generierten PDFs in Unterordnern erstellt werden, nicht so wie momentan einfach in /backend/

        String billPdfName = "";
        int billCount = 0;
        int year = LocalDate.now().getYear();
        String line;

        try {
            FileReader fileReader =
                new FileReader("bills.txt");

            BufferedReader bufferedReader =
                new BufferedReader(fileReader);


            while(( line = bufferedReader.readLine() ) != null) {
                String parts[] = line.split("-");
                billCount = Integer.parseInt(parts[0]);
                year = Math.max(Integer.parseInt(parts[1]), year);
            }

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            LOGGER.info("File bills.txt was not found. Creating a new one");
        }
        catch(IOException e) {
            LOGGER.error("Error reading file bills.txt: " + e.getMessage());
        }

        billCount += 1;

        try {
            FileWriter fileWriter = new FileWriter("bills.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(billCount + "-" + year);
            bufferedWriter.close();
        }
        catch(IOException e) {
            LOGGER.error("Error writing file bills.txt: " + e.getMessage());
        }

        Font headlineFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
        Font listFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM_dd-hh_mm");
        String docname = LocalDateTime.now().format(formatter) +
                         "_Rechnung_" +
                         billCount +
                         "_" +
                         year +
                         ".pdf";
        LOGGER.info("Docname: " + docname);
        Document document = new Document();
        document.setMargins(60f, 60f, 60f, 60f);
        PdfWriter writer;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(docname));
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        try {
            Image logo = Image.getInstance("src/main/resources/logo_bills.jpg");
            logo.scalePercent(10f);
            logo.setAbsolutePosition(350, 755);
            document.add(logo);
        }
        catch(IOException e) {
            LOGGER.error("Image logo_bills.jpg not found");
            e.printStackTrace();
        }

        cb.beginText();
        try {
            cb.setFontAndSize(BaseFont.createFont(), 8);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "Abs.: Begabungsexpertin | Steinfeldergasse 24 | 2340 Mödling",
                           60, 715, 0
        );
        cb.endText();

        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        cb.beginText();
        try {
            cb.setFontAndSize(BaseFont.createFont(), 11);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           customer.getFirstName() + " " + customer.getLastName(),
                           60, 670, 0
        );

        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "Begabungsexpertin",
                           370, 740, 0
        );
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "Steinfeldergasse 24",
                           370, 725, 0
        );
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "2340 Mödling, Österreich",
                           370, 710, 0
        );
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "Tel.: +43 - 699 / 181 699 83",
                           370, 695, 0
        );
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "office@begabungs-expertin.at",
                           370, 680, 0
        );

        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "Steuer Nr. 16 / 111 1182",
                           370, 655, 0
        );
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
                           "UST-IdNr. ATU 711 89 135",
                           370, 640, 0
        );

        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                           "Rechnung: Re-" + billCount + "/" + year,
                           530, 600, 0
        );
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
                           "Datum: " + formattedDate,
                           530, 585, 0
        );
        cb.endText();

        Paragraph headline = new Paragraph("Rechnung", headlineFont);
        headline.setSpacingBefore(190);
        headline.setAlignment(Element.ALIGN_CENTER);
        document.add(headline);

        Chunk billLeft =
            new Chunk("Rechnung: Re-" + billCount + "/" + year,
                      FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)
            );
        document.add(billLeft);

        Paragraph thankYou = new Paragraph(
            "Vielen Dank für Ihr Vertrauen. Ich hoffe Sie sind zufrieden.\nIch freue mich wieder von Ihnen zu hören!",
            FontFactory.getFont(FontFactory.HELVETICA, 11)
        );
        document.add(thankYou);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingAfter(5f);
        table.setSpacingBefore(11f);

        PdfPCell defaultCell = table.getDefaultCell();
        defaultCell.setPadding(3f);
        defaultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        defaultCell.setVerticalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell(new Phrase("Pos."));
        cell1.setUseBorderPadding(true);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell1.setVerticalAlignment(Element.ALIGN_CENTER);
        cell1.setBorderWidthBottom(2f);
        table.addCell(cell1);
        cell1.setPhrase(new Phrase("Bezeichnung"));
        table.addCell(cell1);
        cell1.setPhrase(new Phrase("Menge"));
        table.addCell(cell1);
        cell1.setPhrase(new Phrase("Einzelpreis"));
        table.addCell(cell1);
        cell1.setPhrase(new Phrase(""));
        table.addCell(cell1);
        cell1.setPhrase(new Phrase("Gesamtpreis"));
        table.addCell(cell1);

        table.addCell(new PdfPCell());
        table.addCell(new PdfPCell(new Phrase(event.getName())));
        table.addCell(new PdfPCell(new Phrase("1")));

        PdfPCell cell = new PdfPCell();

        double brutto = Double.valueOf(new DecimalFormat("#.##").format(event.getPrice()));
        cell.setPhrase(new Phrase(brutto + " €"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        cell.setPhrase(new Phrase("Netto:"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        double nettoBetrag = event.getPrice() / 1.2;
        nettoBetrag = Double.valueOf(new DecimalFormat("#.##").format(nettoBetrag));
        cell.setPhrase(new Phrase(nettoBetrag + " €"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        for(int i = 0; i < 4; i++) {
            table.addCell(new Phrase());
        }

        cell.setPhrase(new Phrase("USt.:"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        double ustBetrag = event.getPrice() - nettoBetrag;
        ustBetrag = Double.valueOf(new DecimalFormat("#.##").format(ustBetrag));
        cell = new PdfPCell(new Phrase(ustBetrag + " €"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        for(int i = 0; i < 4; i++) {
            table.addCell(new Phrase());
        }

        cell.setPhrase(new Phrase("Brutto:"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell.setPhrase(new Phrase(brutto + " €"));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        document.add(table);

        document.add(new Paragraph("Bitte überweisen Sie den Rechnungsbetrag auf das Konto:"));

        Paragraph konto = new Paragraph("Inhaber: Katja Higatzberger MA.\n" +
                                        "IBAN: AT55 1420 0200 1159 7239\n" +
                                        "BIC: EASYATW1\n" +
                                        "Zahlungsreferenz: Rechnungsnummer");
        konto.setSpacingBefore(20f);
        konto.setSpacingAfter(15f);
        document.add(konto);

        document.add(new Paragraph("mit freundlichen Grüßen\n\nKatja Higatzberger MA."));

        cb.beginText();
        try {
            cb.setFontAndSize(BaseFont.createFont(), 8);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                           "Begabungsexpertin",
                           305, 60, 0
        );

        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                           "Steinfeldergasse 24 2340 Mödling Österreich",
                           305, 50, 0
        );

        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                           "Tel.:+43 - 699 / 181 699 83 office@begabungs-expertin.at",
                           305, 40, 0
        );

        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                           "Steuer Nr. 16 / 111 1182 UST-IdNr. ATU 711 89 135",
                           305, 30, 0
        );

        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                           "IBAN: AT55 1420 0200 1159 7239 BIC: EASYATW1",
                           305, 20, 0
        );
        cb.endText();

        document.close();

        sendCustomerConfirmationMail(customer, billPdfName, event);
        return billPdfName;
    }


    public void sendCustomerConfirmationMail(CustomerDto customer, String filename, EventDto event
    ) throws IOException, EmailException {

        LOGGER.info("Creating Email with participationList");
        String from = "testingsepmstuffqse25@gmail.com";
        String password = "This!is!a!password!";
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
            mimeMessage.setRecipient(Message.RecipientType.TO,
                                     new InternetAddress(customer.getEmail())
            );
            mimeMessage.setSubject("Teilnehmerliste: " + filename);

            //Creating multipart
            Multipart multipart = new MimeMultipart();
            MimeBodyPart message = new MimeBodyPart();
            message.setText("Hallo " +
                            customer.getFirstName() +
                            " " +
                            customer.getLastName() +
                            "!\n\nWir wollten Sie daran erinnern, " +
                            "dass Sie zu dem Event: " +
                            event.getName() +
                            " angemeldet sind.\n" +
                            "Das Event findet am " +
                            event.getRoomUses().get(0).getBegin().format(formatter2) +
                            " statt." +
                            "Anbei finden Sie außerdem Ihre Rechnung!\n\nMit freundlichen Grüßen,\nDas Talenderteam!");
            multipart.addBodyPart(message);
            //Attaching Pdf
            LOGGER.debug("Attatching Pdf");
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.attachFile(new File(filename), "application/pdf", null);
            multipart.addBodyPart(attachment);

            //Adding multipart to the mail
            mimeMessage.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, 587, from, password);
            LOGGER.debug("Attempting to send an Email...");
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            LOGGER.debug("Sending Email successful!");
            transport.close();
        }
        catch(MessagingException e) {
            throw new EmailException(" " + e.getMessage());
        }
    }
}
