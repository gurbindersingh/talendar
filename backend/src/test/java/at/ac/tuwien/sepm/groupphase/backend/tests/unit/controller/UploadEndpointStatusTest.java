package at.ac.tuwien.sepm.groupphase.backend.tests.unit.controller;

import at.ac.tuwien.sepm.groupphase.backend.service.IImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.tests.configuration.URL;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * NOTE:    pure unit test!
 *          anything but the controller layer is mocked away!
 *
 *          only tested that response status is correct
 *          -> i.e. that each error from the service layer is mapped appropriately
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UploadEndpointStatusTest {
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    @LocalServerPort
    private int port = 8080;
    @MockBean
    private IImageService imageService;


    /**
     * Configuration
     */
    private static HttpHeaders headers;
    private static MultiValueMap<String, Object> body;
    private static HttpEntity<MultiValueMap<String, Object>> requestEntity;

    /**
     * TEST VALUES
     */
    private static final String DUMMY_FILENAME_ON_SUCCESS = "default.jpg";
    private static final String DUMMY_UPLOAD_CONTENT = "thisMayBeARealContent";
    private static final String DUMMY_UPLOAD_NAME = "file";
    // not used because using it causes a jackson exception when controller received request
    // but it wa meant as dummy whatsoever (just a placeholder for method call)
    // if smn finds out how to pass it, nice, else nvm
    private static final MockMultipartFile DUMMY_UPLOAD =
        new MockMultipartFile(DUMMY_UPLOAD_NAME, DUMMY_UPLOAD_CONTENT.getBytes());

    private final ServiceException SERVICE_EXCEPTION = new ServiceException("Test", new Exception());

    @BeforeAll
    public static void init() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        body = new LinkedMultiValueMap();
        // see above: excluded but does not affect objectives of this test
        // body.add("file", DUMMY_UPLOAD);
        requestEntity = new HttpEntity<>(body, headers);
    }

    @Test
    public void uploadSucceeds_statusIs201() throws Exception {
        when(imageService.save(Mockito.any())).thenReturn(DUMMY_FILENAME_ON_SUCCESS);

        ResponseEntity<String> response = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.REQUEST_PROFILE_PIC, requestEntity, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }

    @Test
    public void uploadFailure_internalServerError_statusIs500() throws Exception {
        when(imageService.save(Mockito.any())).thenThrow(SERVICE_EXCEPTION);

        assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> response = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.REQUEST_PROFILE_PIC, requestEntity, String.class);
            assertThat(response.getStatusCode(), equalTo(500));
        });
    }

    @Test
    public void uploadFailure_fileNotValid_statusIs400() throws Exception {
        when(imageService.save(Mockito.any())).thenThrow(SERVICE_EXCEPTION);

        assertThrows(HttpClientErrorException.class, () -> {
            ResponseEntity<String> response = REST_TEMPLATE.postForEntity(URL.BASE + port + URL.REQUEST_PROFILE_PIC, requestEntity, String.class);
            assertThat(response.getStatusCode(), equalTo(400));
        });
    }
}
