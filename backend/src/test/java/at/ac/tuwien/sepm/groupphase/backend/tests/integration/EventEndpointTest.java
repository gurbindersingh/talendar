package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.enums.BirthdayType;
import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventEndpointTest {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    @LocalServerPort
    private int port = 8080;

    @Test
    public void postBirthdayResponse () {
        FakeData fakeData = new FakeData();
        EventDto birthday = fakeData.fakeBirthday();

        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, trequest, TrainerDto.class);
        TrainerDto trainerResponse = tresponse.getBody();
        List<String> birthdayTypeList = trainerResponse.getBirthdayTypes();

        birthday.setBirthdayType(birthdayTypeList.get(0));
        birthday.setId(null);
        birthday.setUpdated(null);
        birthday.setCreated(null);
        birthday.setTrainer(null);
        for(CustomerDto x : birthday.getCustomerDtos()
        ) {
            x.setId(null);
        }
        HttpEntity<EventDto> request = new HttpEntity<>(birthday);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT, HttpMethod.POST, request, EventDto.class);
        EventDto birthdayResponse = response.getBody();


        assertNotNull(birthdayResponse);
        System.out.println(birthdayResponse);
        assertNotNull(birthdayResponse.getId());
    }


    @Test
    public void postCourseResponse () {
        FakeData fakeData = new FakeData();
        EventDto course = fakeData.fakeCourse();

        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, trequest, TrainerDto.class);
        TrainerDto trainerResponse = tresponse.getBody();
        System.out.println(trainerResponse);

        course.setId(null);
        course.setUpdated(null);
        course.setCreated(null);
        course.setTrainer(trainerResponse);
        course.setCustomerDtos(null);
        HttpEntity<EventDto> request = new HttpEntity<>(course);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT, HttpMethod.POST, request, EventDto.class);
        EventDto courseResponse = response.getBody();


        assertNotNull(courseResponse);
        System.out.println(courseResponse);
        assertNotNull(courseResponse.getId());
    }
}
