package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.StorageProperties;
import at.ac.tuwien.sepm.groupphase.backend.service.IImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.FileDeletionException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.Validator;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidFileException;
import at.ac.tuwien.sepm.groupphase.backend.util.validator.exceptions.InvalidFilePathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;

@Service
public class ImageService implements IImageService {

    private final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    private final StorageProperties storageProperties;
    private final Validator validator;

    public ImageService(StorageProperties storageProperties, Validator validator) {
        this.storageProperties = storageProperties;
        this.validator = validator;
    }

    @Override
    public String save(MultipartFile file, String folder) throws ServiceException, ValidationException {
        LOGGER.info("Prepare save of new image: {}", file);
        // intermediate directory where the file will be saved (used for creation of new file)
        File directory = new File(System.getProperty("user.dir") + folder);
        LocalDate date = LocalDate.now();
        File newFile;

        try {
            validator.validateImageFile(file);
        }
        catch(InvalidFileException e) {
            throw new ValidationException(e.getMessage(), e);
        }
        // now image is guaranteed to be in an accessible format
        try {
            String filePrefix = date.toString() + "_";
            String origFileExt = extractFileEnding(file);
            // file created by createTempFile is not different to a regular file
            // advantage: method guarantees to create a new file that did not already exist in the specified folder
            newFile = File.createTempFile(filePrefix,origFileExt, directory);
            newFile.setExecutable(false);
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
    public InputStream get(String filename, String folder) throws ServiceException, FileNotFoundException {
        LOGGER.info("Try to retrieve requested image with name: " + filename);
        String path = System.getProperty("user.dir") + folder;

        try {
            validator.checkFilepath(path, filename);
        }
        catch(InvalidFilePathException | InvalidFileException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        File found = new File(path + filename);
        return new FileInputStream(found);
    }


    @Override
    public void delete(String fileName, String folder) throws FileDeletionException {
        LOGGER.info("Try to delete image with name: " + fileName);
        String path = System.getProperty("user.dir") + folder;
        File toBeDeleted = new File(path + fileName);

        if (!toBeDeleted.exists()) {
            throw new FileDeletionException("there is no file that is referenced by the given name");
        }

        // performs additional checks upon the filename (no relative paths, indeed image)
        // assure that only files/images within oour pre defined folder can be deleted
        try {
            validator.checkFilepath(path, fileName);
        }
        catch(InvalidFilePathException | InvalidFileException | FileNotFoundException e) {
          throw new FileDeletionException(e);
        }

        try {
            Files.delete(toBeDeleted.toPath());
        }
        catch(IOException e) {
            throw new FileDeletionException("the requested image could not be deleted: " + e.getMessage());
        }
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
        String fileName = file.getContentType();
        int lastSlash = fileName.lastIndexOf('/');
        System.out.println(fileName.substring(lastSlash + 1));
        return "." + fileName.substring(lastSlash + 1);
    }
}
