package by.bsu.cinemarating.dao;

import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 14.04.16
 * Time: 2:08
 * To change this template use File | Settings | File Templates.
 */
public class MovieDAO extends AbstractDAO<Movie> {
    private static Logger log = LogManager.getLogger(MovieDAO.class);

    private static final String SELECT_ALL = "SELECT movie_id,name,description,year,country,rating,ref FROM movies";
    private static final String SELECT_BY_ID = "SELECT name,description,year,country,rating,ref FROM movies WHERE movie_id=?";
    private static final String SELECT_TOP = "SELECT movie_id,name,description,year,country,rating,ref FROM movies ORDER BY rating DESC LIMIT ?";
    private static final String SELECT_LATEST_ADDED = "SELECT movie_id,name,description,year,country,rating,ref FROM movies ORDER BY movie_id DESC LIMIT ?";
    private static final String SELECT_RATING_BY_MOVIE_ID = "SELECT rating FROM movies WHERE movie_id=?";
    private static final String SELECT_LAST_ID = "SELECT MAX(movie_id) AS id FROM movies";
    private static final String SELECT_RATING = "SELECT rating FROM ratings WHERE mid=? AND uid=?";
    private static final String SELECT_UIDS_BY_MID = "SELECT uid FROM ratings WHERE mid=?";

    private static final String INSERT_MOVIE = "INSERT INTO movies(name,description,year,country,ref,added_by) VALUES(?,?,?,?,?,?)";

    private static final String DELETE_BY_ID = "DELETE FROM movies WHERE movie_id=?";

    private static final String UPDATE_MOVIE = "UPDATE movies SET name=?,description=?,year=?,country=?,ref=? WHERE movie_id=?";
    private static final String UPDATE_RATING = "UPDATE movies SET rating=? WHERE movie_id=?";

    private static final String SHARE_MOVIE = "INSERT INTO shared(owner_id,shared_id,movie_id, access_type) VALUES(?,?,?,?)";
    private static final String SELECT_OWNER_MOVIES = "SELECT movie_id,name,description,year,country,rating,ref FROM movies WHERE added_by=?";
    private static final String SELECT_ALL_FOR_USER =
            "SELECT distinct movies.movie_id,name,description,year,country,rating,ref FROM movies JOIN shared WHERE added_by=? OR (movies.movie_id = shared.movie_id AND shared_id=? AND (access_type = 'READ' OR access_type = 'READ_WRITE'))";
    private static final String SELECT_TOP_FOR_USER =
            "SELECT distinct movies.movie_id,name,description,year,country,rating,ref FROM movies JOIN shared WHERE added_by=? OR (movies.movie_id = shared.movie_id AND shared_id=? AND (access_type = 'READ' OR access_type = 'READ_WRITE')) ORDER BY rating DESC LIMIT ?";

    public MovieDAO(WrapperConnection connection) {
        super(connection);
    }

    @Override
    public boolean create(Movie entity) throws DAOException {
        boolean created = false;
        try (PreparedStatement st = connection.prepareStatement(INSERT_MOVIE)) {
            st.setString(1, entity.getName());
            st.setString(2, entity.getDescription());
            st.setInt(3, entity.getYear());
            st.setString(4, entity.getCountry());
            st.setString(5, entity.getRef());
            st.setInt(6, entity.getAuthorId());
            int res = st.executeUpdate();
            if (res > 0) {
                created = true;
                updateId(entity);
                log.info("Movie " + entity + " created");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return created;
    }

    @Override
    public List<Movie> findAll() throws DAOException {
        List<Movie> allMovies = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_ALL);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double rating = rs.getDouble(RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(id, name, description, year, country, rating, ref);
                allMovies.add(movie);
            }
            log.info("All movies retrieved");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return allMovies;
    }

    public List<Movie> findAll(int userId) throws DAOException {
        List<Movie> allMovies = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_ALL_FOR_USER)) {
            st.setInt(1, userId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double rating = rs.getDouble(RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(id, name, description, year, country, rating, ref);
                allMovies.add(movie);
            }
            log.info("All movies retrieved");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return allMovies;
    }

    @Override
    public Optional<Movie> findEntityById(int id) throws DAOException {
        Movie movie = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_BY_ID)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString(NAME);
                    String description = rs.getString(DESCRIPTION);
                    int year = rs.getInt(YEAR);
                    String country = rs.getString(COUNTRY);
                    double rating = rs.getDouble(RATING);
                    String ref = rs.getString(REF);
                    movie = new Movie(id, name, description, year, country, rating, ref);
                    log.info("Movie [id = " + id + "] found");
                } else {
                    log.info("Movie [id = " + id + "] not found");
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(movie);
    }

