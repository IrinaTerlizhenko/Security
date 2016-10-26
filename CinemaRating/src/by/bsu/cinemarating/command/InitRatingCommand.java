package by.bsu.cinemarating.command;

import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.BanLogic;
import by.bsu.cinemarating.logic.RatingLogic;
import by.bsu.cinemarating.logic.ReviewLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
public class InitRatingCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(InitRatingCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int movieId = Integer.parseInt(request.getParameter(MOVIE_ID));
            int userId = (int) request.getSession().getAttribute(USER_ID);
            byte rating = RatingLogic.takeRating(userId, movieId);
            boolean isBanned = BanLogic.isUserBanned(userId);
            request.setAttribute(BANNED, isBanned);
            request.setAttribute(MOVIE_ID, movieId);
            request.setAttribute(RATING, rating);
            ReviewLogic.takeReview(movieId, userId).ifPresent(r -> request.setAttribute(REVIEW, r));
            page = ConfigurationManager.getProperty("path.page.rating_review");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
