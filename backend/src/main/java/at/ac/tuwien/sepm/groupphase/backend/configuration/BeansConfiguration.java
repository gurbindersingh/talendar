package at.ac.tuwien.sepm.groupphase.backend.configuration;

import at.ac.tuwien.sepm.groupphase.backend.util.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.TrainerMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class can be used to make any class available as a injectable bean for spring.
 *
 * That is, if a class that is not already part of the Spring Ecosystem should be made available
 * for Spring (e.g. classes from external integrated libs) then they can be made accessible
 * with an according @Bean annotated method.
 */


@Configuration
public class BeansConfiguration {

    @Bean
    public TrainerMapper createTrainerMapper() {
        TrainerMapper trainerMapper = TrainerMapper.INSTANCE;
        return trainerMapper;
    }

    @Bean
    public EventMapper createEventMapper(){
        EventMapper eventMapper = EventMapper.INSTANCE;
        return eventMapper;
    }

}
