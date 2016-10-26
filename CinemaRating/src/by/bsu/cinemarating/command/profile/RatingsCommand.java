package by.bsu.cinemarating.command.profile;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.RatingLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;

public class RatingsCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(RatingsCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int userId = (int) request.getSession().getAttribute(USER_ID);
            LinkedHashMap<Movie, Byte> ratingMap = RatingLogic.takeUserRatings(userId);
            request.setAttribute(RATING_MAP, ratingMap);
            page = ConfigurationManager.getProperty("path.page.ratings");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
