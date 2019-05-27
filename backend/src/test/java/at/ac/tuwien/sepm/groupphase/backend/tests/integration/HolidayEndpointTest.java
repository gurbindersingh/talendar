package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.HolidayDto;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HolidayEndpointTest {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    @LocalServerPort
    private int port = 8080;


    @Test
    @ResponseStatus(HttpStatus.CREATED)
    public void postHolidayResponse() {
        FakeData fakeData = new FakeData();
        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, trequest,
                                   TrainerDto.class
            );
        TrainerDto trainerResponse = tresponse.getBody();

        HolidayDto holiday = new HolidayDto(
            null,
            trainerResponse,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(3)
        );
        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);
        ResponseEntity<HolidayDto> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAY, HttpMethod.POST, request,
                                   HolidayDto.class
            );
        HolidayDto holidayResponse = response.getBody();
        assertNotNull(holidayResponse);
        System.out.println(holidayResponse);
        assertNotNull(holidayResponse.getId());
    }


    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postHolidayStartInPastThenStatus400() {

        FakeData fakeData = new FakeData();
        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, trequest,
                                   TrainerDto.class
            );
        TrainerDto trainerResponse = tresponse.getBody();

        HolidayDto holiday = new HolidayDto(null, trainerResponse, LocalDateTime.now().minusDays(1),
                                            LocalDateTime.now().plusDays(3)
        );

        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);

        assertThrows(HttpClientErrorException.class, () -> {
            final ResponseEntity<HolidayDto> response =
                REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAY, HttpMethod.POST, request,
                                       HolidayDto.class
                );
            assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        });
    }


    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postHolidayEndBeforeStartThenStatus400() {
        FakeData fakeData = new FakeData();
        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, trequest,
                                   TrainerDto.class
            );
        TrainerDto trainerResponse = tresponse.getBody();

        HolidayDto holiday = new HolidayDto(null, trainerResponse, LocalDateTime.now().plusDays(5),
                                            LocalDateTime.now().plusDays(3)
        );
        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);

        assertThrows(HttpClientErrorException.class, () -> {
            final ResponseEntity<HolidayDto> response =
                REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAY, HttpMethod.POST, request,
                                       HolidayDto.class
                );
            assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        });
    }

    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postHolidayWithNullTrainerThenStatus400() {
        HolidayDto holiday = new HolidayDto(null, null, LocalDateTime.now().plusDays(5),
                                            LocalDateTime.now().plusDays(3)
        );

        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);

        assertThrows(HttpClientErrorException.class, () -> {
            final ResponseEntity<HolidayDto> response =
                REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAY, HttpMethod.POST, request,
                                       HolidayDto.class
                );
            assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        });
    }

    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postHolidayWithNoneExistantTrainerThenStatus400() {
        FakeData fakeData = new FakeData();
        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> trequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> tresponse =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, trequest,
                                   TrainerDto.class
            );
        TrainerDto trainerResponse = tresponse.getBody();

        trainerResponse.setId(9999l);

        HolidayDto holiday = new HolidayDto(null, trainerResponse, LocalDateTime.now().plusDays(2),
                                            LocalDateTime.now().plusDays(3)
        );
        HttpEntity<HolidayDto> request = new HttpEntity<>(holiday);

        assertThrows(HttpClientErrorException.class, () -> {
            final ResponseEntity<HolidayDto> response =
                REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAY, HttpMethod.POST, request,
                                       HolidayDto.class
                );
            assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        });
    }


}
