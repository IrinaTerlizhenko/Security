package by.bsu.cinemarating.command;

import by.bsu.cinemarating.command.movie.MovieExecutor;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.ReviewLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditReviewCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(EditReviewCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int userId = Integer.parseInt(request.getParameter(USER_ID));
            int movieId = Integer.parseInt(request.getParameter(MOVIE_ID));
            String text = request.getParameter(REVIEW);
            ReviewLogic.replaceReview(movieId, userId, text);
            MovieExecutor.executeShowMovie(request);
            page = ConfigurationManager.getProperty("path.page.current_movie");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
