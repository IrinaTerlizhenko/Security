package by.bsu.cinemarating.command;

import by.bsu.cinemarating.entity.Entity;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.SearchLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SearchCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(SearchCommand.class);

    private static String QUERY = "query";
    private static String USER_RESULT = "user_result";
    private static String MOVIE_RESULT = "movie_result";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            String query = request.getParameter(QUERY);
            if (query != null && !query.isEmpty()) {
                List<List<Entity>> queryList = SearchLogic.findFullCoincidence(query);
                request.setAttribute(USER_RESULT, queryList.get(0));
                request.setAttribute(MOVIE_RESULT, queryList.get(1));
            }
            page = ConfigurationManager.getProperty("path.page.search");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
