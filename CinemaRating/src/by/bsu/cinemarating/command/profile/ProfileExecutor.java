package by.bsu.cinemarating.command.profile;

import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.ReviewLogic;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

public class ProfileExecutor {
    private static Logger log = LogManager.getLogger(ProfileExecutor.class);

    private static final String ID = "id";
    private static final String USER_ID = "user_id";
    private static final String USER = "user";
    private static final String REVIEW_MAP = "review_map";

    public static String executeProfile(HttpServletRequest request) {
        String page;
        try {
            int id;
            String idString = request.getParameter(ID);
            if (idString != null) {
                id = Integer.parseInt(idString);
            } else {
                Object idObject = request.getAttribute(ID);
                id = (int) ((idObject != null) ? idObject : request.getSession().getAttribute(USER_ID));
            }
            User user = UserLogic.findUser(id).get();
            request.setAttribute(USER, user);
            page = ConfigurationManager.getProperty("path.page.profile");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }

    public static String executeInitEditProfile(HttpServletRequest request) {
        String page;
        try {
            int id = (int) request.getSession().getAttribute(USER_ID);
            User user = UserLogic.findUser(id).get();
            request.setAttribute(USER, user);
            page = ConfigurationManager.getProperty("path.page.edit_profile");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }

    public static String executeReviews(HttpServletRequest request) {
        String page;
        try {
            int userId = (int) request.getSession().getAttribute(USER_ID);
            LinkedHashMap<Movie, Review> reviewMap = ReviewLogic.takeUserReviews(userId);
            request.setAttribute(REVIEW_MAP, reviewMap);
            page = ConfigurationManager.getProperty("path.page.reviews");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
