package by.bsu.cinemarating.command;

import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

public class AllUsersCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(AllUsersCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            List<User> userList = UserLogic.takeAllUsers();
            Collections.reverse(userList);
            request.setAttribute(USERS, userList);
            request.getSession().setAttribute(NUM_PAGE, 0);
            page = ConfigurationManager.getProperty("path.page.users");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
