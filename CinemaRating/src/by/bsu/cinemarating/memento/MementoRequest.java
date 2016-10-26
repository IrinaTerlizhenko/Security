package by.bsu.cinemarating.memento;

import java.util.Map;

/**
 * Created by User on 06.06.2016.
 */
public class MementoRequest {
    private Map<String, Object> attributes;

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
