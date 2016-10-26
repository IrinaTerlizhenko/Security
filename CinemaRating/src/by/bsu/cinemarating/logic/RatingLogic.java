package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.dao.MovieDAO;
import by.bsu.cinemarating.dao.RatingDAO;
import by.bsu.cinemarating.dao.ReviewDAO;
import by.bsu.cinemarating.dao.UserDAO;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Rating;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.exception.LogicException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * A service layer class implementing all the logic concerning ratings.
 */
public class RatingLogic {
    private static final int COUNT_TO_UPDATE_STATUS = 3;
    private static final double MAX_RATING = 10.0;

    /**
     * Retrieves rating from a specified user to a specified movie.
     *
     * @param userId  user id
     * @param movieId movie id
     * @return rating from a user with a specified id to a movie with a specified id
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static byte takeRating(int userId, int movieId) throws LogicException {
        byte rating = -1;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            rating = movieDAO.selectRating(movieId, userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return rating;
    }

    /**
     * Adds rating and review from a specified user to a specified movie.
     *
     * @param userId    user id
     * @param movieId   movie id
     * @param newRating rating from a user with a specified id to a movie with a specified id
     * @param newReview review from a user with a specified id to a movie with a specified id
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     * @throws SQLException   if any exceptions occurred while rolling back the transaction
     */
    public static void addRating(int userId, int movieId, byte newRating, String newReview) throws LogicException, SQLException {
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            RatingDAO ratingDAO = new RatingDAO(connection);
            ArrayList<Byte> ratings = ratingDAO.selectRatingsExceptUserId(userId, movieId);
            int count = ratings.size() + 1;
            double sum = ratings.stream().mapToDouble(e -> e).sum() + newRating;
            ReviewDAO reviewDAO = new ReviewDAO(connection);

            connection.setAutoCommit(false);

            if (newReview != null && !newReview.isEmpty()) {
                reviewDAO.replace(movieId, userId, newReview.trim());
            }
            double newMovieRating = sum / count;

            MovieDAO movieDAO = new MovieDAO(connection);

            // set users' statuses
            if (count >= COUNT_TO_UPDATE_STATUS) {
                UserDAO userDAO = new UserDAO(connection);
                ArrayList<Integer> uidRated = movieDAO.selectWhoRated(movieId);

                if (!userDAO.ratedMovie(movieId, userId)) {
                    User user = userDAO.findEntityById(userId).get();
                    int numRated = user.getNumRated();
                    userDAO.updateNumRated(userId, numRated + 1);
                    double status = user.getStatus();
                    status = MAX_RATING - ((MAX_RATING - status) * numRated * numRated + Math.abs(newRating - newMovieRating)) / (numRated + 1);
                    userDAO.updateStatus(userId, status);
                }

                double oldMovieRating = movieDAO.selectRating(movieId);
                for (Integer uid : uidRated) {
                    User user = userDAO.findEntityById(uid).get();
                    double status = user.getStatus();
                    int numRated = user.getNumRated();
                    if (count == COUNT_TO_UPDATE_STATUS) {
                        userDAO.updateNumRated(uid, ++numRated);
                    }
                    int ratingThisMovie = movieDAO.selectRating(movieId, uid);
                    if (count > COUNT_TO_UPDATE_STATUS) {
                        status += (Math.abs(ratingThisMovie - oldMovieRating) - Math.abs(ratingThisMovie - newMovieRating)) / numRated;
                    } else { // count == COUNT_TO_UPDATE_STATUS
                        status -= Math.abs(ratingThisMovie - newMovieRating) / numRated;
                    }
                    userDAO.updateStatus(uid, status);
                }
            }

            ratingDAO.replace(new Rating(movieId, userId, newRating));
            movieDAO.updateRating(movieId, newMovieRating);

            connection.commit();
        } catch (SQLException | DAOException e) {
            if (optConnection.isPresent()) {
                optConnection.get().rollback();
            }
            throw new LogicException(e);
        } finally {
            if (optConnection.isPresent()) {
                optConnection.get().setAutoCommit(true);
            }
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
    }

    /**
     * Retrieves all ratings of a specific user.
     *
     * @param userId id of the user whose ratings to retrieve
     * @return map with all movies, rated by the user, and their ratings
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static LinkedHashMap<Movie, Byte> takeUserRatings(int userId) throws LogicException {
        LinkedHashMap<Movie, Byte> ratingMap;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            RatingDAO ratingDAO = new RatingDAO(connection);
            ratingMap = ratingDAO.takeUserRatings(userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return ratingMap;
    }
}
