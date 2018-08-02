package com.s4got10dev.waes.diff.model;

import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Serhii Homeniuk
 */
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class AddDiffPartResponse {

    private Long id;
    private String message;

    @Builder(builderMethodName = "successful")
    private AddDiffPartResponse(Long id, Part part) {
        this.id = id;
        this.message = format("%s part of record with id %d was successfully saved", part.getMessage(), id);
    }


}
