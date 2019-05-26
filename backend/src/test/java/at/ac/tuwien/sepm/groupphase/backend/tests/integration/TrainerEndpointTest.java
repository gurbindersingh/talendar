package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.Trainer;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TrainerEndpointTest {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    @LocalServerPort
    private int port = 8080;

    private FakeData fakeData = new FakeData();


    /**
     *  TEST Trainer Post (Valid Trainer Saved And Id/Timestamps Set and Invalid Leads To Bad
     *  Request Status)
     */

    @Test
    public void postTrainerResponse () {
        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        HttpEntity<TrainerDto> request = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, request, TrainerDto.class);
        TrainerDto trainerResponse = response.getBody();
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertNotNull(trainerResponse);
        assertNotNull(trainerResponse.getId());
        assertNotNull(trainerResponse.getCreated());
        assertNotNull(trainerResponse.getUpdated());
    }


    @Test
    public void postInvalidTrainer_Status400Expected()  {
        FakeData fakeData = new FakeData();
        TrainerDto trainer = fakeData.fakeTrainerDto();
        trainer.setId(null);
        trainer.setUpdated(null);
        trainer.setCreated(null);
        trainer.setFirstName("");
        HttpEntity<TrainerDto> request = new HttpEntity<>(trainer);
        assertThrows(HttpClientErrorException.class, () -> {
            final ResponseEntity<TrainerDto> response =  REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.POST, request, TrainerDto.class);
            assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        });
    }


     /**
     * Assure Saved Trainer Can Be Delivered By Backend
     */

    @Test
    public void getExistentTrainer_shouldReturnThisDto() {
        // post trainer
        TrainerDto trainer = fakeData.fakeNewTrainerDto();
        HttpEntity<TrainerDto> saveRequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> saveResponse = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, saveRequest, TrainerDto.class);
        TrainerDto postedAndSaved = saveResponse.getBody();


        // find previously posted trainer
        ResponseEntity<TrainerDto> getResponse = REST_TEMPLATE.getForEntity(URL.BASE + port + URL.TRAINER + "/" + postedAndSaved.getId(), TrainerDto.class);
        TrainerDto result = getResponse.getBody();
        assertThat(result.getId(), equalTo(postedAndSaved.getId()));
    }

    @Test
    public void getNonExistentTrainer_responseStatus404expected() {
        // post trainer
        TrainerDto trainer = fakeData.fakeNewTrainerDto();
        HttpEntity<TrainerDto> saveRequest = new HttpEntity<>(trainer);
        ResponseEntity<TrainerDto> saveResponse = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, saveRequest, TrainerDto.class);
        TrainerDto postedAndSaved = saveResponse.getBody();


        // now lookup for id which does not exist
        // exception must be thrown and status 404
        assertThrows(HttpClientErrorException.class, () -> {
            /***
             * only one entity in DB, most likely 1000 is not the one and only ID, if it were, we just pick the next.
             */
            int wrongID = 1000;
            if (wrongID == postedAndSaved.getId()) {
                wrongID++;
            }
            ResponseEntity<TrainerDto> response = REST_TEMPLATE.getForEntity(URL.BASE + port + URL.TRAINER + "/" + wrongID, TrainerDto.class);
            assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
        });
    }


    @Test
    public void getListOfTrainers_shouldContainAll() {
        // post list of 3 trainers
        List<TrainerDto> postedTrainers = new LinkedList<>();
        List<TrainerDto> savedTrainers = new LinkedList<>();
        TrainerDto trainerDto1 = fakeData.fakeNewTrainerDto();
        TrainerDto trainerDto2 = fakeData.fakeNewTrainerDto();
        TrainerDto trainerDto3 = fakeData.fakeNewTrainerDto();
        postedTrainers.add(trainerDto1);
        postedTrainers.add(trainerDto2);
        postedTrainers.add(trainerDto3);

        // save each of the replies from the post requests, i.e. this is the effective list of each saved trainer
        // list of trainers as how they are persisted (incl ID)
        for (int i = 0; i < postedTrainers.size(); i++) {
            HttpEntity<TrainerDto> saveRequest = new HttpEntity<>(postedTrainers.get(i));
            ResponseEntity<TrainerDto> saveResponse = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, saveRequest, TrainerDto.class);
            TrainerDto postedAndSaved = saveResponse.getBody();
            savedTrainers.add(postedAndSaved);
        }


        // now make request for EACH trainer
        ResponseEntity<List<TrainerDto>> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.GET, null, new ParameterizedTypeReference<List<TrainerDto>>(){});
        List<TrainerDto> results = response.getBody();

        // returned size must be equals to 3 === the number of previously posted trainers
        assertThat(results.size(), equalTo(savedTrainers.size()));
        // check that ids are are all equal
        for (int i = 0; i < results.size(); i++) {
            assertThat(results.get(i).getId(), equalTo(savedTrainers.get(i).getId()));
        }
    }


    

    @Test
    public void testUpdate_IdIsNotReplacedAndNewValuesAreSet() {
        TrainerDto postedOriginalTrainer = fakeData.fakeNewTrainerDto();
        HttpEntity<TrainerDto> saveRequestOriginal = new HttpEntity<>(postedOriginalTrainer);
        ResponseEntity<TrainerDto> saveResponseOriginal = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TRAINER, saveRequestOriginal, TrainerDto.class);
        TrainerDto  originalTrainer = saveResponseOriginal.getBody();


        TrainerDto postedUpdateTrainer = fakeData.fakeTrainerDto();
        // prepare for update operation
        postedUpdateTrainer.setId(originalTrainer.getId());


        HttpEntity<TrainerDto> request = new HttpEntity<>(postedUpdateTrainer);
        ResponseEntity<TrainerDto> responseAfterPatch = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER,HttpMethod.PUT, request, TrainerDto.class);
        TrainerDto response = responseAfterPatch.getBody();
        //REST_TEMPLATE.put(URL.BASE + port + URL.TRAINER, postedUpdateTrainer);

        // id is not reasigned but the same
        assertThat(response.getId(), equalTo(originalTrainer.getId()));
        // all other values are set as proposed by the update
        assertThat(response.getFirstName(), equalTo(postedUpdateTrainer.getFirstName()));
        assertThat(response.getLastName(), equalTo(postedUpdateTrainer.getLastName()));
        assertThat(response.getEmail(), equalTo(postedUpdateTrainer.getEmail()));
        assertThat(response.getPhone(), equalTo(postedUpdateTrainer.getPhone()));
        assertThat(response.getBirthday(), equalTo(postedUpdateTrainer.getBirthday()));
        assertThat(response.getUpdated().isAfter(response.getCreated()), equalTo(true));


        ResponseEntity<List<TrainerDto>> responseListOfAllTrainers = REST_TEMPLATE.exchange(URL.BASE + port + URL.TRAINER, HttpMethod.GET, null, new ParameterizedTypeReference<List<TrainerDto>>(){});
        List<TrainerDto> results = responseListOfAllTrainers.getBody();
        assertThat(results.size(), equalTo(1));
    }

}
