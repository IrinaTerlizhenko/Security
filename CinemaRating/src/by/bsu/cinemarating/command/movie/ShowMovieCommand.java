package by.bsu.cinemarating.command.movie;

import by.bsu.cinemarating.command.ActionCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowMovieCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return MovieExecutor.executeShowMovie(request);
    }
}
