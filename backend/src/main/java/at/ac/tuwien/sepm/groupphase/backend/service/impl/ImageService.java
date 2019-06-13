package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.StorageProperties;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.IImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidEntityException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@Service
public class ImageService implements IImageService {

    private final StorageProperties storageProperties;
    /**
     * consider removing this manual validation by more enhanced technique. Constraints for files
     * can be specified within yaml and thrown exception by spring can be filtered and customized...
     */
    private final Validator validator;

    public ImageService(StorageProperties storageProperties, Validator validator) {
        this.storageProperties = storageProperties;
        this.validator = validator;
    }

    @Override
    public String save(MultipartFile file) throws ServiceException, ValidationException {
        // intermediate directory where the file will be saved (used for creation of new file)
        File directory = new File(System.getProperty("user.dir") + storageProperties.getProfileImgFolder());
        LocalDate date = LocalDate.now();
        File newFile;

        try {
            validator.validateImageFile(file);
        }
        catch(InvalidEntityException e) {
            throw new ValidationException(e.getMessage(), e);
        }
        // now image is guaranteed to be in an accessible format
        try {
            String filePrefix = date.toString() + "_";
            String origFileExt = extractFileEnding(file);
            // file created by createTempFile is not different to a regular file
            // advantage: method guarantees to create a new file that did not already exist in the specified folder
            newFile = File.createTempFile(filePrefix,origFileExt, directory);
        }
        catch(IOException e) {
            throw new ServiceException("a error occurred while creating a new file", e);
        }

        try {
            file.transferTo(newFile);
        }
        catch(IOException e) {
            throw new ServiceException("a error occurred while saving the content to the new file", e);
        }

        return newFile.getName();
    }


    @Override
    public MultipartFile get(String filename) throws ServiceException, NotFoundException {
        return null;
    }



    /**
     * Returns the extension of a file. (i.e png for 'image.png')
     * It is expected that this method may only be used after the input file has been validated
     * and approved
     *
     * @param file original file to be saved
     * @return extension of file
     */
    private String extractFileEnding(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int lastDot = fileName.lastIndexOf('.');
        return fileName.substring(lastDot);
    }
}
