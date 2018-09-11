package s4got10dev.diff.rest;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

import java.util.Arrays;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import s4got10dev.diff.model.DiffComparisonResult;
import s4got10dev.diff.model.Offset;
import s4got10dev.diff.model.Result;

/**
 * @author Serhii Homeniuk
 */
@RunWith(SpringRunner.class)
@JsonTest
public class DiffResultJsonSerializeTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenSidesEqual_thenReturnResultWithoutOffsets() throws JsonProcessingException, JSONException {
        DiffComparisonResult result = DiffComparisonResult.builder().result(Result.EQUAL).build();
        String json = objectMapper.writeValueAsString(result);
        assertEquals("{\"result\":\"Left and Right sides are equal\"}", json, false);
    }

    @Test
    public void whenSidesDifferent_thenReturnResultWithOffsets() throws JsonProcessingException, JSONException {
        //@formatter:off
        DiffComparisonResult user = DiffComparisonResult.builder()
                .result(Result.DIFFERENT)
                .offsets(Arrays.asList(
                        Offset.builder().start(1).length(5).build(),
                        Offset.builder().start(10).length(7).build()))
                .build();
        //@formatter:on
        String json = objectMapper.writeValueAsString(user);
        assertEquals("{\"result\":\"Left and Right sides different\","
                + "\"offsets\":[{\"start\":1,\"length\":5},{\"start\":10,\"length\":7}]}", json, false);
    }

}
