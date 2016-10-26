package by.bsu.cinemarating.command;

import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.MovieLogic;
import by.bsu.cinemarating.logic.ReviewLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class RedirectCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(RedirectCommand.class);

    private static final String PAGE_MAIN = "path.page.main";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String nextPage = request.getParameter(NEXT_PAGE);
        try {
            if (PAGE_MAIN.equals(nextPage)) {
                int userId = (int) request.getSession().getAttribute("user_id");
                List<Movie> list = MovieLogic.takeLatestAddedMovies(SIZE_MOVIE);
                request.setAttribute(LATEST_MOVIES, list);
                list = MovieLogic.takeTopMovies(SIZE_MOVIE, userId);
                request.setAttribute(TOP_MOVIES, list);
                List<String> movieNames = new ArrayList<>();
                List<Review> reviewList = ReviewLogic.takeLatestReviews(SIZE_REVIEW, movieNames);
                request.setAttribute(REVIEWS, reviewList);
                request.setAttribute(MOVIES, movieNames);
            }
            page = ConfigurationManager.getProperty(nextPage);
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
