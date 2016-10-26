package by.bsu.cinemarating.command.profile;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.UserLogic;
import by.bsu.cinemarating.resource.ConfigurationManager;
import by.bsu.cinemarating.validation.ValidationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

public class EditProfileCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(EditProfileCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        try {
            int id = (int) request.getSession().getAttribute(USER_ID);
            String name = request.getParameter(NAME);
            String surname = request.getParameter(SURNAME);
            String email = request.getParameter(EMAIL);
            Part filePart = request.getPart(PICTURE);
            String path = request.getServletContext().getRealPath(request.getServletPath());
            String previousPassword = request.getParameter(PREVIOUS_PASSWORD);
            String password = request.getParameter(PASSWORD);
            String repeatPassword = request.getParameter(REPEAT_PASSWORD);
            ValidationResult result;
            User user = UserLogic.findUser(id).get();
            if (previousPassword != null && !previousPassword.isEmpty()) {
                user = UserLogic.checkLogin(user.getLogin(), previousPassword);
            }
            if (user != null) {
                user.setName(name);
                user.setSurname(surname);
                user.setEmail(email);
                if (filePart != null && filePart.getSize() > 0) {
                    UserLogic.updatePhoto(user, filePart, path);
                }
                if (password == null || password.isEmpty()) {
                    result = UserLogic.editUser(user);
                } else if (UserLogic.checkRepeatPassword(password, repeatPassword)) {
                    user.setPassword(password);
                    result = UserLogic.editUserWithPassword(user);
                } else {
                    result = ValidationResult.PASS_NOT_MATCH;
                }
            } else {
                result = ValidationResult.PASS_NOT_MATCH;
            }
            ProfileExecutor.executeProfile(request);
            switch (result) {
                case ALL_RIGHT:
                    page = ConfigurationManager.getProperty("path.page.profile");
                    break;
                case NAME_INCORRECT:
                case SURNAME_INCORRECT:
                case EMAIL_INCORRECT:
                case PASS_NOT_MATCH:
                    String errorAttribute = result.name().toLowerCase();
                    page = ConfigurationManager.getProperty("path.page.edit_profile");
                    ProfileExecutor.executeInitEditProfile(request);
                    request.setAttribute(ERROR, errorAttribute);
                    break;
                case UNKNOWN_ERROR:
                    throw new LogicException("Error occurred while editing profile.");
                default:
                    throw new LogicException();
            }
        } catch (IOException | ServletException | LogicException e) {
            log.error(e);
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
