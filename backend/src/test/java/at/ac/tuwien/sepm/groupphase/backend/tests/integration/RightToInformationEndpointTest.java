package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.Entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.Entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exceptions.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.CustomerDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RightToInformationEndpointTest {

    private static final TestRestTemplate REST_TEMPLATE = new TestRestTemplate();

    @LocalServerPort
    private int port = 8080;

    private FakeData fakeData = new FakeData();


    @Test
    public void requestByNonExistentCustomer_shouldFail_andStatusIs() {
        CustomerDto customerDto = fakeData.fakeCustomer();

        // can not wrap it in assertThrows because RestTemplate will not pass controller exceptions
        // on some basic types like String.class or xyz[].class types
        // the error response is available though, which is fine
        ResponseEntity<byte[]> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.INFO + "/" + customerDto.getEmail(),
                                   HttpMethod.GET, null,
                                   byte[].class
            );
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void requestInfoForExistentCustomer_shouldReturnByteStreamAsPdf() {
        TrainerDto trainer =  fakeData.fakeTrainerDto();
        HttpEntity<TrainerDto> trainerRequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> trainerResponse = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, trainerRequest, TrainerDto.class);
        TrainerDto postedTrainer = trainerResponse.getBody();
        trainer.setId(postedTrainer.getId());

        EventDto eventDto = fakeData.fakeCourse();
        // assure that regstration for this event is made possible
        eventDto.setMinAge(5);
        eventDto.setMaxAge(100);
        eventDto.setMaxParticipants(8);
        eventDto.setCustomerDtos(null);
        eventDto.setTrainer(trainer);
        HttpEntity<EventDto> requestEvent = new HttpEntity<>(eventDto);
        ResponseEntity<EventDto> eventResponse = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.POST_COURSE, requestEvent, EventDto.class);
        EventDto postedEvent = eventResponse.getBody();

        assertThat(postedEvent, notNullValue());

        CustomerDto customer = fakeData.fakeCustomerForSignIn();
        eventDto.setId(postedEvent.getId());
        Set<CustomerDto> customers = new HashSet<>();
        customers.add(customer);
        eventDto.setCustomerDtos(customers);

        HttpEntity<EventDto> courseRegistrationRequest = new HttpEntity<>(eventDto);
        ResponseEntity<EventDto> registrationRequest = REST_TEMPLATE.exchange(URL.BASE + port + URL.PUT_CUSTOMERS, HttpMethod.PUT, courseRegistrationRequest, EventDto.class);
        EventDto eventAfterRegistration = registrationRequest.getBody();

        assertThat(eventAfterRegistration, notNullValue());


        ResponseEntity<byte[]> response =
            REST_TEMPLATE.exchange(URL.BASE + port + URL.INFO + "/" + customer.getEmail(),
                                   HttpMethod.GET, null,
                                   byte[].class
            );
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        /**
         * Maybe it should also be tested that response is actually an byte stream that can be
         * interpreted as PDF
         * but no idea how to do it...
         */
    }
}

