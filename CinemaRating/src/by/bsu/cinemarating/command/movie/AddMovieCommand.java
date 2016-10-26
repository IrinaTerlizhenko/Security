package by.bsu.cinemarating.command.movie;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.MovieLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import by.bsu.cinemarating.servlet.FormType;
import by.bsu.cinemarating.validation.ValidationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

public class AddMovieCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(AddMovieCommand.class);

    private static final String ADD_MOVIE = "add_movie";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            FormType formType = FormType.valueOf(((String) request.getSession().getAttribute(FORM)).toUpperCase());
            if (!FormType.ADD_MOVIE.equals(formType)) {
                String movieName = request.getParameter(MOVIE_NAME);
                String description = request.getParameter(DESCRIPTION);
                int year = Integer.parseInt(request.getParameter(YEAR));
                String country = request.getParameter(COUNTRY);
                Part filePart = request.getPart(PICTURE);
                int userId = (int) request.getSession().getAttribute("user_id");
                String path = request.getServletContext().getRealPath(request.getServletPath());
                ValidationResult result = MovieLogic.addMovie(movieName, description, year, country, filePart, path, userId);
                if (ValidationResult.ALL_RIGHT.equals(result)) {
                    request.getSession().setAttribute(PAGE_TYPE, ALL);
                    MovieExecutor.executeAllMovies(request);
                    request.getSession().setAttribute(FORM, ADD_MOVIE);
                    page = ConfigurationManager.getProperty("path.page.movies");
                } else {
                    page = ConfigurationManager.getProperty("path.page.add_movie");
                }
            } else {
                MovieExecutor.executeAllMovies(request);
                page = ConfigurationManager.getProperty("path.page.movies");
            }
        } catch (NumberFormatException | IOException | ServletException | LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
