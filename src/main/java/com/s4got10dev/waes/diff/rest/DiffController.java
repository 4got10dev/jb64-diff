package com.s4got10dev.waes.diff.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.s4got10dev.waes.diff.storage.DiffStorage;
import com.s4got10dev.waes.diff.model.AddDiffPartRequest;
import com.s4got10dev.waes.diff.model.AddDiffPartResponse;
import com.s4got10dev.waes.diff.model.DiffComparisonResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Serhii Homeniuk
 */
@RestController
@RequestMapping("v1/diff")
@Api(description = "Set of endpoints for adding left and right JSON base64 encoded binary data and retrieving results of comparison.")
public class DiffController {

    private final DiffStorage diffStorage;

    public DiffController(DiffStorage diffStorage) {
        this.diffStorage = diffStorage;
    }

    @ApiOperation("Returns a diff results for previously set data")
    @ApiResponses({ @ApiResponse(code = 200, message = "Diff result successfully returned"),
            @ApiResponse(code = 404, message = "Diff result cannot be provided") })
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DiffComparisonResult> getDiffResult(@PathVariable Long id) {
        DiffComparisonResult result = diffStorage.getDiffResult(id);
        switch (result.getResult()) {
            case LEFT_SIDE_NOT_PROVIDED:
            case RIGHT_SIDE_NOT_PROVIDED:
            case NO_DATA_PROVIDED:
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            default:
                return ResponseEntity.ok(result);
        }
    }

    @ApiOperation("Accept left part of JSON base64 encoded binary data")
    @ApiResponses({ @ApiResponse(code = 200, message = "Left part successfully updated"),
                @ApiResponse(code = 400, message = "Bad request content") })
    @PostMapping(value = "{id}/left", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddDiffPartResponse> addLeft(@PathVariable Long id, @Valid @RequestBody AddDiffPartRequest request) {
        return ResponseEntity.ok(diffStorage.addLeft(id, request));
    }


    @ApiOperation("Accept right part of JSON base64 encoded binary data")
    @ApiResponses({ @ApiResponse(code = 200, message = "Right part successfully updated"),
                @ApiResponse(code = 400, message = "Bad request content") })
    @PostMapping(value = "{id}/right", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddDiffPartResponse> addRight(@PathVariable Long id, @Valid @RequestBody AddDiffPartRequest request) {
        return ResponseEntity.ok(diffStorage.addRight(id, request));
    }

}
