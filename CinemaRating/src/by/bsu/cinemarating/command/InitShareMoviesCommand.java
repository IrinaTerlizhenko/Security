package by.bsu.cinemarating.command;

import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.MovieLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 06.10.2016.
 */
public class InitShareMoviesCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(InitShareMoviesCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        int ownerId = (int)request.getSession().getAttribute("user_id");
        int sharedId = Integer.parseInt(request.getParameter("id"));
        try {
            List<Movie> movies = MovieLogic.takeOwnerMovies(ownerId);
            request.setAttribute("movies", movies);
            request.setAttribute("id", sharedId);
            page = ConfigurationManager.getProperty("path.page.share");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
