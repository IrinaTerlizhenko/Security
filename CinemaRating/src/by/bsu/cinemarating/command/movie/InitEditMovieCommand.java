package by.bsu.cinemarating.command.movie;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.MovieLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitEditMovieCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(InitEditMovieCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int movieId = Integer.parseInt(request.getParameter(MOVIE_ID));
            Movie movie = MovieLogic.takeMovie(movieId);
            request.setAttribute(MOVIE, movie);
            page = ConfigurationManager.getProperty("path.page.edit_movie");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
