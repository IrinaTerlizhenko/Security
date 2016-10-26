package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.entity.Movie;
import by.bsu.cinemarating.entity.Review;
import by.bsu.cinemarating.exception.LogicException;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Optional;

public class ReviewLogicTest {
    private static final int MOVIE_ID = 29;
    private static final int USER_ID = 1;
    private static final int USER_ID_NOT_EXISTING = 10_000_000;
    private static final int USER_ID_2 = 12;
    private static final String TEXT = "some review";
    private static final String TEXT_REPLACING = "some review 2";

    @Test
    public void takeReviewTestNormal() throws LogicException {
        Optional<String> optReviewText = ReviewLogic.takeReview(MOVIE_ID, USER_ID);
        Assert.assertTrue(optReviewText.isPresent());
    }

    @Test
    public void takeReviewTestNotExisting() throws LogicException {
        Optional<String> optReviewText = ReviewLogic.takeReview(MOVIE_ID, USER_ID_NOT_EXISTING);
        Assert.assertFalse(optReviewText.isPresent());
    }

    @Test
    public void replaceReviewTestNotExisting() throws LogicException {
        try {
            boolean added = ReviewLogic.replaceReview(MOVIE_ID, USER_ID_2, TEXT);
            Assert.assertTrue(added);
            String text = ReviewLogic.takeReview(MOVIE_ID, USER_ID_2).get();
            Assert.assertEquals(TEXT, text);
        } finally {
            ReviewLogic.deleteReview(MOVIE_ID, USER_ID_2);
        }
    }

    @Test
    public void replaceReviewTestExisting() throws LogicException {
        try {
            ReviewLogic.replaceReview(MOVIE_ID, USER_ID_2, TEXT);
            boolean replaced = ReviewLogic.replaceReview(MOVIE_ID, USER_ID_2, TEXT_REPLACING);
            Assert.assertTrue(replaced);
            String text = ReviewLogic.takeReview(MOVIE_ID, USER_ID_2).get();
            Assert.assertEquals(TEXT_REPLACING, text);
        } finally {
            ReviewLogic.deleteReview(MOVIE_ID, USER_ID_2);
        }
    }

    @Test
    public void isReviewedTestTrue() throws LogicException {
        try {
            ReviewLogic.replaceReview(MOVIE_ID, USER_ID_2, TEXT);
            boolean isReviewed = ReviewLogic.isReviewed(MOVIE_ID, USER_ID_2);
            Assert.assertTrue(isReviewed);
        } finally {
            ReviewLogic.deleteReview(MOVIE_ID, USER_ID_2);
        }
    }

    @Test
    public void isReviewedTestFalse() throws LogicException {
        boolean isReviewed = ReviewLogic.isReviewed(MOVIE_ID, USER_ID_2);
        Assert.assertFalse(isReviewed);
    }

    @Test
    public void takeUserReviewsTestExisting() throws LogicException {
        LinkedHashMap<Movie, Review> map = ReviewLogic.takeUserReviews(USER_ID);
        Assert.assertNotNull(map);
        Assert.assertFalse(map.isEmpty());
    }

    @Test
    public void takeUserReviewsTestNotExisting() throws LogicException {
        LinkedHashMap<Movie, Review> map = ReviewLogic.takeUserReviews(USER_ID_2);
        Assert.assertNotNull(map);
        Assert.assertTrue(map.isEmpty());
    }
}
