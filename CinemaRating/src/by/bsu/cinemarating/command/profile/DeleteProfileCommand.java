package by.bsu.cinemarating.command.profile;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteProfileCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(DeleteProfileCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int userId = (int) request.getSession().getAttribute(USER_ID);
            boolean deleted = UserLogic.deleteUser(userId);
            if (!deleted) {
                throw new LogicException("Can't delete user; id=" + userId);
            }
            request.setAttribute(DELETED, PROFILE);
            page = ConfigurationManager.getProperty("path.page.deleted");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
