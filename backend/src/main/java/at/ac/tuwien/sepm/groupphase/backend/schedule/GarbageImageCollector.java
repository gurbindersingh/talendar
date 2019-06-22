package at.ac.tuwien.sepm.groupphase.backend.schedule;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.StorageProperties;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

@Component
public class GarbageImageCollector {
    private final Logger LOGGER = LoggerFactory.getLogger(GarbageImageCollector.class);

    private final StorageProperties storageProperties;
    private final TrainerRepository trainerRepository;

    public GarbageImageCollector(StorageProperties storageProperties, TrainerRepository trainerRepository) {
        this.storageProperties = storageProperties;
        this.trainerRepository = trainerRepository;
    }

    public void deleteLegacyProfilePictures() {
        // get all files in profile picture folder
        String folderName = System.getProperty("user.dir") + storageProperties.getProfileImgFolder();
        // remove pending '/' to open directory as file instance
        File folder = new File(folderName.substring(0,folderName.length() - 1));
        File[] images = folder.listFiles();

        System.out.println(
            images.length
        );

        // get all active trainers
        List<Trainer> activeTrainer = trainerRepository.findByDeletedFalse();

        System.out.println(activeTrainer.size());
        List<String> savedImageNames = new LinkedList();

        // and list of each associated image
        for (Trainer trainer: activeTrainer) {
            if (trainer.getPicture() != null) {
                savedImageNames.add(trainer.getPicture());
            }
        }

        for(File image: images) {
            System.out.println(
                image.getName()
            );
            if (!savedImageNames.contains(image.getName())) {
                try {
                    Files.delete(image.toPath());
                }
                catch(IOException e) {
                    LOGGER.warn("garbage image (unassociated) could not be deleted: " + e.getMessage());
                }
            }
        }

    }
}
