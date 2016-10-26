package by.bsu.cinemarating.command;

import by.bsu.cinemarating.command.movie.MovieExecutor;
import by.bsu.cinemarating.command.profile.ProfileExecutor;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.ReviewLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteReviewCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(DeleteReviewCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int movieId = Integer.parseInt(request.getParameter(MOVIE_ID));
            int userId = Integer.parseInt(request.getParameter(USER_ID));
            ReviewLogic.deleteReview(movieId, userId);
            request.setAttribute(MOVIE_ID, movieId);
            String pageAttribute = (String) request.getSession().getAttribute(PAGE);
            switch (pageAttribute) {
                case "path.page.current_movie":
                    MovieExecutor.executeShowMovie(request);
                    break;
                case "path.page.reviews":
                    ProfileExecutor.executeReviews(request);
                    break;
                default:
                    pageAttribute = "path.page.error";
                    break;
            }
            page = ConfigurationManager.getProperty(pageAttribute);
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
