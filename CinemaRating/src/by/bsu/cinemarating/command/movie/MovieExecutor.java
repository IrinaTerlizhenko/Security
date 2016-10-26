package by.bsu.cinemarating.command.movie;

import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.*;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

public class MovieExecutor {
    private static Logger log = LogManager.getLogger(MovieExecutor.class);

    private static final String MOVIES = "movies";
    private static final String USER = "user";
    private static final String PAGE_TYPE = "page_type";
    private static final String ALL = "all";
    private static final String SIZE = "size";
    private static final String TOP = "top";
    private static final String MOVIE_ID = "movie_id";
    private static final String USER_ID = "user_id";
    private static final String MOVIE = "movie";
    private static final String REVIEWS = "reviews";
    private static final String REVIEWED = "reviewed";
    private static final String BANNED = "banned";
    private static final String RATING = "rating";

    public static String executeAllMovies(HttpServletRequest request) {
        String page;
        int userId = (int) request.getSession().getAttribute("user_id");
        try {
            List<Movie> movieList = MovieLogic.takeAllMovies(userId);
            Collections.reverse(movieList);
            request.setAttribute(MOVIES, movieList);
            request.getSession().setAttribute(PAGE_TYPE, ALL);
            page = ConfigurationManager.getProperty("path.page.movies");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }

    public static String executeTopMovies(HttpServletRequest request) {
        String page;
        int userId = (int) request.getSession().getAttribute("user_id");
        try {
            int size = Integer.parseInt((String) request.getSession().getAttribute(SIZE));
            List<Movie> movieList = MovieLogic.takeTopMovies(size, userId);
            request.setAttribute(MOVIES, movieList);
            request.getSession().setAttribute(PAGE_TYPE, TOP);
            page = ConfigurationManager.getProperty("path.page.movies");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }

    public static String executeShowMovie(HttpServletRequest request) {
        String page;
        try {
            int movieId = Integer.parseInt(request.getParameter(MOVIE_ID));
            Movie movie = MovieLogic.takeMovie(movieId);
            request.setAttribute(MOVIE, movie);
            int userId = (int) request.getSession().getAttribute(USER_ID);
            List<Review> reviews = MovieLogic.takeMovieReviews(movieId, userId);
            request.setAttribute(REVIEWS, reviews);
            boolean banned = BanLogic.isUserBanned(userId);
            request.setAttribute(BANNED, banned);
            boolean reviewed = ReviewLogic.isReviewed(movieId, userId);
            request.setAttribute(REVIEWED, reviewed);
            byte rating = RatingLogic.takeRating(userId, movieId);
            request.setAttribute(RATING, rating);
            User user = UserLogic.findUser(userId).get();
            request.setAttribute(USER, user);
            boolean canEdit = UserLogic.canEdit(userId, movieId);
            request.setAttribute("canEdit", canEdit);
            page = ConfigurationManager.getProperty("path.page.current_movie");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
