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
import by.bsu.cinemarating.logic.encrypt.MD5;
import by.bsu.cinemarating.resource.ConfigurationManager;
import by.bsu.cinemarating.resource.MessageManager;
import by.bsu.cinemarating.servlet.FormType;
import by.bsu.cinemarating.validation.ValidationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class RegisterCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(RegisterCommand.class);

    private static final String REGISTER = "register";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        FormType formType = FormType.valueOf(((String) request.getSession().getAttribute(FORM)).toUpperCase());
        if (!FormType.REGISTER.equals(formType)) {
            String login = request.getParameter(LOGIN);
            String password = request.getParameter(PASSWORD);
            String repeatPassword = request.getParameter(REPEAT_PASSWORD);
            String email = request.getParameter(EMAIL);
            String pin = request.getParameter("pin");
            User user = new User();
            try {
                ValidationResult result = UserLogic.register(login, password, repeatPassword, pin, email, user);
                switch (result) {
                    case ALL_RIGHT:
                        request.getSession().setAttribute(USER_ID, user.getId());
                        request.getSession().setAttribute(ROLE, user.getRoleID());
                        request.getSession().setAttribute(FORM, REGISTER);
                        List<Movie> list = MovieLogic.takeLatestAddedMovies(SIZE_MOVIE);
                        request.setAttribute(LATEST_MOVIES, list);
                        list = MovieLogic.takeTopMovies(SIZE_MOVIE, user.getId());
                        request.setAttribute(TOP_MOVIES, list);
                        List<String> movieNames = new ArrayList<>();
                        List<Review> reviewList = ReviewLogic.takeLatestReviews(SIZE_REVIEW, movieNames);
                        request.setAttribute(REVIEWS, reviewList);
                        request.setAttribute(MOVIES, movieNames);
                        Cookie cookie = new Cookie("pin", MD5.encrypt(pin));
                        response.addCookie(cookie);
                        String token = TokenLogic.generateToken(user.getId());
                        request.getSession().setAttribute("token", token);
                        page = ConfigurationManager.getProperty("path.page.main");
                        break;
                    case LOGIN_PASS_INCORRECT:
                        request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.loginerror"));
                        break;
                    case EMAIL_INCORRECT:
                        request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.emailerror"));
                        break;
                    case PASS_NOT_MATCH:
                        request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.passnotmatch"));
                        break;
                    case LOGIN_NOT_UNIQUE:
                        request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.loginduplicate"));
                        break;
                    case EMAIL_NOT_UNIQUE:
                        request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.emailduplicate"));
                        break;
                    case UNKNOWN_ERROR:
                        request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.unknown"));
                        break;
                }
                if (result != ValidationResult.ALL_RIGHT) {
                    request.setAttribute(LOGIN, login);
                    request.setAttribute(PASSWORD, password);
                    request.setAttribute(REPEAT_PASSWORD, repeatPassword);
                    request.setAttribute(EMAIL, email);
                    page = ConfigurationManager.getProperty("path.page.register");
                }
            } catch (LogicException e) {
                log.error(e);
                page = ConfigurationManager.getProperty("path.page.error");
            }
        } else {
            page = ConfigurationManager.getProperty("path.page.main");
        }
        return page;
    }
}
