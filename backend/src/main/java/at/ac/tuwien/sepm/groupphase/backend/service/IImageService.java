package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {

    /**
     * Saves the given file (allowed types: png,jpg) to the filesystem.
     *
     * @param file given file (an image) to be saved
     * @return the unique filename of the saved file is returned (no absolute path, i.e. no folders but merely the filename itself)
     * @throws ServiceException will be thrown if the file could not be persistently stored
     * @throws ValidationException  will be thrown if the uploaded file violates the given constraint (file empty or > 10 MB,
     *                              unsupported file extension, corrupted image data)
     */
    String save(MultipartFile file) throws ServiceException, ValidationException;

    /**
     * A file that is referred to by the specific name is returned if such a file is stored on the server's filesystem.
     *
     * @param filename the unique name of the requested file (no absolute path, but merely the name itself without folder structure)
     * @return the requested file wrapped in a container
     * @throws ServiceException will be thrown is the lookup for the file could not be performed (malformed search string, internal errors)
     * @throws NotFoundException will be thrown if no file exists that has the same filename as requested
     */
    MultipartFile get(String filename) throws ServiceException, NotFoundException;
}
