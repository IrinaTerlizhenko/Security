package by.bsu.cinemarating.command;

import by.bsu.cinemarating.command.movie.MovieExecutor;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.RatingLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

public class RateCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(RateCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int movieId = Integer.parseInt(request.getParameter(MOVIE_ID));
            int userId = (int) request.getSession().getAttribute(USER_ID);
            byte newRating = Byte.parseByte(request.getParameter(RATING));
            String newReview = request.getParameter(REVIEW);
            RatingLogic.addRating(userId, movieId, newRating, newReview);
            MovieExecutor.executeShowMovie(request);
            page = ConfigurationManager.getProperty("path.page.current_movie");
        } catch (SQLException | LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
