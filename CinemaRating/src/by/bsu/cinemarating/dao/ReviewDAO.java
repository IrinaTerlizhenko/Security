package by.bsu.cinemarating.dao;

import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.format.FormattedTimestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.*;
import java.util.*;

/**
 * Created by User on 06.06.2016.
 */
public class ReviewDAO extends AbstractRatingDAO<Review> {
    private static Logger log = LogManager.getLogger(ReviewDAO.class);

    private static final String SELECT_BY_ID =
            "SELECT review,time,login,email,reg_date,role_id,name,surname,status,photo,num_rated FROM reviews,users WHERE mid=? AND uid=? AND user_id=uid";
    private static final String SELECT_TEXT_BY_ID = "SELECT review FROM reviews WHERE mid=? AND uid=?";
    private static final String SELECT_REVIEWS_BY_MID = "SELECT uid,review,time FROM reviews WHERE mid=? ORDER BY time DESC";
    private static final String SELECT_USER_REVIEWS_WITH_MOVIES =
            "SELECT review,time,movie_id,name,description,year,country,movies.rating AS movie_rating,ref FROM reviews,movies WHERE uid=? AND mid=movie_id ORDER BY time DESC";
    private static final String SELECT_LATEST =
            "SELECT review,time,user_id,login,email,reg_date,role_id,users.name AS name,surname,status,photo,num_rated,movie_id,movies.name AS movie_name FROM reviews,users,movies WHERE user_id=uid AND movie_id=mid ORDER BY time DESC LIMIT ?";

    private static final String INSERT_REVIEW = "INSERT INTO reviews(mid,uid,review) VALUES(?,?,?)";

    private static final String REPLACE_REVIEW = "REPLACE INTO reviews(mid,uid,review,time) VALUES(?,?,?,?)";

    private static final String DELETE_BY_ID = "DELETE FROM reviews WHERE mid=? AND uid=?";

    public ReviewDAO(WrapperConnection connection) {
        super(connection);
    }

    @Override
    public boolean delete(int movieId, int userId) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(DELETE_BY_ID)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("Review [userId = " + userId + "; movieId = " + movieId + "] deleted");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    @Override
    public boolean create(Review entity) throws DAOException {
        return create(entity.getMid(), entity.getUser().getId(), entity.getText());
    }

