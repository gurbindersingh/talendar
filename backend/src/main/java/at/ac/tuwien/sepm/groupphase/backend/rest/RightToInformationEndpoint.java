package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.Entity.InformationOutput;
import at.ac.tuwien.sepm.groupphase.backend.annotations.IsAdmin;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.IRightToInformationService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.UserNotFoundException;
import com.lowagie.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.TransformerConfigurationException;
import java.io.FileNotFoundException;

@IsAdmin
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/talendar/info")
public class RightToInformationEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(RightToInformationEndpoint.class);

    private final IRightToInformationService rightToInformationService;


    @Autowired
    public RightToInformationEndpoint(IRightToInformationService rightToInformationService) {
        this.rightToInformationService = rightToInformationService;
    }


    @IsAdmin
    @RequestMapping(method = RequestMethod.GET, value = "/{mail}")
    public ResponseEntity<byte[]> returnRightToInformationPdf(@PathVariable("mail") String mail) throws
                                                                                                 UserNotFoundException,
                                                                                                 DocumentException,
                                                                                                 FileNotFoundException,
                                                                                                 TransformerConfigurationException,
                                                                                                 ServiceException,
                                                                                                 NotFoundException {
        LOGGER.info("Incoming Request to create a pdf containing UserData for User with mail: {}", mail);
        try{
            InformationOutput info = rightToInformationService.createInformationOutput(mail);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = info.getFilename();
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<>(info.getContents(), headers, HttpStatus.OK);
            LOGGER.info("Sending back the response in form of byte[] with size: " + info.getContents().length);
            return response;
        } catch(NotFoundException e){
            LOGGER.error("Couldnt find any customers with email: " + e.getMessage(), e);
            throw new NotFoundException("Es konnten keine Kunden mit dieser E-Mail-Adresse in der Datenbank gefunden werden", e);
        }
    }
}


