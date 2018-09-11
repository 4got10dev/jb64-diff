package s4got10dev.diff;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import s4got10dev.diff.model.AddDiffPartRequest;
import s4got10dev.diff.model.AddDiffPartResponse;
import s4got10dev.diff.model.DiffComparisonResult;
import s4got10dev.diff.model.Offset;
import s4got10dev.diff.model.Part;
import s4got10dev.diff.model.Result;

/**
 * @author Serhii Homeniuk
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DiffApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class DiffIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private String testData;
    private String testDeita;
    private String tootDooo;

    @Before
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/v1/diff/";
        testData = Base64.getEncoder().encodeToString("testData".getBytes());
        testDeita = Base64.getEncoder().encodeToString("testDeita".getBytes());
        tootDooo = Base64.getEncoder().encodeToString("tootDooo".getBytes());
    }

    private void doPostAddPart(Long id, Part part) {
        doPostAddPart(id, part, testData);
    }

    private void doPostAddPart(Long id, Part part, String data) {
        ResponseEntity<AddDiffPartResponse> response =
                restTemplate.postForEntity(baseUrl + id + "/" + part.name().toLowerCase(),
                        AddDiffPartRequest.builder().data(data).build(), AddDiffPartResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(id);
        assertThat(response.getBody().getMessage()).contains(part.getMessage(), String.valueOf(id), "saved");
    }

    private void doGetDiffResults(Long id, HttpStatus expectedStatus, Result expectedResult, Offset... expectedOffsets) {
        ResponseEntity<DiffComparisonResult> response =
                restTemplate.getForEntity(baseUrl + id, DiffComparisonResult.class);

        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResult()).isEqualTo(expectedResult);
        if (expectedOffsets.length > 0) {
            assertThat(response.getBody().getOffsets())
                    .hasSameSizeAs(expectedOffsets)
                    .containsExactlyInAnyOrder(expectedOffsets);
        } else {
            assertNull(response.getBody().getOffsets());
        }
    }

    @Test
    public void whenNoSidesAdded_thenReturnNoDataProvidedResponse() {
        doGetDiffResults(1L, NOT_FOUND, Result.NO_DATA_PROVIDED);
    }

    @Test
    public void whenOnlyLeftSideAdded_thenReturnRightSideNotProvidedResponse() {
        doPostAddPart(2L, Part.LEFT);
        doGetDiffResults(2L, NOT_FOUND, Result.RIGHT_SIDE_NOT_PROVIDED);
    }

    @Test
    public void whenOnlyRightSideAdded_thenReturnLeftSideNotProvidedResponse() {
        doPostAddPart(3L, Part.RIGHT);
        doGetDiffResults(3L, NOT_FOUND, Result.LEFT_SIDE_NOT_PROVIDED);
    }

    @Test
    public void whenAddSideAddedTwoTimes_thenReturnOKResponse() {
        doPostAddPart(4L, Part.RIGHT);
        doPostAddPart(4L, Part.RIGHT);
    }

    @Test
    public void testTwoSidesEqualData() {
        doPostAddPart(5L, Part.LEFT);
        doPostAddPart(5L, Part.RIGHT);
        doGetDiffResults(5L, OK, Result.EQUAL);
    }

    @Test
    public void testTwoSidesDifferentSizeData() {
        doPostAddPart(6L, Part.LEFT, testData);
        doPostAddPart(6L, Part.RIGHT, testDeita);
        doGetDiffResults(6L, OK, Result.NOT_EQUAL_SIZE);
    }

    @Test
    public void testTwoSidesEqualSizeButDifferentData() {
        doPostAddPart(7L, Part.LEFT, testData);
        doPostAddPart(7L, Part.RIGHT, tootDooo);
        doGetDiffResults(7L, OK, Result.DIFFERENT,
                Offset.builder().start(1).length(2).build(),
                Offset.builder().start(5).length(3).build());
    }



}
