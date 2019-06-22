package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.InformationOutput;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.persistence.CustomerRepository;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.service.IRightToInformationService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.UserNotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.CustomerMapper;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
public class RightToInformationService implements IRightToInformationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RightToInformationService.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MMMM_dd-hh_mm");
    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;


    public RightToInformationService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public InformationOutput createInformationOutput(String mail) throws DocumentException,
                                                                         NotFoundException {
        List<Customer> customers = customerRepository.findByEmail(mail);
        if(customers.isEmpty()){
            throw new NotFoundException("Keine Kunden mit dieser Email-Adresse gefunden");
        }
        InformationOutput info = new InformationOutput();
        String filename = LocalDateTime.now().format(formatter) + "_" + mail;
        info.setFilename(filename);
        LOGGER.info("Docname: " + filename);
        Document document = new Document();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, b);
        document.open();
        Font listFont = FontFactory.getFont(FontFactory.COURIER, 14, Color.BLACK);
        Paragraph chunk;
        for(int i = 0; i < customers.size(); i++){
            CustomerDto customerDto = customerMapper.entityToCustomerDto(customers.get(i));
            chunk = new Paragraph("\nVorname: " + customerDto.getFirstName() + "\nNachname: " + customerDto.getLastName() +
                                  "\nTelefonnummer: " + customerDto.getPhone() + "\nEmail-Adresse: " + customerDto.getEmail() +
                                  "\nHat teilgenommen/Wird teilnehmen an:\n\n\n");

            PdfPTable table = new PdfPTable(2);
            table.addCell("Event-Name");
            table.addCell("Datum");
            for(int j = 0; j < customerDto.getEventDtos().size(); j++){
                table.addCell(customerDto.getEventDtos().get(j).getName());
                table.addCell(customerDto.getEventDtos().get(j).getRoomUses().get(0).getBegin().format(formatter));
            }
            document.add(chunk);
            document.add(table);
        }
        document.close();
        info.setContents(b.toByteArray());
        return info;
    }
}
