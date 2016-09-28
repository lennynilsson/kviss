package se.bylenny.kviss.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Entity implements Serializable {
    String title;
    String image;
    String id;
}
