package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.Entity.InformationOutput;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.UserNotFoundException;
import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Component;

import javax.xml.transform.TransformerConfigurationException;
import java.io.FileNotFoundException;

@Component
public interface IRightToInformationService {
    /**
     * Create a new user (account) for an authenticated user of the system.
     * This user profile will be used for any spring security related retrieval of known users.
     * I.e. Any authentication (username/password mapping upon a login) will be matched against
     * user entities that are created by this service!
     *
     * @param mail contains the EMail of the customer who wishes for an extraction of his private info.
     * @return the persisted user account
     * @throws at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException will be thrown if the user account could not be created.
     */
    InformationOutput createInformationOutput(String mail) throws ServiceException,
                                                                  UserNotFoundException,
                                                                  FileNotFoundException,
                                                                  DocumentException,
                                                                  TransformerConfigurationException,
                                                                  NotFoundException;
}
