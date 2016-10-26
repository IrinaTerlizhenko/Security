package by.bsu.cinemarating.command;

import by.bsu.cinemarating.memento.Caretaker;
import by.bsu.cinemarating.memento.MementoRequest;
import by.bsu.cinemarating.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangePageCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int numPage = Integer.parseInt(request.getParameter(NUM_PAGE));
        request.getSession().setAttribute(NUM_PAGE, numPage);
        MementoRequest memento = (MementoRequest) request.getSession().getAttribute("memento");
        Caretaker caretaker = new Caretaker(memento);
        caretaker.fill(request);
        return ConfigurationManager.getProperty((String) request.getSession().getAttribute(PAGE));
    }
}
