package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
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
            x.setEmail(fakeData.fakeEmail());
        }
        HttpEntity<EventDto> request = new HttpEntity<>(birthday);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.POST_BIRTHDAY,
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
    @ResponseStatus(HttpStatus.OK)
    public void postCourseUpdateThen200() {
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
        ResponseEntity<EventDto> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.POST_COURSE,
                                   HttpMethod.POST,
                                   request,
                                   EventDto.class
            );
        EventDto courseResponse = response.getBody();

        courseResponse.setName("Update Course");

        request = new HttpEntity<>(courseResponse);
        System.out.println(request.toString());
        response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT, HttpMethod.PUT, request,
                                   EventDto.class
            );
        courseResponse = response.getBody();

        assertNotNull(courseResponse);
        System.out.println(courseResponse);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }


    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postCourseUpdateWithInvalidMaxAgeThen400() {
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
        ResponseEntity<EventDto> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.POST_COURSE,
                                   HttpMethod.POST,
                                   request,
                                   EventDto.class
            );
        EventDto courseResponse = response.getBody();

        courseResponse.setMaxAge(102);

        HttpEntity<EventDto> requestPut = new HttpEntity<>(courseResponse);
        System.out.println(request.toString());
        assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<EventDto> responseCourse =
                REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT, HttpMethod.PUT, requestPut,
                                       EventDto.class
                );
            assertThat(responseCourse.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        });
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
        ResponseEntity<EventDto> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.POST_COURSE,
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
        ResponseEntity<EventDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.POST_RENT,
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
        HttpEntity<EventDto> request = new HttpEntity<>(consultation);
        System.out.println(request.toString());
        ResponseEntity<EventDto> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.POST_CONSULTATION,
                                   HttpMethod.POST,
                                   request,
                                   EventDto.class
            );
        EventDto consultationResponse = response.getBody();

        assertNotNull(consultationResponse);
        System.out.println(consultationResponse);
        assertNotNull(consultationResponse.getId());
    }


    @Test
    void updateEventCustomers() {
    }


    @Test
    void cancelEvent() {
    }


    @Test
    void getEventById() {
    }


    @Test
    void getAllEventsForAdmin() {
    }


    @Test
    void getAllEventsForTrainer() {
    }


    @Test
    void getAllEventsForClients() {
    }


    @Test
    void getAllFutureCourses() {
    }
}
