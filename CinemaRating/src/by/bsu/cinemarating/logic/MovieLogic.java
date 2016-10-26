package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.dao.MovieDAO;
import by.bsu.cinemarating.dao.ReviewDAO;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.loader.PictureLoader;
import by.bsu.cinemarating.validation.ValidationResult;
import by.bsu.cinemarating.validation.Validator;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;
import java.util.List;
import java.util.Optional;

/**
 * A service layer class implementing all the logic concerning movies.
 */
public class MovieLogic {
    /**
     * Folder where all movie pictures are saved.
     */
    private static final String PICTURE_FOLDER = "img" + File.separator + "movie";
    private static final String DEFAULT_PICTURE = "default.png";

    private static final String REGEXP_MOVIE_NAME = ".{1,50}";
    private static final String REGEXP_MOVIE_DESCRIPTION = ".{0,1000}";
    private static final int MIN_YEAR = 1888;
    private static final int MAX_YEAR = Year.now().getValue() + 1;
    private static final String REGEXP_COUNTRY = "([A-Z][A-Za-z]*(, [A-Z][A-Za-z]*)*)|([А-Я][А-Яа-я]*(, [А-Я][А-Яа-я]*)*)";

    /**
     * Retrieves all the movies in the system.
     *
     * @return List of all movies added to the system.
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static List<Movie> takeAllMovies() throws LogicException {
        List<Movie> list;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            list = movieDAO.findAll();
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return list;
    }

    public static List<Movie> takeAllMovies(int userId) throws LogicException {
        List<Movie> list;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            list = movieDAO.findAll(userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return list;
    }

    /**
     * Retrives a list of movies with maximum ratings. The size of the list is bounded up to size parameter.
     *
     * @param size maximum size of the return list
     * @return List of movies with maximum ratings
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static List<Movie> takeTopMovies(int size) throws LogicException {
        List<Movie> list;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            list = movieDAO.findTop(size);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return list;
    }

    public static List<Movie> takeTopMovies(int size, int userId) throws LogicException {
        List<Movie> list;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            list = movieDAO.findTop(size, userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return list;
    }

    /**
     * Retrives a list of latest added movies. The size of the list is bounded up to size parameter.
     *
     * @param size maximum size of the return list
     * @return List of latest added movies
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static List<Movie> takeLatestAddedMovies(int size) throws LogicException {
        List<Movie> list;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            list = movieDAO.findLatestAdded(size);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return list;
    }

    /**
     * Retrieves a movie with a specified id.
     *
     * @param movieId id of the movie
     * @return Movie with a specified id
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static Movie takeMovie(int movieId) throws LogicException {
        Movie movie;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            Optional<Movie> optMovie = movieDAO.findEntityById(movieId);
            movie = optMovie.orElseThrow(LogicException::new);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return movie;
    }

    /**
     * Retrives a list of all movie reviews. If a review with specified movieId and userId exists, it comes first.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return List of reviews on a movie with a specified id
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static List<Review> takeMovieReviews(int movieId, int userId) throws LogicException {
        List<Review> reviews;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            ReviewDAO reviewDAO = new ReviewDAO(connection);
            reviews = reviewDAO.findReviews(movieId, userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return reviews;
    }

    /**
     * Adds a movie to the system.
     *
     * @param name        movie name
     * @param description movie description
     * @param year        year of movie premiere
     * @param country     country(-ies) of movie production
     * @param filePart    movie picture
     * @param path        path to the server root folder
     * @return ValidationResult.ALL_RIGHT if the movie was added, ValidationResult with a specific cause if not valid,
     * ValidationResult.UNKNOWN_ERROR otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static ValidationResult addMovie(String name, String description, int year, String country, Part filePart,
                                            String path, int authorId) throws LogicException {
        ValidationResult result = ValidationResult.UNKNOWN_ERROR;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            if (!validateMovieName(name)) {
                result = ValidationResult.NAME_INCORRECT;
            } else if (!validateMovieDescription(description)) {
                result = ValidationResult.DESCRIPTION_INCORRECT;
            } else if (!validateYear(year)) {
                result = ValidationResult.YEAR_INCORRECT;
            } else if (!validateCountry(country)) {
                result = ValidationResult.COUNTRY_INCORRECT;
            } else {
                optConnection = ConnectionPool.getInstance().takeConnection();
                WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
                MovieDAO movieDAO = new MovieDAO(connection);
                Movie movie = new Movie(0, name, description, year, country, 0.0, PICTURE_FOLDER + File.separator + DEFAULT_PICTURE);
                movie.setAuthorId(authorId);
                movieDAO.create(movie);
                String filename = PictureLoader.loadPicture(filePart, movie.getId(), path, PICTURE_FOLDER, DEFAULT_PICTURE);
                if (filename != null) {
                    movie.setRef(filename);
                    movieDAO.update(movie);
                }
                result = ValidationResult.ALL_RIGHT;
            }
        } catch (IOException | SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    /**
     * Edits an existing movie.
     *
     * @param movieId     movie id
     * @param name        movie name
     * @param description movie description
     * @param year        year of movie premiere
     * @param country     country(-ies) of movie production
     * @param filePart    movie picture
     * @param path        path to the server root folder
     * @return edited movie
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static Movie editMovie(int movieId, String name, String description, int year, String country, Part filePart, String path) throws LogicException {
        Movie movie;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            String filename = PictureLoader.loadPicture(filePart, movieId, path, PICTURE_FOLDER, DEFAULT_PICTURE);
            movie = new Movie(movieId, name, description, year, country, 0.0, (filename != null) ? filename : PICTURE_FOLDER + File.separator + DEFAULT_PICTURE);
            MovieDAO movieDAO = new MovieDAO(connection);
            Movie oldMovie = movieDAO.update(movie);
            movie.setRating(oldMovie.getRating());
        } catch (IOException | SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return movie;
    }

    /**
     * Deletes a movie from the system.
     *
     * @param movieId movie id
     * @return true if the movie was deleted, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean deleteMovie(int movieId) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            result = movieDAO.delete(movieId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    public static boolean share(int ownerId, int sharedId, int movieId, String type) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            result = movieDAO.share(ownerId, sharedId, movieId, type);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    public static List<Movie> takeOwnerMovies(int userId) throws LogicException {
        List<Movie> movies;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            movies = movieDAO.findOwnerMovies(userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return movies;
    }

    /**
     * Retrieves id of the last added movie.
     *
     * @return id of the last added movie
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static int takeLastId() throws LogicException {
        int id = 0;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            MovieDAO movieDAO = new MovieDAO(connection);
            id = movieDAO.selectLastId();
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return id;
    }

    /**
     * Validates movie name. A movie name contains from 1 to 50 symbols.
     *
     * @param movieName String containing movie name
     * @return true if name is valid, false otherwise
     */
    private static boolean validateMovieName(String movieName) {
        return Validator.validate(movieName, REGEXP_MOVIE_NAME);
    }

    /**
     * Validates movie description. A movie description contains from 0 to 1000 symbols.
     *
     * @param movieDescription String containing movie description
     * @return true if description is valid, false otherwise
     */
    private static boolean validateMovieDescription(String movieDescription) {
        return Validator.validate(movieDescription, REGEXP_MOVIE_DESCRIPTION);
    }

    /**
     * Validates movie year. A year is a positive number between 1888 and future year.
     *
     * @param year movie premiere year
     * @return true if year is valid, false otherwise
     */
    private static boolean validateYear(int year) {
        return year >= MIN_YEAR && year <= MAX_YEAR;
    }

    /**
     * Validates movie production country. A country contains either english or russian letters. A country contains
     * one or more words starting with a capital letter, divided from each other by one comma and space.
     *
     * @param country String containing movie production countries (maybe several)
     * @return true if country is valid, false otherwise
     */
    private static boolean validateCountry(String country) {
        return Validator.validate(country, REGEXP_COUNTRY);
    }
}
