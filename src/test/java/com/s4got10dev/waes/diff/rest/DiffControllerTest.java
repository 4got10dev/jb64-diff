package com.s4got10dev.waes.diff.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s4got10dev.waes.diff.model.AddDiffPartRequest;
import com.s4got10dev.waes.diff.model.AddDiffPartResponse;
import com.s4got10dev.waes.diff.model.DiffComparisonResult;
import com.s4got10dev.waes.diff.model.Offset;
import com.s4got10dev.waes.diff.model.Part;
import com.s4got10dev.waes.diff.model.Result;
import com.s4got10dev.waes.diff.storage.DiffStorage;

/**
 * @author Serhii Homeniuk
 */
@RunWith(SpringRunner.class)
@WebMvcTest(DiffController.class)
public class DiffControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DiffStorage storage;

    @Autowired
    private ObjectMapper objectMapper;

    private final Long ADD_ID = 0L;
    private AddDiffPartRequest ADD_REQUEST;
    private final String ADD_CONTENT = "dGVzdGRhdGE=";
    private final Long DIFFERENT_DIFF_ID = 1L;
    private final Long ERROR_DIFF_ID = 2L;

    @Before
    public void setUp() {
        ADD_REQUEST = AddDiffPartRequest.builder().data(ADD_CONTENT).build();
        //@formatter:off
        given(storage.addLeft(ADD_ID, ADD_REQUEST))
                .willReturn(AddDiffPartResponse.successful().id(ADD_ID).part(Part.LEFT).build());
        given(storage.addRight(ADD_ID, ADD_REQUEST))
                .willReturn(AddDiffPartResponse.successful().id(ADD_ID).part(Part.RIGHT).build());
        given(storage.getDiffResult(DIFFERENT_DIFF_ID))
                .willReturn(DiffComparisonResult.builder()
                        .result(Result.DIFFERENT)
                        .offsets(Arrays.asList(
                                Offset.builder().start(1).length(5).build(),
                                Offset.builder().start(10).length(7).build()))
                        .build());
        given(storage.getDiffResult(ERROR_DIFF_ID))
                .willReturn(DiffComparisonResult.builder()
                        .result(Result.NO_DATA_PROVIDED).build());
        //@formatter:on

    }

    @Test
    public void whenProperDiffRequested_thenReturnJsonResponseWithResult() throws Exception {
        mvc.perform(get("/v1/diff/" + DIFFERENT_DIFF_ID)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is(Result.DIFFERENT.getMessage())))
                .andExpect(jsonPath("$.offsets.*", hasSize(2)));
    }

    @Test
    public void whenMissingDiffRequested_thenReturnErrorMessage() throws Exception {
        mvc.perform(get("/v1/diff/" + ERROR_DIFF_ID)).andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is(Result.NO_DATA_PROVIDED.getMessage())))
                .andExpect(jsonPath("$.offsets.*").doesNotExist());
    }

    @Test
    public void whenLeftUpdated_HttpOkResponseReturned() throws Exception {
        mvc.perform(post("/v1/diff/" + ADD_ID + "/left")
                .content("{\"data\": \"" + ADD_CONTENT + "\"}")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.message").exists());

    }

    @Test
    public void whenRightAdded_HttpOkResponseReturned() throws Exception {
        mvc.perform(post("/v1/diff/" + ADD_ID + "/right")
                        .content(objectMapper.writeValueAsString(ADD_REQUEST))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(0)))
                .andExpect(jsonPath("$.message").exists());

    }

    @Test
    public void whenAddingSideInvokedWithoutContent_HttpBadRequestResponseReturned() throws Exception {
        mvc.perform(post("/v1/diff/" + ADD_ID + "/left").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenAddingSideInvokedWithBadContent_HttpBadRequestResponseReturned() throws Exception {
        mvc.perform(post("/v1/diff/" + ADD_ID + "/right")
                .content("{\"somewrong\": \"datahere\"}")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
