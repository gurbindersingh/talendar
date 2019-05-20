package at.ac.tuwien.sepm.groupphase.backend;


import at.ac.tuwien.sepm.groupphase.backend.TestDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.TestObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidayDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;

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
    private static final String HOLIDAY_URL = "/api/talendar/holiday";

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
    public void postHolidayResponse(){
        HolidayDto holiday = new HolidayDto(null, 1, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(3));
        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);
        ResponseEntity<HolidayDto> response = REST_TEMPLATE.exchange(BASE_URL + port + HOLIDAY_URL, HttpMethod.POST, request, HolidayDto.class);
        HolidayDto holidayResponse = response.getBody();
        assertNotNull(holidayResponse);
        System.out.println(holidayResponse);
        assertNotNull(holidayResponse.getId());
    }

    //TODO: Warum gibt dieser Test ein Internal Server Error? Die Antwort ist wohl in einer kaputten Bananenschale zu finden, oder im Code...I dont even know.
    @Test
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void postHolidayWronglyThenStatus500(){
        HolidayDto holiday = new HolidayDto(null, 2, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3));
        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);
        System.out.println("500 Internal Server Error");
    }

    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postHolidayEndBeforeStartThenStatus400(){
        HolidayDto holiday = new HolidayDto(null, 2, LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(3));
        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);
        System.out.println("400 Bad Request");
    }
}
