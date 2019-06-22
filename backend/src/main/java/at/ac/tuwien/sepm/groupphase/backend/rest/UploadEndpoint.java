package at.ac.tuwien.sepm.groupphase.backend.rest;

import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.StorageProperties;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.BackendException;
import at.ac.tuwien.sepm.groupphase.backend.service.IImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/talendar/upload")
public class UploadEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadEndpoint.class);

    private final IImageService imageService;
    private final StorageProperties storageProperties;

    @Autowired
    public UploadEndpoint(IImageService imageService, StorageProperties storageProperties) {
        this.imageService = imageService;
        this.storageProperties = storageProperties;
    }

    @PostMapping(value = "/image/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadProfilePicture(@RequestBody MultipartFile file) throws BackendException {
        LOGGER.info("Incoming POST Request For Uploading A Profile Picture");

        try {
            return imageService.save(file, storageProperties.getProfileImgFolder());
        }
        catch(ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException("Das Profilbild konnte nicht gespeichert werden, bitte versuch es später erneut.", e);
        }
        catch(ValidationException e) {
            LOGGER.error(e.getMessage(),e);
            throw new BackendException("Das Profilbild konnte nicht gespeichert werden: " + e.getMessage(), e);
        }
    }
    @PostMapping(value = "/image/course")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadCoursePictures(@RequestBody MultipartFile file) throws BackendException {
        LOGGER.info("Incoming POST Request For Uploading A Course Picture");


        try {
            return imageService.save(file, storageProperties.getCourseImgFolder());
        }
        catch(ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException("Das Profilbild konnte nicht gespeichert werden, bitte versuch es später erneut.", e);
        }
        catch(ValidationException e) {
            LOGGER.error(e.getMessage(),e);
            throw new BackendException("Das Profilbild konnte nicht gespeichert werden: " + e.getMessage(), e);
        }
    }

    @GetMapping(value = "image/trainer", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getProfilePicture(@RequestParam("name") String name) throws BackendException {
        LOGGER.info("Incoming GET Request For Retrieving Profile Picture");

        try {
            InputStream inputStream = imageService.get(name, storageProperties.getProfileImgFolder());
            return inputStream.readAllBytes();
        }
        catch(ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException("Es konnte kein Profilbild geladen werden", e);
        }
        catch(FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException("Es existiert kein Bild mit dem gegebenen Filenamen", e);
        }
        catch(IOException e) {
            LOGGER.error("error occured while reading from input stream: " + e.getMessage(), e);
            throw new BackendException("Es konnte kein Profilbild geladen werden", e);
        }
    }

    @GetMapping(value = "image/course", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getCoursePicture(@RequestParam("name") String name) throws BackendException {
        LOGGER.info("Incoming GET Request For Retrieving Course Picture");

        try {
            InputStream inputStream = imageService.get(name, storageProperties.getCourseImgFolder());
            return inputStream.readAllBytes();
        }
        catch(ServiceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException("Es konnte kein Profilbild geladen werden", e);
        }
        catch(FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new BackendException("Es existiert kein Bild mit dem gegebenen Filenamen", e);
        }
        catch(IOException e) {
            LOGGER.error("error occured while reading from input stream: " + e.getMessage(), e);
            throw new BackendException("Es konnte kein Profilbild geladen werden", e);
        }
    }
}
