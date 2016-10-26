package by.bsu.cinemarating.command;

import by.bsu.cinemarating.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeLanguageCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String language = request.getParameter(LANGUAGE);
        request.getSession().setAttribute(LOCALE, language);
        String page = ConfigurationManager.getProperty((String) request.getSession().getAttribute(PAGE));
        return page;
    }
}
