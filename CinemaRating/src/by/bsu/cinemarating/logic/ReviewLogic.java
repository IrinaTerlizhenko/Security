package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.dao.ReviewDAO;
import by.bsu.cinemarating.dao.UserDAO;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.exception.LogicException;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * A service layer class implementing all the logic concerning reviews.
 */
public class ReviewLogic {
    /**
     * Retrieves a review from a specified user to a specified movie.
     *
     * @param movieId movie id
     * @param userId  user id
     * @return review from a user with a specified id to a movie with a specified id wrapped in Optional if exists, Optional.empty() otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static Optional<String> takeReview(int movieId, int userId) throws LogicException {
        Optional<String> review = Optional.empty();
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            ReviewDAO reviewDAO = new ReviewDAO(connection);
            review = reviewDAO.takeText(movieId, userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return review;
    }

    /**
     * Deletes the review from a specified user to a specified movie.
     *
     * @param movieId movie id
     * @param userId  user id
     * @return true if the review was deleted, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean deleteReview(int movieId, int userId) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            ReviewDAO reviewDAO = new ReviewDAO(connection);
            result = reviewDAO.delete(movieId, userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    /**
     * Replaces review text with specific user id and movie id.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @param text    new review text
     * @return true if text was replaced, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean replaceReview(int movieId, int userId, String text) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            ReviewDAO reviewDAO = new ReviewDAO(connection);
            result = reviewDAO.replace(movieId, userId, text);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    /**
     * checks if the user with the given id reviewed the movie with the given id.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return true if the user reviewed the movie, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean isReviewed(int movieId, int userId) throws LogicException {
        boolean reviewed = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            ReviewDAO reviewDAO = new ReviewDAO(connection);
            reviewed = reviewDAO.existsReview(movieId, userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return reviewed;
    }

    /**
     * Retrieves all reviews from a specific user with reviewed movies.
     *
     * @param userId id of the user
     * @return map of movies reviewed by the user and reviews themselves
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static LinkedHashMap<Movie, Review> takeUserReviews(int userId) throws LogicException {
        LinkedHashMap<Movie, Review> reviewMap;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            ReviewDAO reviewDAO = new ReviewDAO(connection);
            UserDAO userDAO = new UserDAO(connection);
            User user = userDAO.findEntityById(userId).get();
            reviewMap = reviewDAO.takeUserReviews(user);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return reviewMap;
    }

    /**
     * Retrieves latest reviews with movie names.
     *
     * @param size       size of the return list
     * @param movieNames names of movies reviewed, should be empty on call
     * @return list of latest reviews
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static List<Review> takeLatestReviews(int size, List<String> movieNames) throws LogicException {
        List<Review> reviewList;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            ReviewDAO reviewDAO = new ReviewDAO(connection);
            reviewList = reviewDAO.takeLatestReviews(size, movieNames);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return reviewList;
    }
}
