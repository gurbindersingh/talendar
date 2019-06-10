package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.Entity.InformationOutput;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.service.IRightToInformationService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.UserNotFoundException;
import com.itextpdf.text.DocumentException;
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


@RestController
@RequestMapping("/api/v1/talendar/info")
public class RightToInformationEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(RightToInformationEndpoint.class);

    private final IRightToInformationService rightToInformationService;


    @Autowired
    public RightToInformationEndpoint(IRightToInformationService rightToInformationService) {
        this.rightToInformationService = rightToInformationService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping(method = RequestMethod.GET, value = "/{mail}")
    public ResponseEntity<byte[]> returnRightToInformationPdf(@PathVariable("mail") String mail) throws
                                                                                                 BackendException,
                                                                                                 UserNotFoundException,
                                                                                                 DocumentException,
                                                                                                 FileNotFoundException,
                                                                                                 TransformerConfigurationException {
        LOGGER.info("Incoming Request to create a pdf containing UserData for User with mail: {}", mail);
        try{
            InformationOutput info = rightToInformationService.createInformationOutput(mail);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            String filename = info.getFilename();
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<>(info.getContents(), headers, HttpStatus.OK);
            return response;
        } catch(ServiceException e){
            LOGGER.error("Error beim InformationOutput erstellen: " + e.getMessage(), e);
            throw new BackendException("Etwas ist leider am Server schiefgelaufen", e);
        }
    }
}


