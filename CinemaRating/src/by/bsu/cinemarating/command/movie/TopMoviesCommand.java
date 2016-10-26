package by.bsu.cinemarating.command.movie;

import by.bsu.cinemarating.command.ActionCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TopMoviesCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute(NUM_PAGE, 0);
        return MovieExecutor.executeTopMovies(request);
    }
}
