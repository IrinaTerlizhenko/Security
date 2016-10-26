package by.bsu.cinemarating.command;

import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.MovieLogic;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by User on 06.10.2016.
 */
public class ShareMoviesCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(ShareMoviesCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        int ownerId = (int)request.getSession().getAttribute("user_id");
        int sharedId = Integer.parseInt(request.getParameter("id"));
        try {
            List<Movie> movies = MovieLogic.takeOwnerMovies(ownerId);
            for (Movie movie : movies) {
                int movieId = movie.getId();
                String accessType = request.getParameter(String.valueOf(movieId));
                if (!"NONE".equals(accessType)) {
                    MovieLogic.share(ownerId, sharedId, movieId, accessType);
                }
            }
            User user = UserLogic.findUser(sharedId).get();
            request.setAttribute(USER, user);
            page = ConfigurationManager.getProperty("path.page.profile");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
