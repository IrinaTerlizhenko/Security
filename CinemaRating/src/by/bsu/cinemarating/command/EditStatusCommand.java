package by.bsu.cinemarating.command;

import by.bsu.cinemarating.command.profile.ProfileExecutor;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditStatusCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(EditStatusCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int userId = Integer.parseInt(request.getParameter(USER_ID));
            double status = Double.parseDouble(request.getParameter(STATUS));
            UserLogic.updateStatus(userId, status);
            request.setAttribute(ID, userId);
            ProfileExecutor.executeProfile(request);
            page = ConfigurationManager.getProperty("path.page.profile");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
