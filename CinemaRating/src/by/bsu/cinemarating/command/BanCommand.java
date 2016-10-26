package by.bsu.cinemarating.command;

import by.bsu.cinemarating.entity.BanType;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.format.TimeFormatter;
import by.bsu.cinemarating.logic.BanLogic;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

public class BanCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(BanCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        int id = Integer.parseInt(request.getParameter(ID));
        BanType type = BanType.valueOf(request.getParameter(TYPE));
        Timestamp expiration = TimeFormatter.format(request.getParameter(EXPIRATION));
        String reason = request.getParameter(REASON);
        try {
            BanLogic.addBan(id, type, reason, expiration);
            User user = UserLogic.findUser(id).get();
            request.setAttribute(USER, user);
            page = ConfigurationManager.getProperty("path.page.profile");
        } catch (LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }

        return page;
    }
}
