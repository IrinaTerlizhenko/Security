package by.bsu.cinemarating.command.movie;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.MovieLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

public class EditMovieCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(InitEditMovieCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int movieId = Integer.parseInt(request.getParameter(MOVIE_ID));
            String movieName = request.getParameter(MOVIE_NAME);
            String description = request.getParameter(DESCRIPTION);
            int year = Integer.parseInt(request.getParameter(YEAR));
            String country = request.getParameter(COUNTRY);
            Part filePart = request.getPart(PICTURE);
            String path = request.getServletContext().getRealPath(request.getServletPath());
            Movie movie = MovieLogic.editMovie(movieId, movieName, description, year, country, filePart, path);
            request.setAttribute(MOVIE, movie);
            page = ConfigurationManager.getProperty("path.page.current_movie");
        } catch (IOException | ServletException | LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
