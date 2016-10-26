package by.bsu.cinemarating.dao;

import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Rating;
import by.bsu.cinemarating.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 24.04.16
 * Time: 11:08
 * To change this template use File | Settings | File Templates.
 */
public class RatingDAO extends AbstractRatingDAO<Rating> {
    private static Logger log = LogManager.getLogger(RatingDAO.class);

    private static final String SELECT_BY_ID = "SELECT rating FROM ratings WHERE mid=? AND uid=?";
    private static final String SELECT_RATINGS = "SELECT uid,rating FROM ratings WHERE mid=?";
    private static final String SELECT_USER_RATINGS_WITH_MOVIES =
            "SELECT ratings.rating AS rating,movie_id,name,description,year,country,movies.rating AS movie_rating,ref FROM ratings,movies WHERE uid=? AND mid=movie_id ORDER BY ratings.rating DESC";

    private static final String INSERT_RATING = "INSERT INTO ratings(mid,uid,rating) VALUES(?,?,?)";

    private static final String DELETE_BY_ID = "DELETE FROM ratings WHERE mid=? AND uid=?";

    private static final String REPLACE_RATING = "REPLACE INTO ratings(uid,mid,rating) VALUES(?,?,?)";

    public RatingDAO(WrapperConnection connection) {
        super(connection);
    }

    @Override
    public boolean create(Rating entity) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(INSERT_RATING)) {
            st.setInt(1, entity.getMovieId());
            st.setInt(2, entity.getUserId());
            st.setByte(3, entity.getRating());
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("Rating " + entity + " created");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    @Override
    public boolean delete(int movieId, int userId) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(DELETE_BY_ID)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("Rating [userId = " + userId + "; movieId = " + movieId + "] deleted");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    @Override
    public Optional<Rating> findEntityById(int movieId, int userId) throws DAOException {
        Rating rating = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_BY_ID)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                byte ratingValue = rs.getByte(RATING);
                rating = new Rating(movieId, userId, ratingValue);
                log.info("Rating [userId = " + userId + "; movieId = " + movieId + "] found");
            } else {
                log.info("Rating [userId = " + userId + "; movieId = " + movieId + "] not found");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(rating);
    }

    @Override
    public boolean replace(Rating entity) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(REPLACE_RATING)) {
            st.setInt(1, entity.getUserId());
            st.setInt(2, entity.getMovieId());
            st.setDouble(3, entity.getRating());
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("Rating " + entity + " replaced");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    /**
     * Selects a list of all ratings to the movie except the one given by the user with a specific id.
     *
     * @param userId  id of the user whose rating doesn't need to be counted
     * @param movieId id of the movie
     * @return a list of ratings to the movie
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public ArrayList<Byte> selectRatingsExceptUserId(int userId, int movieId) throws DAOException {
        ArrayList<Byte> ratings = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_RATINGS)) {
            st.setInt(1, movieId);
            ResultSet rs = st.executeQuery();
            int uid;
            byte rating;
            while (rs.next()) {
                uid = rs.getInt(UID);
                if (uid != userId) {
                    rating = rs.getByte(RATING);
                    ratings.add(rating);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return ratings;
    }

    /**
     * Selects all movies, rated by the user with a specific id along with the rating to the movie.
     *
     * @param userId id of the user
     * @return map of all movies and their rating from the user
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public LinkedHashMap<Movie, Byte> takeUserRatings(int userId) throws DAOException {
        LinkedHashMap<Movie, Byte> ratingMap = new LinkedHashMap<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_USER_RATINGS_WITH_MOVIES)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                byte rating = rs.getByte(RATING);
                int movieId = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double movieRating = rs.getDouble(MOVIE_RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(movieId, name, description, year, country, movieRating, ref);
                ratingMap.put(movie, rating);
            }
            log.info("Ratings of user [id = " + userId + "] selected");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return ratingMap;
    }
}
