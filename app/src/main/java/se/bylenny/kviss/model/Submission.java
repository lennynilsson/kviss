package se.bylenny.kviss.model;

import java.io.Serializable;
import java.util.HashMap;

public class Submission implements Serializable {
    // NOTE: We need it serializable
    private HashMap<String, String> answers = new HashMap<>();

    public String getAnswer(String question) {
        return answers.get(question);
    }

    public String setAnswer(String question, String answer) {
        return answers.put(question, answer);
    }
}
