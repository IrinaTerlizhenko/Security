package by.bsu.cinemarating.command.factory;

import by.bsu.cinemarating.command.ActionCommand;
import by.bsu.cinemarating.command.EmptyCommand;
import by.bsu.cinemarating.command.client.CommandEnum;
import by.bsu.cinemarating.resource.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {
    /**
     * @param request request from client side
     * @return ActionCommand defined in request to be executed, otherwise EmptyCommand
     */
    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = new EmptyCommand();
        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return current;
        }
        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
            current = currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            request.setAttribute("wrongAction", action
                    + MessageManager.getProperty("message.wrongaction"));
        }
        return current;
    }
}
