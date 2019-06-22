package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.rest.dto.HolidaysDto;
import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.HolidayDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HolidaysEndpointTest {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    @LocalServerPort
    private int port = 8080;

    //30/30 13/15 30/30 5/5 2019/2019 true O2 4 Nach 3
    @Test
    @ResponseStatus(HttpStatus.CREATED)
    public void postHolidaysResponse() {
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
        String cronExpression = "30/30 13/15 30/30 5/5 2020/2020 true O2 4 Nach 3 ";

        HolidaysDto holiday = new HolidaysDto(
            trainerResponse.getId(),
            "TestTitle",
            "TestDescription",
            cronExpression
        );
        HttpEntity<HolidaysDto> request = new HttpEntity<>(holiday);
        ResponseEntity<HolidayDto[]> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAYS, HttpMethod.POST, request,
                                   HolidayDto[].class
            );
        HolidayDto[] holidayResponse = response.getBody();
        assertNotNull(holidayResponse);
        System.out.println(holidayResponse);
        assertNotNull(holidayResponse[0].getId());
    }

    @Test
    @ResponseStatus(HttpStatus.CREATED)
    public void postHolidaysTwiceResponse() {
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
        String cronExpression = "30/30 13/15 30/30 5/5 2020/2020 true O2 4 Nach 3 ";

        HolidaysDto holidays = new HolidaysDto(
            trainerResponse.getId(),
            "TestTitle",
            "TestDescription",
            cronExpression
        );
        HttpEntity<HolidaysDto> request = new HttpEntity<>(holidays);
        ResponseEntity<HolidayDto[]> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAYS, HttpMethod.POST, request,
                                   HolidayDto[].class
            );
        HolidayDto[] holidayResponse = response.getBody();
        assertNotNull(holidayResponse);
        System.out.println(holidayResponse);
        assertNotNull(holidayResponse[0].getId());


        cronExpression = "30/30 13/15 30/30 5/5 2020/2020 true O3 1 Nach 2 ";

        holidays = new HolidaysDto(
            trainerResponse.getId(),
            "TestTitle",
            "TestDescription",
            cronExpression
        );
        request = new HttpEntity<>(holidays);
        ResponseEntity<HolidayDto[]> responseTwo =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAYS, HttpMethod.POST, request,
                                   HolidayDto[].class
            );
        HolidayDto[] holidayResponseTwo = responseTwo.getBody();
        assertNotNull(holidayResponseTwo);
        System.out.println(holidayResponseTwo);
        assertNotNull(holidayResponseTwo[0].getId());
    }

    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postHolidaysWithEndDateForStartDateThenError400() {
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
        String cronExpression = "29/30 13/15 30/30 5/5 2020/2020 true O2 4 Nach 3 ";

        HolidaysDto holiday = new HolidaysDto(
            trainerResponse.getId(),
            "TestTitle",
            "TestDescription",
            cronExpression
        );
        HttpEntity<HolidaysDto> request = new HttpEntity<>(holiday);
        ResponseEntity<HolidayDto[]> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAYS, HttpMethod.POST, request,
                                   HolidayDto[].class
            );
        HolidayDto[] holidayResponse = response.getBody();
        assertNotNull(holidayResponse);
        System.out.println(holidayResponse);
        assertNotNull(holidayResponse[0].getId());
    }

    @Test
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void postHolidaysWithInvalidTrainerIdThenError400() {
        String cronExpression = "29/30 13/15 30/30 5/5 2020/2020 true O2 4 Nach 3 ";

        HolidaysDto holiday = new HolidaysDto(
            9999l,
            "TestTitle",
            "TestDescription",
            cronExpression
        );
        HttpEntity<HolidaysDto> request = new HttpEntity<>(holiday);

        assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<HolidayDto[]> response =
                REST_TEMPLATE.exchange(URL.BASE + port + URL.HOLIDAYS, HttpMethod.POST, request,
                                       HolidayDto[].class);
            assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        });
    }

}

