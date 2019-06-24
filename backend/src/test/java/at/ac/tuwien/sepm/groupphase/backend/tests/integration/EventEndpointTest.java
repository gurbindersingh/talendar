package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.persistence.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
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

    @Autowired
    private EventRepository eventRepository;

    private FakeData faker = new FakeData();

    @Test
    public void postBirthdayResponse() {
        EventDto birthday = faker.fakeBirthday();

        TrainerDto trainer = faker.fakeTrainerDto();
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
            x.setEmail(faker.fakeEmail());
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
        EventDto course = faker.fakeCourse();

        TrainerDto trainer = faker.fakeTrainerDto();
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
        EventDto course = faker.fakeCourse();

        TrainerDto trainer = faker.fakeTrainerDto();
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
        EventDto course = faker.fakeCourse();

        TrainerDto trainer = faker.fakeTrainerDto();
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
        EventDto rent = faker.fakeRent();

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
        EventDto consultation = faker.fakeConsultation();

        TrainerDto trainer = faker.fakeTrainerDto();
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
    void cancelEvent() {
        EventDto course = faker.fakeCourse();

        TrainerDto trainer = faker.fakeTrainerDto();
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

        given().when()
               .delete(URL.BASE + port + URL.EVENT + "/" + courseResponse.getId())
               .then()
               .statusCode(200);
    }


    @Test
    void getEventById() {
        EventDto course = faker.fakeCourse();

        TrainerDto trainer = faker.fakeTrainerDto();
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

        int courseId = given().contentType("application/json")
                        .body(course)
                        .when()
                        .post(URL.BASE + port + URL.POST_COURSE)
                        .then()
                        .extract()
                        .path("id");

        given().when()
               .get(URL.BASE + port + URL.EVENT + "/" + courseId)
               .then()
               .statusCode(200);
    }


    @Test
    void getAllEventsForAdmin_shouldContainAllEventsWithAllData() {
        // prepare trainers
        TrainerDto trainer1 = faker.fakeTrainerDto();
        trainer1.setId(1l);
        HttpEntity<TrainerDto> trainer1Request = new HttpEntity<>(trainer1);
        TrainerDto trainer2 = faker.fakeTrainerDto();
        trainer2.setId(2l);
        HttpEntity<TrainerDto> trainer2Request = new HttpEntity<>(trainer2);
        TrainerDto trainer3 = faker.fakeTrainerDto();
        trainer3.setId(3l);
        HttpEntity<TrainerDto> trainer3Request = new HttpEntity<>(trainer3);

        ResponseEntity<TrainerDto> trainerResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer1Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer2Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer3Request, TrainerDto.class);


        // prepare events
        EventDto course = faker.fakeCourse();
        course.setCustomerDtos(null);
        course.setTrainer(trainerResponse1.getBody());
        HttpEntity<EventDto> courseRequest1 = new HttpEntity<>(course);
        EventDto birthday = faker.fakeBirthday();
        birthday.getCustomerDtos().forEach((dto) -> dto.setId(null));
        birthday.setTrainer(trainerResponse2.getBody());
        HttpEntity<EventDto> birthdayRequest2 = new HttpEntity<>(birthday);
        EventDto rent = faker.fakeRent();
        rent.getCustomerDtos().forEach((dto) -> dto.setId(null));
        HttpEntity<EventDto> rentRequest3 = new HttpEntity<>(rent);

        ResponseEntity<EventDto> courseResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_COURSE, courseRequest1, EventDto.class);
        ResponseEntity<EventDto> birthdayResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_BIRTHDAY, birthdayRequest2, EventDto.class);
        ResponseEntity<EventDto> rentResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_RENT, rentRequest3, EventDto.class);


        // check that this endpoint retuns complete list of all events
        ResponseEntity<List<EventDto>> finalResponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT + "/all/admin", HttpMethod.GET, null, new ParameterizedTypeReference<List<EventDto>>() {});
        List<EventDto> foundEvents = finalResponse.getBody();

        // contains all
        assertThat(foundEvents.size(), is(3));
        // contains all data are available ('completely' for admins)
        // better perform an assert(x, equalTo(y)) check but awkwardly the values differ slightly
        // like difference at 5 position after comma... (not clear why though)
        assertThat(foundEvents.get(0).getId(), equalTo(courseResponse1.getBody().getId()));
        assertThat(foundEvents.get(0).getName(), equalTo(courseResponse1.getBody().getName()));
        assertThat(foundEvents.get(0).getEventType(), equalTo(courseResponse1.getBody().getEventType()));
        assertThat(foundEvents.get(0).getTrainer().getId(), equalTo(courseResponse1.getBody().getTrainer().getId()));
        assertThat(foundEvents.get(0).getMinAge(), equalTo(courseResponse1.getBody().getMinAge()));
        assertThat(foundEvents.get(0).getMaxAge(), equalTo(courseResponse1.getBody().getMaxAge()));
        assertThat(foundEvents.get(0).getPrice(), equalTo(courseResponse1.getBody().getPrice()));
        assertThat(foundEvents.get(0).getMaxParticipants(), equalTo(courseResponse1.getBody().getMaxParticipants()));

        assertThat(foundEvents.get(1).getId(), equalTo(birthdayResponse2.getBody().getId()));
        assertThat(foundEvents.get(1).getName(), equalTo(birthdayResponse2.getBody().getName()));
        assertThat(foundEvents.get(1).getEventType(), equalTo(birthdayResponse2.getBody().getEventType()));
        assertThat(foundEvents.get(1).getTrainer().getId(), equalTo(birthdayResponse2.getBody().getTrainer().getId()));
        assertThat(foundEvents.get(1).getAgeToBe(), equalTo(birthdayResponse2.getBody().getAgeToBe()));
        assertThat(foundEvents.get(1).getHeadcount(), equalTo(birthdayResponse2.getBody().getHeadcount()));
        assertThat(foundEvents.get(1).getBirthdayType(), equalTo(birthdayResponse2.getBody().getBirthdayType()));

        assertThat(foundEvents.get(2).getId(), equalTo(rentResponse3.getBody().getId()));
        assertThat(foundEvents.get(2).getName(), equalTo(rentResponse3.getBody().getName()));
        assertThat(foundEvents.get(2).getEventType(), equalTo(rentResponse3.getBody().getEventType()));
    }





    @Test
    void getAllEventsForTrainer_shouldContainAllEvents_nonOwnMarkedAsHidden() {
        // prepare trainers
        TrainerDto trainer1 = faker.fakeTrainerDto();
        trainer1.setId(1l);
        HttpEntity<TrainerDto> trainer1Request = new HttpEntity<>(trainer1);
        TrainerDto trainer2 = faker.fakeTrainerDto();
        trainer2.setId(2l);
        HttpEntity<TrainerDto> trainer2Request = new HttpEntity<>(trainer2);
        TrainerDto trainer3 = faker.fakeTrainerDto();
        trainer3.setId(3l);
        HttpEntity<TrainerDto> trainer3Request = new HttpEntity<>(trainer3);

        ResponseEntity<TrainerDto> trainerResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer1Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer2Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer3Request, TrainerDto.class);


        // prepare events
        EventDto course = faker.fakeCourse();
        course.setCustomerDtos(null);
        course.setTrainer(trainerResponse1.getBody());
        HttpEntity<EventDto> courseRequest1 = new HttpEntity<>(course);
        EventDto birthday = faker.fakeBirthday();
        birthday.getCustomerDtos().forEach((dto) -> dto.setId(null));
        birthday.setTrainer(trainerResponse2.getBody());
        HttpEntity<EventDto> birthdayRequest2 = new HttpEntity<>(birthday);
        EventDto rent = faker.fakeRent();
        rent.getCustomerDtos().forEach((dto) -> dto.setId(null));
        HttpEntity<EventDto> rentRequest3 = new HttpEntity<>(rent);

        ResponseEntity<EventDto> courseResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_COURSE, courseRequest1, EventDto.class);
        ResponseEntity<EventDto> birthdayResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_BIRTHDAY, birthdayRequest2, EventDto.class);
        ResponseEntity<EventDto> rentResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_RENT, rentRequest3, EventDto.class);


        // check that request contains complete info for own event but no for non hosted events events and rents
        ResponseEntity<List<EventDto>> finalResponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT + "/all/trainer/2", HttpMethod.GET, null, new ParameterizedTypeReference<List<EventDto>>() {});
        List<EventDto> foundEvents = finalResponse.getBody();

        // all events loaded but...
        assertThat(foundEvents.size(), is(3));
        // only our own event has everything set and...
        assertThat(foundEvents.get(1).getId(), equalTo(birthdayResponse2.getBody().getId()));
        assertThat(foundEvents.get(1).getName(), equalTo(birthdayResponse2.getBody().getName()));
        assertThat(foundEvents.get(1).getEventType(), equalTo(birthdayResponse2.getBody().getEventType()));
        assertThat(foundEvents.get(1).getTrainer().getId(), equalTo(birthdayResponse2.getBody().getTrainer().getId()));
        assertThat(foundEvents.get(1).getAgeToBe(), equalTo(birthdayResponse2.getBody().getAgeToBe()));
        assertThat(foundEvents.get(1).getHeadcount(), equalTo(birthdayResponse2.getBody().getHeadcount()));
        assertThat(foundEvents.get(1).getBirthdayType(), equalTo(birthdayResponse2.getBody().getBirthdayType()));

        // .. that other events are marked as subordinate
        assertThat(foundEvents.get(0).isHide(), is(true));
        assertThat(foundEvents.get(2).isHide(), is(true));
    }






    @Test
    void getAllEventsForClients_shouldContainAllEvents_anyExceptCourseIsRedacted() {
        // prepare trainers
        TrainerDto trainer1 = faker.fakeTrainerDto();
        trainer1.setId(1l);
        HttpEntity<TrainerDto> trainer1Request = new HttpEntity<>(trainer1);
        TrainerDto trainer2 = faker.fakeTrainerDto();
        trainer2.setId(2l);
        HttpEntity<TrainerDto> trainer2Request = new HttpEntity<>(trainer2);
        TrainerDto trainer3 = faker.fakeTrainerDto();
        trainer3.setId(3l);
        HttpEntity<TrainerDto> trainer3Request = new HttpEntity<>(trainer3);

        ResponseEntity<TrainerDto> trainerResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer1Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer2Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer3Request, TrainerDto.class);


        // prepare events
        EventDto course = faker.fakeCourse();
        course.setCustomerDtos(null);
        course.setTrainer(trainerResponse1.getBody());
        HttpEntity<EventDto> courseRequest1 = new HttpEntity<>(course);
        EventDto birthday = faker.fakeBirthday();
        birthday.getCustomerDtos().forEach((dto) -> dto.setId(null));
        birthday.setTrainer(trainerResponse2.getBody());
        HttpEntity<EventDto> birthdayRequest2 = new HttpEntity<>(birthday);
        EventDto rent = faker.fakeRent();
        rent.getCustomerDtos().forEach((dto) -> dto.setId(null));
        HttpEntity<EventDto> rentRequest3 = new HttpEntity<>(rent);

        ResponseEntity<EventDto> courseResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_COURSE, courseRequest1, EventDto.class);
        ResponseEntity<EventDto> birthdayResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_BIRTHDAY, birthdayRequest2, EventDto.class);
        ResponseEntity<EventDto> rentResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_RENT, rentRequest3, EventDto.class);


        // check that client only has complete information about courses, but no visble infp aboout any other evenz
        ResponseEntity<List<EventDto>> finalResponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT + "/all/client", HttpMethod.GET, null, new ParameterizedTypeReference<List<EventDto>>() {});
        List<EventDto> foundEvents = finalResponse.getBody();

        // course is displayed normally
        assertThat(foundEvents.get(0).getId(), equalTo(courseResponse1.getBody().getId()));
        assertThat(foundEvents.get(0).getName(), equalTo(courseResponse1.getBody().getName()));
        assertThat(foundEvents.get(0).getEventType(), equalTo(courseResponse1.getBody().getEventType()));
        assertThat(foundEvents.get(0).getTrainer().getId(), equalTo(courseResponse1.getBody().getTrainer().getId()));
        assertThat(foundEvents.get(0).getMinAge(), equalTo(courseResponse1.getBody().getMinAge()));
        assertThat(foundEvents.get(0).getMaxAge(), equalTo(courseResponse1.getBody().getMaxAge()));
        assertThat(foundEvents.get(0).getPrice(), equalTo(courseResponse1.getBody().getPrice()));
        assertThat(foundEvents.get(0).getMaxParticipants(), equalTo(courseResponse1.getBody().getMaxParticipants()));

        // birthday and rent are not displayed (marked as redacted)
        assertThat(foundEvents.get(1).isRedacted(), is(true));
        assertThat(foundEvents.get(2).isRedacted(), is(true));

        // data are removed
        assertThat(foundEvents.get(1).getId(), not(equalTo(birthdayResponse2.getBody().getId())));
        assertThat(foundEvents.get(1).getName(), not(equalTo(birthdayResponse2.getBody().getName())));
        assertThat(foundEvents.get(1).getEventType(), nullValue());
        assertThat(foundEvents.get(1).getCustomerDtos(), nullValue());
        assertThat(foundEvents.get(1).getTrainer(), nullValue());
        assertThat(foundEvents.get(1).getPrice(), nullValue());
        assertThat(foundEvents.get(1).getMinAge(), nullValue());
        assertThat(foundEvents.get(1).getMaxAge(), nullValue());

        assertThat(foundEvents.get(2).getId(), not(equalTo(rentResponse3.getBody().getId())));
        assertThat(foundEvents.get(2).getName(), not(equalTo(rentResponse3.getBody().getName())));
        assertThat(foundEvents.get(2).getEventType(), nullValue());
        assertThat(foundEvents.get(2).getCustomerDtos(), nullValue());
    }



    @Test
    void getAllFutureEventsForAdmin() {
        // prepare trainers
        TrainerDto trainer1 = faker.fakeTrainerDto();
        trainer1.setId(1l);
        HttpEntity<TrainerDto> trainer1Request = new HttpEntity<>(trainer1);
        TrainerDto trainer2 = faker.fakeTrainerDto();
        trainer2.setId(2l);
        HttpEntity<TrainerDto> trainer2Request = new HttpEntity<>(trainer2);
        TrainerDto trainer3 = faker.fakeTrainerDto();
        trainer3.setId(3l);
        HttpEntity<TrainerDto> trainer3Request = new HttpEntity<>(trainer3);

        ResponseEntity<TrainerDto> trainerResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer1Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer2Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer3Request, TrainerDto.class);


        EventDto course = faker.fakeCourse();
        course.setCustomerDtos(null);
        course.setTrainer(trainerResponse1.getBody());
        HttpEntity<EventDto> courseRequest1 = new HttpEntity<>(course);
        EventDto birthday = faker.fakeBirthday();
        birthday.getCustomerDtos().forEach((dto) -> dto.setId(null));
        birthday.setTrainer(trainerResponse2.getBody());
        HttpEntity<EventDto> birthdayRequest2 = new HttpEntity<>(birthday);
        EventDto rent = faker.fakeRent();
        rent.getCustomerDtos().forEach((dto) -> dto.setId(null));
        HttpEntity<EventDto> rentRequest3 = new HttpEntity<>(rent);


        /**
         * Inject Course Manually, will be soon outdated!
         */
      /*  TrainerDto postedCourse = trainerResponse1.getBody();
        //Trainer trainerHack = new Trainer(null, postedCourse.getFirstName(), postedCourse.getLastName(), postedCourse.getBirthday(), postedCourse.getPhone(), postedCourse.getEmail(), postedCourse.getPassword(), postedCourse, null, null, null, null, postedCourse.getCreated(), postedCourse.getCreated()) ;
        Trainer trainerHack = faker.fakeTrainerEntity();
        trainerHack.setId(null);
        Event eventHack = faker.fakeNewCourseEntity();

        eventHack.setTrainer(trainerHack);
        eventHack.getRoomUses().get(0).setBegin(LocalDateTime.now().plusSeconds(1));
        eventHack.setCreated(LocalDateTime.now().minusSeconds(1));
        eventHack.setUpdated(LocalDateTime.now().minusSeconds(1));



        Event savedHack = eventRepository.save(eventHack);



        // wait until course has started
        try {
            Thread.sleep(1000);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
*/

        ResponseEntity<EventDto> courseResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_COURSE, courseRequest1, EventDto.class);
        ResponseEntity<EventDto> birthdayResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_BIRTHDAY, birthdayRequest2, EventDto.class);
        ResponseEntity<EventDto> rentResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_RENT, rentRequest3, EventDto.class);



        // check that client only has complete information about courses, but no visble infp aboout any other evenz
        ResponseEntity<List<EventDto>> finalResponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT + "/all/future/admin", HttpMethod.GET, null, new ParameterizedTypeReference<List<EventDto>>() {});
        List<EventDto> foundEvents = finalResponse.getBody();

        // admin can get any any event in future, but course has already started so only rnet and bd returned
        assertThat(foundEvents.size(), is(3));
    }

    @Test
    void getAllFutureEventsForTrainer() {
        // prepare trainers
        TrainerDto trainer1 = faker.fakeTrainerDto();
        trainer1.setId(1l);
        List<String> bdType1 = new LinkedList<>();
        bdType1.add("Dryice");
        trainer1.setBirthdayTypes(bdType1);

        HttpEntity<TrainerDto> trainer1Request = new HttpEntity<>(trainer1);
        TrainerDto trainer2 = faker.fakeTrainerDto();
        trainer2.setId(2l);
        List<String> bdType2 = new LinkedList<>();
        bdType2.add("Superhero");
        trainer2.setBirthdayTypes(bdType2);

        HttpEntity<TrainerDto> trainer2Request = new HttpEntity<>(trainer2);
        TrainerDto trainer3 = faker.fakeTrainerDto();
        trainer3.setId(3l);
        List<String> bdType3 = new LinkedList<>();
        bdType3.add("Photography");
        trainer3.setBirthdayTypes(bdType3);
        HttpEntity<TrainerDto> trainer3Request = new HttpEntity<>(trainer3);

        ResponseEntity<TrainerDto> trainerResponse1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer1Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer2Request, TrainerDto.class);
        ResponseEntity<TrainerDto> trainerResponse3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainer3Request, TrainerDto.class);


        EventDto course = faker.fakeCourse();
        course.setCustomerDtos(null);
        course.setTrainer(trainerResponse1.getBody());
        HttpEntity<EventDto> courseRequest1 = new HttpEntity<>(course);
        EventDto birthday = faker.fakeBirthday();
        birthday.setBirthdayType("Superhero");
        birthday.setTrainer(trainerResponse2.getBody());
        birthday.getCustomerDtos().forEach((dto) -> dto.setId(null));
        HttpEntity<EventDto> birthdayRequest2 = new HttpEntity<>(birthday);
        EventDto rent = faker.fakeRent();
        rent.getCustomerDtos().forEach((dto) -> dto.setId(null));
        HttpEntity<EventDto> rentRequest3 = new HttpEntity<>(rent);

        REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_COURSE, courseRequest1, EventDto.class);
        REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_BIRTHDAY, birthdayRequest2, EventDto.class);
        REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_RENT, rentRequest3, EventDto.class);


        // check that client only has complete information about courses, but no visible info about any other evenz
        ResponseEntity<List<EventDto>> finalResponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.EVENT + "/all/future/trainer/" + trainerResponse2.getBody().getId(), HttpMethod.GET, null, new ParameterizedTypeReference<List<EventDto>>() {});
        List<EventDto> foundEvents = finalResponse.getBody();
        
        assertThat(foundEvents.size(), is(1));
    }

}
