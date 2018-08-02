package com.s4got10dev.waes.diff;

import static com.s4got10dev.waes.diff.model.Part.LEFT;
import static com.s4got10dev.waes.diff.model.Part.RIGHT;
import static com.s4got10dev.waes.diff.model.Result.DIFFERENT;
import static com.s4got10dev.waes.diff.model.Result.EQUAL;
import static com.s4got10dev.waes.diff.model.Result.LEFT_SIDE_NOT_PROVIDED;
import static com.s4got10dev.waes.diff.model.Result.NOT_EQUAL_SIZE;
import static com.s4got10dev.waes.diff.model.Result.NO_DATA_PROVIDED;
import static com.s4got10dev.waes.diff.model.Result.RIGHT_SIDE_NOT_PROVIDED;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.s4got10dev.waes.diff.model.AddDiffPartRequest;
import com.s4got10dev.waes.diff.model.AddDiffPartResponse;
import com.s4got10dev.waes.diff.model.DiffComparisonResult;
import com.s4got10dev.waes.diff.model.Offset;
import com.s4got10dev.waes.diff.model.Part;
import com.s4got10dev.waes.diff.model.Result;

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
        doGetDiffResults(1L, NOT_FOUND, NO_DATA_PROVIDED);
    }

    @Test
    public void whenOnlyLeftSideAdded_thenReturnRightSideNotProvidedResponse() {
        doPostAddPart(2L, LEFT);
        doGetDiffResults(2L, NOT_FOUND, RIGHT_SIDE_NOT_PROVIDED);
    }

    @Test
    public void whenOnlyRightSideAdded_thenReturnLeftSideNotProvidedResponse() {
        doPostAddPart(3L, RIGHT);
        doGetDiffResults(3L, NOT_FOUND, LEFT_SIDE_NOT_PROVIDED);
    }

    @Test
    public void whenAddSideAddedTwoTimes_thenReturnOKResponse() {
        doPostAddPart(4L, RIGHT);
        doPostAddPart(4L, RIGHT);
    }

    @Test
    public void testTwoSidesEqualData() {
        doPostAddPart(5L, LEFT);
        doPostAddPart(5L, RIGHT);
        doGetDiffResults(5L, OK, EQUAL);
    }

    @Test
    public void testTwoSidesDifferentSizeData() {
        doPostAddPart(6L, LEFT, testData);
        doPostAddPart(6L, RIGHT, testDeita);
        doGetDiffResults(6L, OK, NOT_EQUAL_SIZE);
    }

    @Test
    public void testTwoSidesEqualSizeButDifferentData() {
        doPostAddPart(7L, LEFT, testData);
        doPostAddPart(7L, RIGHT, tootDooo);
        doGetDiffResults(7L, OK, DIFFERENT,
                Offset.builder().start(1).length(2).build(),
                Offset.builder().start(5).length(3).build());
    }



}
