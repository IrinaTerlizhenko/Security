package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.dao.MovieDAO;
import by.bsu.cinemarating.dao.UserDAO;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Entity;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.exception.LogicException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A service layer class implementing all the logic concerning search.
 */
public class SearchLogic {
    /**
     * Retrieves all entities in the system appropriate for specific query
     *
     * @param query string containing user's query
     * @return list of specific entities appropriate for query
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static List<List<Entity>> findFullCoincidence(String query) throws LogicException {
        List<List<Entity>> list = new ArrayList<>();
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            List<User> ulist = userDAO.findAll();
            list.add(ulist.stream().filter(u -> containsQuery(query, u)).collect(Collectors.toList()));
            MovieDAO movieDAO = new MovieDAO(connection);
            List<Movie> mlist = movieDAO.findAll();
            list.add(mlist.stream().filter(m -> containsQuery(query, m)).collect(Collectors.toList()));
        } catch (SQLException e) {
            throw new LogicException("DB connection error: ", e);
        } catch (DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return list;
    }

    /**
     * Checks the user for coincidence with the query
     *
     * @param query string containing user's query
     * @param user  user to check for coincidence
     * @return true if any of user's fields contains query, false otherwise
     */
    private static boolean containsQuery(String query, User user) {
        return user.getLogin().contains(query) ||
                user.getEmail().contains(query) ||
                (user.getName() != null && user.getName().contains(query)) ||
                (user.getSurname() != null && user.getSurname().contains(query));
    }

    /**
     * Checks the movie for coincidence with the query
     *
     * @param query string containing user's query
     * @param movie movie to check for coincidence
     * @return true if any of movie's fields contains query, false otherwise
     */
    private static boolean containsQuery(String query, Movie movie) {
        return movie.getName().contains(query) ||
                movie.getDescription().contains(query) ||
                movie.getCountry().contains(query);
    }
}
