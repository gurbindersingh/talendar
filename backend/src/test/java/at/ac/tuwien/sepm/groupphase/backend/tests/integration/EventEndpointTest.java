package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.TestDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventEndpointTest {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    @LocalServerPort
    private int port = 8080;


    @Test
    public void postBirthdayResponse() {
        FakeData fakeData = new FakeData();
        EventDto birthday = fakeData.fakeBirthday();

        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER,
                                                                      HttpMethod.POST,
                                                                      trequest,
                                                                      TrainerDto.class
        );
        TrainerDto trainerResponse = tresponse.getBody();
        List<String> birthdayTypeList = trainerResponse.getBirthdayTypes();

        birthday.setBirthdayType(birthdayTypeList.get(0));
        birthday.setId(null);
        birthday.setUpdated(null);
        birthday.setCreated(null);
        birthday.setTrainer(null);
        for(CustomerDto x : birthday.getCustomerDtos()) {
            x.setId(null);
        }
        HttpEntity<EventDto> request = new HttpEntity<>(birthday);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT,
                                                                   HttpMethod.POST,
                                                                   request,
                                                                   EventDto.class
        );
        EventDto birthdayResponse = response.getBody();


        assertNotNull(birthdayResponse);
        System.out.println(birthdayResponse);
        assertNotNull(birthdayResponse.getId());
    }


    @Test
    public void postCourseResponse() {
        FakeData fakeData = new FakeData();
        EventDto course = fakeData.fakeCourse();

        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER,
                                                                      HttpMethod.POST,
                                                                      trequest,
                                                                      TrainerDto.class
        );
        TrainerDto trainerResponse = tresponse.getBody();
        System.out.println(trainerResponse);

        course.setId(null);
        course.setUpdated(null);
        course.setCreated(null);
        course.setTrainer(trainerResponse);
        course.setCustomerDtos(null);
        HttpEntity<EventDto> request = new HttpEntity<>(course);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT,
                                                                   HttpMethod.POST,
                                                                   request,
                                                                   EventDto.class
        );
        EventDto courseResponse = response.getBody();


        assertNotNull(courseResponse);
        System.out.println(courseResponse);
        assertNotNull(courseResponse.getId());
    }


    @Test
    public void postRentResponse() {
        FakeData fakeData = new FakeData();
        EventDto rent = fakeData.fakeRent();

        rent.setId(null);
        rent.setUpdated(null);
        rent.setCreated(null);
        for(CustomerDto x : rent.getCustomerDtos()
        ) {
            x.setId(null);
        }
        HttpEntity<EventDto> request = new HttpEntity<>(rent);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT,
                                                                   HttpMethod.POST,
                                                                   request,
                                                                   EventDto.class
        );
        EventDto courseResponse = response.getBody();


        assertNotNull(courseResponse);
        System.out.println(courseResponse);
        assertNotNull(courseResponse.getId());
    }


    @Test
    public void postConsultationResponse() {
        FakeData fakeData = new FakeData();
        EventDto consultation = fakeData.fakeConsultation();

        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER,
                                                                      HttpMethod.POST,
                                                                      trequest,
                                                                      TrainerDto.class
        );
        TrainerDto trainerResponse = tresponse.getBody();
        System.out.println(trainerResponse);

        consultation.setId(null);
        consultation.setUpdated(null);
        consultation.setCreated(null);
        consultation.setTrainer(trainerResponse);
        consultation.setCustomerDtos(null);
        HttpEntity<EventDto> request = new HttpEntity<>(consultation);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT,
                                                                   HttpMethod.POST,
                                                                   request,
                                                                   EventDto.class
        );
        EventDto consultationResponse = response.getBody();

        assertNotNull(consultationResponse);
        System.out.println(consultationResponse);
        assertNotNull(consultationResponse.getId());
    }
}
