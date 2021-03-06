package s4got10dev.diff.model;

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
