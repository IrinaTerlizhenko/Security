package by.bsu.cinemarating.command.session;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.MovieLogic;
import by.bsu.cinemarating.logic.ReviewLogic;
import by.bsu.cinemarating.logic.TokenLogic;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import by.bsu.cinemarating.resource.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class LoginCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String login = request.getParameter(LOGIN);
        String pass = request.getParameter(PASSWORD);
        String pin = request.getParameter("pin");
        try {
            User user = UserLogic.checkLogin(login, pass, pin);
            if (user != null) {
                request.getSession().setAttribute(USER_ID, user.getId());
                request.getSession().setAttribute(ROLE, user.getRoleID());
                List<Movie> list = MovieLogic.takeLatestAddedMovies(SIZE_MOVIE);
                request.setAttribute(LATEST_MOVIES, list);
                list = MovieLogic.takeTopMovies(SIZE_MOVIE, user.getId());
                request.setAttribute(TOP_MOVIES, list);
                List<String> movieNames = new ArrayList<>();
                List<Review> reviewList = ReviewLogic.takeLatestReviews(SIZE_REVIEW, movieNames);
                request.setAttribute(REVIEWS, reviewList);
                request.setAttribute(MOVIES, movieNames);
                String token = TokenLogic.generateToken(user.getId());
                request.getSession().setAttribute("token", token);
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                request.setAttribute(LOGIN, login);
                request.setAttribute(PASSWORD, pass);
                request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.loginerror"));
                page = ConfigurationManager.getProperty("path.page.login");
            }
        } catch (LogicException e) {
            log.error(e);
            request.getSession().setAttribute("customException", e.getMessage());
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
