package at.ac.tuwien.sepm.groupphase.backend.configuration;

import at.ac.tuwien.sepm.groupphase.backend.Entity.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.util.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class can be used to make any class available as a injectable bean for spring.
 *
 * That is, if a class that is not already part of the Spring Ecosystem should be made available
 * for Spring (e.g. classes from external integrated libs) then they can be made accessible
 * with an according @Bean annotated method.
 */


@Configuration
@ComponentScan
public class BeansConfiguration {

    @Bean
    public TrainerMapper createTrainerMapper() {
        TrainerMapper trainerMapper = TrainerMapper.INSTANCE;
        return trainerMapper;
    }

    @Bean
    public HolidayMapper createHolidayMapper() {
        HolidayMapper holidayMapper = HolidayMapper.INSTANCE;
        return holidayMapper;
    }

    @Bean
    public CustomerMapper createCustomerMapper(){
        CustomerMapper customerMapper = CustomerMapper.INSTANCE;
        return customerMapper;
    }

    @Bean
    public EventMapper createEventMapper(){
    	EventMapper eventMapper = EventMapper.INSTANCE;
    	return eventMapper;
    }

    @Bean
    public RoomUseMapper createRoomUseMapper(){
        RoomUseMapper roomUseMapper = RoomUseMapper.INSTANCE;
        return roomUseMapper;
    }

    @Bean
    public TagMapper createTagMapper(){
        TagMapper tagMapper = TagMapper.INSTANCE;
        return tagMapper;
    }

    @Bean
    public ConsultingTimeMapper createConsultingTimeMapper() {
        ConsultingTimeMapper consultingTimeMapper = ConsultingTimeMapper.INSTANCE;
        return consultingTimeMapper;
    }

    @Bean
    public BirthdayTypeMapper createBirthdayTypeMapper(){
        BirthdayTypeMapper birthdayTypeMapper = BirthdayTypeMapper.INSTANCE;
        return birthdayTypeMapper;
    }
    
    /***
     *  Encoder used for spring security related process'
     */
    @Bean
    public static PasswordEncoder configureDefaultPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
