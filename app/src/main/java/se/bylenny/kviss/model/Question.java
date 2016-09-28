package se.bylenny.kviss.model;

import lombok.Data;

@Data
public class Question extends Entity {
    private String correctAnswerId;
    private Entity[] options;
}