    /**
     * Creates a review in the system.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @param text    text of the review
     * @return true if the entity was created, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public boolean create(int movieId, int userId, String text) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(INSERT_REVIEW)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            st.setString(3, text);
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("Review [userId = " + userId + "; movieId = " + movieId + "; text = " + text + "] created");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    @Override
    public Optional<Review> findEntityById(int movieId, int userId) throws DAOException {
        Review review = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_BY_ID)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String text = rs.getString(REVIEW);
                Timestamp time = rs.getTimestamp(TIME);
                FormattedTimestamp timestamp = new FormattedTimestamp(time);
                String login = rs.getString(LOGIN);
                String email = rs.getString(EMAIL);
                Date reg_date = rs.getDate(REG_DATE);
                byte role_id = rs.getByte(ROLE_ID);
                String name = rs.getString(NAME);
                String surname = rs.getString(SURNAME);
                double status = rs.getDouble(STATUS);
                String photo = rs.getString(PHOTO);
                int numRated = rs.getInt(NUM_RATED);
                User user = new User(userId, login, email, reg_date, role_id, name, surname, status, photo, numRated);
                review = new Review(user, movieId, text, timestamp);
                log.info("Review [userId = " + userId + "; movieId = " + movieId + "] found");
            } else {
                log.info("Review [userId = " + userId + "; movieId = " + movieId + "] not found");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(review);
    }

    @Override
    public boolean replace(Review entity) throws DAOException {
        return replace(entity.getMid(), entity.getUser().getId(), entity.getText());
    }

    /**
     * Replaces a review with the new one.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @param text    new text of the review
     * @return true if the entity was replaced, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public boolean replace(int movieId, int userId, String text) throws DAOException {
        boolean result;
        Optional<Review> oldReview = findEntityById(movieId, userId);
        if (oldReview.isPresent()) {
            try (PreparedStatement st = connection.prepareStatement(REPLACE_REVIEW)) {
                st.setInt(1, movieId);
                st.setInt(2, userId);
                st.setString(3, text);
                st.setTimestamp(4, oldReview.get().getTime().getTimestamp());
                result = st.executeUpdate() > 0;
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        } else {
            result = create(movieId, userId, text);
        }
        if (result) {
            log.info("Review [userId = " + userId + "; movieId = " + movieId + "] replaced");
        }
        return result;
    }

    /**
     * Selects text of a specific review.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return text of the review wrapped in Optional if exists, Optional.empty() otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public Optional<String> takeText(int movieId, int userId) throws DAOException {
        String text = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_TEXT_BY_ID)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                text = rs.getString(REVIEW);
                log.info("Review [userId = " + userId + "; movieId = " + movieId + "] text selected");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(text);
    }

    /**
     * Selects a list of all reviews on the movie with a specified id. The review of the user with a specified id
     * comes first.
     *
     * @param movieId     id of the movie
     * @param userIdFirst id of the user, whose review should come first
     * @return a list of all reviews on the movie
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public List<Review> findReviews(int movieId, int userIdFirst) throws DAOException {
        List<Review> reviews = new LinkedList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_REVIEWS_BY_MID)) {
            st.setInt(1, movieId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt(UID);
                String text = rs.getString(REVIEW);
                Timestamp ts = rs.getTimestamp(TIME);
                FormattedTimestamp timestamp = new FormattedTimestamp(ts);
                UserDAO userDAO = new UserDAO(connection);
                User user = userDAO.findEntityById(userId).get();
                Review review = new Review(user, movieId, text, timestamp);
                if (userIdFirst != userId) {
                    reviews.add(review);
                } else {
                    ((LinkedList<Review>) reviews).addFirst(review);
                }
            }
            log.info("Movie [id = " + movieId + "] reviews selected");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return reviews;
    }

    /**
     * Checks if the user has left a review on the movie.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return true if the user has left a review on the movie, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public boolean existsReview(int movieId, int userId) throws DAOException {
        Optional<Review> optReview = findEntityById(movieId, userId);
        return optReview.isPresent();
    }

    /**
     * Selects all reviews by a specific user.
     *
     * @param user user, whose reviews to select
     * @return map with reviewed movies along with reviews on them from the user
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public LinkedHashMap<Movie, Review> takeUserReviews(User user) throws DAOException {
        LinkedHashMap<Movie, Review> reviewMap = new LinkedHashMap<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_USER_REVIEWS_WITH_MOVIES)) {
            st.setInt(1, user.getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String text = rs.getString(REVIEW);
                Timestamp time = rs.getTimestamp(TIME);
                FormattedTimestamp timestamp = new FormattedTimestamp(time);
                int movieId = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double movieRating = rs.getDouble(MOVIE_RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(movieId, name, description, year, country, movieRating, ref);
                Review review = new Review(user, movieId, text, timestamp);
                reviewMap.put(movie, review);
            }
            log.info("User [id = " + user.getId() + "] reviews selected");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return reviewMap;
    }

    /**
     * Selects a list of latest added reviews.
     *
     * @param size       maximum size of the return list
     * @param movieNames list of names of movies in the reviews. Should be empty on call
     * @return a list of latest added reviews
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public List<Review> takeLatestReviews(int size, List<String> movieNames) throws DAOException {
        List<Review> reviewList = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_LATEST)) {
            st.setInt(1, size);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String text = rs.getString(REVIEW);
                Timestamp time = rs.getTimestamp(TIME);
                FormattedTimestamp timestamp = new FormattedTimestamp(time);
                int userId = rs.getInt(USER_ID);
                String login = rs.getString(LOGIN);
                String email = rs.getString(EMAIL);
                Date reg_date = rs.getDate(REG_DATE);
                byte role_id = rs.getByte(ROLE_ID);
                String name = rs.getString(NAME);
                String surname = rs.getString(SURNAME);
                double status = rs.getDouble(STATUS);
                String photo = rs.getString(PHOTO);
                int numRated = rs.getInt(NUM_RATED);
                int movieId = rs.getInt(MOVIE_ID);
                String movieName = rs.getString(MOVIE_NAME);
                User user = new User(userId, login, email, reg_date, role_id, name, surname, status, photo, numRated);
                Review review = new Review(user, movieId, text, timestamp);
                reviewList.add(review);
                movieNames.add(movieName);
            }
            log.info("Latest " + size + " reviews selected");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return reviewList;
    }
}
