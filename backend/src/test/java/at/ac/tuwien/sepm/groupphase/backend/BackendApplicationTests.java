package at.ac.tuwien.sepm.groupphase.backend;


import at.ac.tuwien.sepm.groupphase.backend.Entity.enums.EventType;
import at.ac.tuwien.sepm.groupphase.backend.TestDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.RoomUseDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.CourseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BackendApplicationTests {

    String phoneRegex = "^[+]*[(]{0,1}[0-9]{1,5}[)]{0,1}[-\\s\\./0-9]*$";
    String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    Pattern phonePattern = Pattern.compile(phoneRegex);
    Pattern emailPattern = Pattern.compile(emailRegex);
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String BASE_URL = "http://localhost:";
    private static final String TRAINER_URL = "/api/talendar/trainers";
    private static final String EVENT_URL = "/api/talendar/event";

    @LocalServerPort
    private int port = 8080;

	@Test
	public void wcontextLoads() {
	    assertEquals("Hello Test", "Hello Test");
	    assertThat("Hello Test", equalToIgnoringCase("HELLO TEST"));
	}


	@Test
    public void checkFakeDataEmail(){
        FakeData fakeData = new FakeData();
        for(int i = 0; i< 100; i++) {
            assert ( emailPattern.matcher(fakeData.fakeEmail()).find() );
        }
    }

    @Test
    public void checkFakeDataPhoneNumber(){
        FakeData fakeData = new FakeData();
        for(int i = 0; i< 100; i++) {
            String number = fakeData.fakePhoneNumber();
            assert ( phonePattern.matcher(number).find());
        }
    }

    @Test
    public void postTrainerResponse(){
        FakeData fakeData = new FakeData();
        TrainerDto trainer = fakeData.fakeTrainer();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> request = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> response = REST_TEMPLATE.exchange(BASE_URL + port + TRAINER_URL, HttpMethod.POST, request, TrainerDto.class);
        TrainerDto trainerResponse = response.getBody();
        assertNotNull(trainerResponse);
        System.out.println(trainerResponse);
        assertNotNull(trainerResponse.getId());
    }


    @Test
    public void postCourseResponse(){
        FakeData fakeData = new FakeData();
        TrainerDto trainerDto = fakeData.fakeTrainer();
        trainerDto.setId(null);
        trainerDto.setUpdated(null);
        trainerDto.setCreated(null);
        HttpEntity<TrainerDto> request = new HttpEntity<>(trainerDto);
        ResponseEntity<TrainerDto> response = REST_TEMPLATE.exchange(BASE_URL + port + TRAINER_URL, HttpMethod.POST, request, TrainerDto.class);
        TrainerDto trainerResponse = response.getBody();
        assertNotNull(trainerResponse);
        System.out.println(trainerResponse);
        assertNotNull(trainerResponse.getId());


        CourseDto courseDto = new CourseDto();
        courseDto.setId(1);
        courseDto.setCreated(null);
        courseDto.setUpdated(null);

        LinkedList<RoomUseDto> roomUseDtoLinkedList = new LinkedList<>();
        roomUseDtoLinkedList.add(fakeData.fakeRoomUse());

        courseDto.setRoomUses(roomUseDtoLinkedList);



        courseDto.setEndOfApplication(LocalDateTime.now().plusDays(2));
        courseDto.setPrice(30.0);
        courseDto.setMaxParticipants(10);
        courseDto.setDescription("Hi, My name is");
        courseDto.setTrainerDto(trainerResponse);
        courseDto.setCustomerDto(null);

        courseDto.setEventType(EventType.Course);

        HttpEntity<CourseDto> requestCourse = new HttpEntity<>(courseDto);
        ResponseEntity<CourseDto> responseCourse = REST_TEMPLATE.exchange(BASE_URL + port + EVENT_URL, HttpMethod.POST, requestCourse, CourseDto.class);
        CourseDto courseResponse = responseCourse.getBody();
        assertNotNull(courseResponse);
        System.out.println(courseResponse);
        assertNotNull(courseResponse.getId());



    }




}
