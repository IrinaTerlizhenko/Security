package by.bsu.cinemarating.servlet;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.command.ChangeLanguageCommand;
import by.bsu.cinemarating.command.factory.ActionFactory;
import by.bsu.cinemarating.command.movie.AddMovieCommand;
import by.bsu.cinemarating.command.session.RegisterCommand;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.memento.Caretaker;
import by.bsu.cinemarating.memento.MementoRequest;
import by.bsu.cinemarating.resource.ConfigurationManager;
import by.bsu.cinemarating.resource.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by User on 16.10.2016.
 */
@WebServlet(name="loginController", value = "/loginController")
public class LoginController extends HttpServlet {
    private static Logger log = LogManager.getLogger(Controller.class);

    private static final String FORM = "form";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(request);

        /*FormType formType = FormType.valueOf(((String) request.getSession().getAttribute(FORM)).toUpperCase());

        if ((FormType.REGISTER.equals(formType) && !(command instanceof RegisterCommand)) ||
                (FormType.ADD_MOVIE.equals(formType) && !(command instanceof AddMovieCommand))) {
            request.getSession().setAttribute(FORM, FormType.NONE.name().toLowerCase());
        }*/

        String page = command.execute(request, response);
        if (page != null) {
            /*MementoRequest memento = (MementoRequest) request.getSession().getAttribute("memento");
            if (memento == null) {
                memento = new MementoRequest();
            }
            Caretaker caretaker = new Caretaker(memento);
            if (command.getClass() != ChangeLanguageCommand.class) {
                caretaker.extract(request);
                request.getSession().setAttribute("memento", memento);
            } else {
                caretaker.fill(request);
            }*/

            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } else {
            log.error("Page is null.");
            page = ConfigurationManager.getProperty("path.page.index");
            request.getSession().setAttribute("nullPage", MessageManager.getProperty("message.nullpage"));
            response.sendRedirect(request.getContextPath() + page);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        ConnectionPool.getInstance().closePool();
    }
}