package by.bsu.cinemarating.command.profile;

import by.bsu.cinemarating.command.ActionCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitEditProfileCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ProfileExecutor.executeInitEditProfile(request);
    }
}
