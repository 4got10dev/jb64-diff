package com.s4got10dev.waes.diff.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Serhii Homeniuk
 */
@Getter
@AllArgsConstructor
public enum Part {
    LEFT("Left"),
    RIGHT("Right");

    String message;
}