    @Override
    public boolean delete(int id) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(DELETE_BY_ID)) {
            st.setInt(1, id);
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("Movie [id = " + id + "] deleted");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    @Override
    public Movie update(Movie entity) throws DAOException {
        Movie movie = findEntityById(entity.getId()).get();
        try (PreparedStatement st = connection.prepareStatement(UPDATE_MOVIE)) {
            st.setString(1, entity.getName());
            st.setString(2, entity.getDescription());
            st.setInt(3, entity.getYear());
            st.setString(4, entity.getCountry());
            st.setString(5, entity.getRef());
            st.setInt(6, entity.getId());
            int updated = st.executeUpdate();
            if (updated > 0) {
                log.info("Movie " + entity + " updated");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return movie;
    }

    /**
     * Updates rating of the movie with a specific id.
     *
     * @param movieId id of the movie
     * @param rating  new movie rating
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public void updateRating(int movieId, double rating) throws DAOException {
        try (PreparedStatement st = connection.prepareStatement(UPDATE_RATING)) {
            st.setDouble(1, rating);
            st.setInt(2, movieId);
            int rows = st.executeUpdate();
            if (rows > 0) {
                log.info("Rating of movie [id = " + movieId + "] updated");
            } else {
                log.info("Rating of movie [id = " + movieId + "] not updated");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Selects rating of the movie with a specific id.
     *
     * @param movieId id of the movie
     * @return movie rating
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public double selectRating(int movieId) throws DAOException {
        double rating = 0;
        try (PreparedStatement st = connection.prepareStatement(SELECT_RATING_BY_MOVIE_ID)) {
            st.setInt(1, movieId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                rating = rs.getDouble(RATING);
                log.info("Rating of movie [id = " + movieId + "] selected");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rating;
    }

    /**
     * Selects a list of top rated movies.
     *
     * @param size maximum size of the return list
     * @return list of top rated movies
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public List<Movie> findTop(int size) throws DAOException {
        List<Movie> topMovies = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_TOP)) {
            st.setInt(1, size);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double rating = rs.getDouble(RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(id, name, description, year, country, rating, ref);
                topMovies.add(movie);
            }
            log.info("Top " + size + " movies found");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return topMovies;
    }

    public List<Movie> findTop(int size, int userId) throws DAOException {
        List<Movie> topMovies = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_TOP_FOR_USER)) {
            st.setInt(1, userId);
            st.setInt(2, userId);
            st.setInt(3, size);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double rating = rs.getDouble(RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(id, name, description, year, country, rating, ref);
                topMovies.add(movie);
            }
            log.info("Top " + size + " movies found");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return topMovies;
    }

    /**
     * Selects a list of latest added movies.
     *
     * @param size maximum size of the return list
     * @return list of latest added movies
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public List<Movie> findLatestAdded(int size) throws DAOException {
        List<Movie> latestMovies = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_LATEST_ADDED)) {
            st.setInt(1, size);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double rating = rs.getDouble(RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(id, name, description, year, country, rating, ref);
                latestMovies.add(movie);
            }
            log.info("Latest added " + size + " movies found");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return latestMovies;
    }

    /**
     * Selects rating from the user to the movie with specific ids.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return rating
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public byte selectRating(int movieId, int userId) throws DAOException {
        byte rating = -1;
        try (PreparedStatement st = connection.prepareStatement(SELECT_RATING)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                rating = rs.getByte(RATING);
                log.info("Rating of user [" + userId + "] to movie [id = " + movieId + "] selected");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rating;
    }

    /**
     * Selects a list of user ids, who rated the movie with a specific id.
     *
     * @param movieId id of the movie
     * @return list of user ids
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public ArrayList<Integer> selectWhoRated(int movieId) throws DAOException {
        ArrayList<Integer> whoRated = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_UIDS_BY_MID)) {
            st.setInt(1, movieId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt(UID);
                whoRated.add(userId);
            }
            log.info("All users who rated movie [id = " + movieId + "] selected");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return whoRated;
    }

    public boolean share(int ownerId, int sharedId, int movieId, String type) throws DAOException {
        boolean shared = false;
        try (PreparedStatement st = connection.prepareStatement(SHARE_MOVIE)) {
            st.setInt(1, ownerId);
            st.setInt(2, sharedId);
            st.setInt(3, movieId);
            st.setString(4, type);
            int res = st.executeUpdate();
            if (res > 0) {
                shared = true;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return shared;
    }

    public ArrayList<Movie> findOwnerMovies(int userId) throws DAOException {
        ArrayList<Movie> movies = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_OWNER_MOVIES)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(MOVIE_ID);
                String name = rs.getString(NAME);
                String description = rs.getString(DESCRIPTION);
                int year = rs.getInt(YEAR);
                String country = rs.getString(COUNTRY);
                double rating = rs.getDouble(RATING);
                String ref = rs.getString(REF);
                Movie movie = new Movie(id, name, description, year, country, rating, ref);
                movies.add(movie);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return movies;
    }

    /**
     * Retrieves id of the latest added movie.
     *
     * @return id of the latest added movie
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public int selectLastId() throws DAOException {
        return super.selectLastId(SELECT_LAST_ID);
    }
}
