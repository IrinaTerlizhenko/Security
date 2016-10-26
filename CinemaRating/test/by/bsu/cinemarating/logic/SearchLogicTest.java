package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.entity.Entity;
import by.bsu.cinemarating.exception.LogicException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SearchLogicTest {
    private static final String[] QUERIES = {"Вася", "Побег", "sfaefog"};

    @Test
    public void findFullCoincidenceTest() throws LogicException {
        List<List<Entity>> list = SearchLogic.findFullCoincidence(QUERIES[0]);
        Assert.assertNotNull(list);
        Assert.assertNotNull(list.get(0));
        Assert.assertNotNull(list.get(1));
        Assert.assertFalse(list.get(0).isEmpty());
        Assert.assertTrue(list.get(1).isEmpty());

        list = SearchLogic.findFullCoincidence(QUERIES[1]);
        Assert.assertNotNull(list);
        Assert.assertNotNull(list.get(0));
        Assert.assertNotNull(list.get(1));
        Assert.assertTrue(list.get(0).isEmpty());
        Assert.assertFalse(list.get(1).isEmpty());

        list = SearchLogic.findFullCoincidence(QUERIES[2]);
        Assert.assertNotNull(list);
        Assert.assertNotNull(list.get(0));
        Assert.assertNotNull(list.get(1));
        Assert.assertTrue(list.get(0).isEmpty());
        Assert.assertTrue(list.get(1).isEmpty());
    }
}
