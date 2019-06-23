package at.ac.tuwien.sepm.groupphase.backend.tests.integration;

import at.ac.tuwien.sepm.groupphase.backend.rest.dto.TagDto;
import at.ac.tuwien.sepm.groupphase.backend.testDataCreation.FakeData;
import at.ac.tuwien.sepm.groupphase.backend.testObjects.TrainerDto;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.Tag;
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

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class TagEndpointTest {

    private static final TestRestTemplate REST_TEMPLATE = new TestRestTemplate();

    @LocalServerPort
    private int port = 8080;

    private FakeData fakeData = new FakeData();


    @Test
    public void postValidTag_shoudlSucceed_WithSetIdAndStatus201() {
        TagDto tag = fakeData.fakeRandomTagDto();

        HttpEntity<TagDto> request = new HttpEntity<>(tag);
        ResponseEntity<TagDto>
            response = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TAGS, request, TagDto.class);

        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    public void postInvalidTag_blank_ExceptionIsThrown() {
        TagDto tag = fakeData.fakeRandomTagDto();
        tag.setTag("");

        HttpEntity<TagDto> request = new HttpEntity<>(tag);
        assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<TagDto>
                response = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TAGS, request, TagDto.class);

            assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        });
    }

    @Test
    public void deleteTag_succeeds_noSuchTagFoundAndStatusIs200() {
        // prepare scenario and save a tag
        TagDto tagDto = fakeData.fakeRandomTagDto();

        HttpEntity<TagDto> request = new HttpEntity<>(tagDto);
        REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TAGS, request, TagDto.class);


        ResponseEntity<?> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.TAGS, HttpMethod.DELETE, request, Void.class);

        ResponseEntity<List<TagDto>> remainingTagsResponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.TAGS, HttpMethod.GET, null,   new ParameterizedTypeReference<List<TagDto>>() {});
        List<TagDto> remainingTags = remainingTagsResponse.getBody();

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        // check that removed entity is no longer existent
        assertThat(remainingTags, not(hasItem(tagDto)));
    }


    /**
     * Strange test, maybe consider changing the actual method (so that it throws something in this case)
     * ... but well, i guess a alayways fail safe method is also ok..
     */
    @Test
    public void deleteInexistentTag_shouldNotFail() {
        TagDto tagDto = fakeData.fakeRandomTagDto();
        HttpEntity<TagDto> request = new HttpEntity<>(tagDto);
        ResponseEntity<?> response = REST_TEMPLATE.exchange(URL.BASE + port + URL.TAGS, HttpMethod.DELETE, request, Void.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        // currently version of method just assumes that system does not break, so we enforce it
        // here too
    }

    @Test
    public void fetchAllTags_shouldContainAllAndStatusIs200() {
        TagDto tag1 = fakeData.fakeRandomTagDto();
        TagDto tag2 = fakeData.fakeRandomTagDto();
        TagDto tag3 = fakeData.fakeRandomTagDto();

        HttpEntity<TagDto> request1 = new HttpEntity<>(tag1);
        HttpEntity<TagDto> request2 = new HttpEntity<>(tag2);
        HttpEntity<TagDto> request3 = new HttpEntity<>(tag3);

        ResponseEntity<TagDto> response1 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TAGS, request1, TagDto.class);
        ResponseEntity<TagDto> response2 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TAGS, request2, TagDto.class);
        ResponseEntity<TagDto> response3 = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.TAGS, request3, TagDto.class);


        ResponseEntity<List<TagDto>> foundTagsResponse = REST_TEMPLATE.exchange(URL.BASE + port + URL.TAGS, HttpMethod.GET, null,   new ParameterizedTypeReference<List<TagDto>>() {});
        List<TagDto> foundTags = foundTagsResponse.getBody();

        /**
         * Check that in found list, each item (by ID) that was previously posted is included
         */
        assertThat(foundTags.size(), is(3));
        assertThat(foundTags.stream().filter((TagDto tag) -> tag.getId().equals( response1.getBody().getId())).collect(
            Collectors.toList()), not(empty()));
        assertThat(foundTags.stream().filter((TagDto tag) -> tag.getId().equals(  response2.getBody().getId())).collect(
            Collectors.toList()), not(empty()));
        assertThat(foundTags.stream().filter((TagDto tag) -> tag.getId().equals(  response3.getBody().getId())).collect(
            Collectors.toList()), not(empty()));

    }
}