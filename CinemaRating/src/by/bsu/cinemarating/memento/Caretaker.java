package by.bsu.cinemarating.memento;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 06.06.2016.
 */
public class Caretaker {
    private MementoRequest memento;

    public Caretaker(MementoRequest memento) {
        this.memento = memento;
    }

    public void extract(HttpServletRequest request) {
        Map<String, Object> attributes = new HashMap<>();
        Enumeration<String> attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String attr = attrs.nextElement();
            Object value = request.getAttribute(attr);
            attributes.put(attr, value);
        }
        memento.setAttributes(attributes);
    }

    public void fill(HttpServletRequest request) {
        for (Map.Entry<String, Object> entry : memento.getAttributes().entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
