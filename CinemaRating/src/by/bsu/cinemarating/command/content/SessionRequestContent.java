package by.bsu.cinemarating.command.content;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SessionRequestContent {
    private HashMap<String, Object> requestAttributes;
    private HashMap<String, String[]> requestParameters;
    private HashMap<String, Object> sessionAttributes;

    public SessionRequestContent() {
        requestAttributes = new HashMap<>();
        requestParameters = new HashMap<>();
        sessionAttributes = new HashMap<>();
    }

    public SessionRequestContent(HttpServletRequest request) {
        this();
        extractValues(request);
    }

    public void extractValues(HttpServletRequest request) {
        requestParameters.putAll(request.getParameterMap());
        Enumeration<String> names = request.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            requestAttributes.put(name, request.getAttribute(name));
        }
        names = request.getSession().getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            sessionAttributes.put(name, request.getAttribute(name));
        }
    }

    public void insertValues(HttpServletRequest request) {
        for (Map.Entry<String, Object> entry : requestAttributes.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String, Object> entry : sessionAttributes.entrySet()) {
            request.getSession().setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public Object takeRequestAttribute(String name) {
        return requestAttributes.get(name);
    }

    public void addRequestAttribute(String name, Object attribute) {
        requestAttributes.put(name, attribute);
    }

    public String[] takeRequestParameter(String name) {
        return requestParameters.get(name);
    }

    public Object takeSessionAttribute(String name) {
        return sessionAttributes.get(name);
    }

    public void addSessionAttribute(String name, Object attribute) {
        sessionAttributes.put(name, attribute);
    }
}
