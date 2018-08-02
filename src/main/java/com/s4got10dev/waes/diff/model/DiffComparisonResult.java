package com.s4got10dev.waes.diff.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

/**
 * @author Serhii Homeniuk
 */
@Getter
@Builder
@Setter(AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@ApiModel("Class representing result of left and right sides comparison")
@JsonPropertyOrder({ "result", "offsets" })
public class DiffComparisonResult {

    @ApiModelProperty(name = "result", notes = "message that represents result")
    protected Result result;

    @ApiModelProperty("if sides have same size but different content "
            + "provide insight in where the diffs are and length of them")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<Offset> offsets;

}
