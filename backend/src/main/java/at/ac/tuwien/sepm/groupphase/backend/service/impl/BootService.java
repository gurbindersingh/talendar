package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.configuration.properties.UserAccountConfigurationProperties;
import at.ac.tuwien.sepm.groupphase.backend.persistence.TrainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Profile({"production", "development"})
@Component
public class BootService {

    private final Logger LOGGER = LoggerFactory.getLogger(BootService.class);

    private final UserAccountConfigurationProperties userAccountConfigurationProperties;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;

    public BootService(UserAccountConfigurationProperties userAccountConfigurationProperties, TrainerRepository trainerRepository, PasswordEncoder passwordEncoder) {
        this.userAccountConfigurationProperties = userAccountConfigurationProperties;
        this.trainerRepository = trainerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializeBaseAccount()  {
        LocalDateTime timeOfCreation = LocalDateTime.now().minusSeconds(1);
        LOGGER.info("Check if admin account is set upon boot");

        String adminMail = userAccountConfigurationProperties.getEmail();
        String adminPassword = userAccountConfigurationProperties.getPassword();
        String firstName = userAccountConfigurationProperties.getFirstName();
        String lastName = userAccountConfigurationProperties.getLastName();
        String birthdayString = userAccountConfigurationProperties.getBirthday();
        LocalDate birthday = null;
        String phone = userAccountConfigurationProperties.getPhone();
        if (birthdayString != null) {
            birthday = LocalDate.parse(birthdayString);
        }

        Trainer admin;


        // try to retrieve pre configured instance
        admin = trainerRepository.findByEmail(adminMail);

        if (admin == null) {
            admin = new Trainer();
            admin.setEmail(adminMail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFirstName(firstName);
            admin.setLastName(lastName);
            admin.setBirthday(birthday);
            admin.setPhone(phone);
            admin.setAdmin(true);
            admin.setDeleted(false);
            admin.setCreated(timeOfCreation);
            admin.setUpdated(timeOfCreation);
            // currently for admin account: not of subtype trainer but can be changed if needed easily
            try {
                trainerRepository.save(admin);
                LOGGER.info("Admin account dod not exist upon boot. New account was created with given credentials");
            }
            catch(DataAccessException e) {
                LOGGER.error("Admin did not exist upon boot and could not be created. Shutdown!");
                throw new IllegalStateException(e);
            }
        }
    }

}
