package com.s4got10dev.waes.diff.model;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;

/**
 * @author Serhii Homeniuk
 */
@AllArgsConstructor
public enum Result {
    EQUAL("Left and Right sides are equal"),
    NOT_EQUAL_SIZE("Left and Right sides has different size"),
    DIFFERENT("Left and Right sides different"),
    LEFT_SIDE_NOT_PROVIDED("Left side was not provided"),
    RIGHT_SIDE_NOT_PROVIDED("Right side was not provided"),
    NO_DATA_PROVIDED("Left and right sides were not provided");

    private final String message;

    @JsonValue
    public String getMessage() {
        return message;
    }

}
