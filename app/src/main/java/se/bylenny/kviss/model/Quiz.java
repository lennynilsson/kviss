package se.bylenny.kviss.model;

import lombok.Data;

@Data
public class Quiz extends Entity {
    long timeLimit;
    Question[] questions;
}
