package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.validation.ValidationResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MovieLogicTest {
    private static final int SIZE_NORMAL = 10;
    private static final int SIZE_LARGE = 10_000_000;
    private static final int MOVIE_ID = 7;
    private static final int MOVIE_ID_2 = 29;
    private static final int MOVIE_ID_NOT_EXISTING = 10_000_000;
    private static final int MOVIE_ID_REVIEW_NOT_EXISTING = 7;
    private static final String MOVIE_NAME = "Побег из Шоушенка";
    private static final String DESCRIPTION = "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.";
    private static final int YEAR = 1994;
    private static final String COUNTRY = "США";
    private static final String REF = "img/movie/7.png";
    private static final String ADD_MOVIE_NAME = "Movie name";
    private static final String ADD_DESCRIPTION = "Movie description.";
    private static final int ADD_YEAR = 2000;
    private static final int ADD_YEAR_2 = 2001;
    private static final String ADD_COUNTRY = "Country, Countryy";
    private static final String ADD_REF = "img\\movie\\default.png";
    private static final String ADD_MOVIE_NAME_INCORRECT = "ADD_MOVIE_NAME_ADD_MOVIE_NAME_ADD_MOVIE_NAME_MORE_THAN_50_SYMBOLS";
    private static final int ADD_YEAR_INCORRECT = 15;
    private static final String ADD_COUNTRY_INCORRECT = "Country,! Countryy";

    @Test
    public void takeAllMoviesTest() throws LogicException {
        List<Movie> movieList = MovieLogic.takeAllMovies();
        Assert.assertNotNull(movieList);
        Assert.assertTrue(movieList.size() > 0);
    }

    @Test
    public void takeTopMoviesTestNormal() throws LogicException {
        List<Movie> movieList = MovieLogic.takeTopMovies(SIZE_NORMAL);
        Assert.assertNotNull(movieList);
        Assert.assertEquals(SIZE_NORMAL, movieList.size());

        movieList = MovieLogic.takeTopMovies(SIZE_LARGE);
        Assert.assertNotNull(movieList);
        Assert.assertTrue(movieList.size() < SIZE_LARGE);
        List<Movie> movieListAll = MovieLogic.takeAllMovies();
        Assert.assertEquals(movieListAll.size(), movieList.size());
    }

    @Test
    public void takeLatestAddedMoviesTestNormal() throws LogicException {
        List<Movie> movieList = MovieLogic.takeLatestAddedMovies(SIZE_NORMAL);
        Assert.assertNotNull(movieList);
        Assert.assertEquals(SIZE_NORMAL, movieList.size());

        movieList = MovieLogic.takeLatestAddedMovies(SIZE_LARGE);
        Assert.assertNotNull(movieList);
        Assert.assertTrue(movieList.size() < SIZE_LARGE);
        List<Movie> movieListAll = MovieLogic.takeAllMovies();
        Assert.assertEquals(movieListAll.size(), movieList.size());
    }

    @Test
    public void takeMovieTestNormal() throws LogicException {
        Movie movie = MovieLogic.takeMovie(MOVIE_ID);
        Assert.assertNotNull(movie);
        Assert.assertEquals(MOVIE_NAME, movie.getName());
        Assert.assertEquals(DESCRIPTION, movie.getDescription());
        Assert.assertEquals(YEAR, movie.getYear());
        Assert.assertEquals(COUNTRY, movie.getCountry());
        Assert.assertEquals(REF, movie.getRef());
    }

    @Test(expected = LogicException.class)
    public void takeMovieTestNotExisting() throws LogicException {
        MovieLogic.takeMovie(MOVIE_ID_NOT_EXISTING);
    }

    @Test
    public void takeMovieReviewsTest() throws LogicException {
        List<Review> reviewList = MovieLogic.takeMovieReviews(MOVIE_ID_2, 0);
        Assert.assertNotNull(reviewList);
        Assert.assertTrue(reviewList.size() > 0);

        reviewList = MovieLogic.takeMovieReviews(MOVIE_ID_REVIEW_NOT_EXISTING, 0);
        Assert.assertNotNull(reviewList);
        Assert.assertTrue(reviewList.isEmpty());
    }

    @Test
    public void addMovieTestNormal() throws LogicException {
        int id = 0;
        try {
            ValidationResult result = MovieLogic.addMovie(ADD_MOVIE_NAME, ADD_DESCRIPTION, ADD_YEAR, ADD_COUNTRY, null, null, 0);
            id = MovieLogic.takeLastId();
            Assert.assertEquals(ValidationResult.ALL_RIGHT, result);
        } finally {
            MovieLogic.deleteMovie(id);
        }

        ValidationResult result = MovieLogic.addMovie(ADD_MOVIE_NAME_INCORRECT, ADD_DESCRIPTION, ADD_YEAR, ADD_COUNTRY, null, null, 0);
        Assert.assertEquals(ValidationResult.NAME_INCORRECT, result);

        result = MovieLogic.addMovie(ADD_MOVIE_NAME, ADD_DESCRIPTION, ADD_YEAR_INCORRECT, ADD_COUNTRY, null, null, 0);
        Assert.assertEquals(ValidationResult.YEAR_INCORRECT, result);

        result = MovieLogic.addMovie(ADD_MOVIE_NAME, ADD_DESCRIPTION, ADD_YEAR, ADD_COUNTRY_INCORRECT, null, null, 0);
        Assert.assertEquals(ValidationResult.COUNTRY_INCORRECT, result);
    }

    @Test
    public void editMovieTest() throws LogicException {
        MovieLogic.addMovie(ADD_MOVIE_NAME, ADD_DESCRIPTION, ADD_YEAR, ADD_COUNTRY, null, null, 0);
        int id = MovieLogic.takeLastId();
        Movie movie = new Movie(id, ADD_MOVIE_NAME, ADD_DESCRIPTION, ADD_YEAR_2, ADD_COUNTRY, 0.0, ADD_REF);
        Movie newMovie = MovieLogic.editMovie(id, ADD_MOVIE_NAME, ADD_DESCRIPTION, ADD_YEAR_2, ADD_COUNTRY, null, null);
        Assert.assertEquals(movie, newMovie);
        MovieLogic.deleteMovie(id);
    }

    @Test
    public void deleteTestNormal() throws LogicException {
        MovieLogic.addMovie(ADD_MOVIE_NAME, ADD_DESCRIPTION, ADD_YEAR, ADD_COUNTRY, null, null, 0);
        int id = MovieLogic.takeLastId();
        boolean deleted = MovieLogic.deleteMovie(id);
        Assert.assertTrue(deleted);

        deleted = MovieLogic.deleteMovie(MOVIE_ID_NOT_EXISTING);
        Assert.assertFalse(deleted);
    }
}
