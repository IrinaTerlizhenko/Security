package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.entity.BanType;
import by.bsu.cinemarating.exception.LogicException;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;

public class BanLogicTest {
    private static final int USER_ID = 2;
    private static final int USER_ID_2 = 1;
    private static final BanType TYPE = BanType.ABUSE;
    private static final String REASON = "some reason";
    private static final Timestamp EXPIRATION = Timestamp.valueOf("2016-07-07 10:00:00");
    private static final Timestamp EXPIRATION_ENDED = Timestamp.valueOf("2020-06-06 10:00:00");

    @Test
    public void addBanTest() throws LogicException {
        int id = 0;
        try {
            boolean added = BanLogic.addBan(USER_ID, TYPE, REASON, EXPIRATION);
            id = BanLogic.takeLastId();
            Assert.assertTrue(added);
        } finally {
            BanLogic.deleteBan(id);
        }
    }

    @Test
    public void isUserBannedTestTrue() throws LogicException {
        int id = 0;
        try {
            BanLogic.addBan(USER_ID_2, TYPE, REASON, EXPIRATION);
            id = BanLogic.takeLastId();
            boolean banned = BanLogic.isUserBanned(USER_ID_2);
            Assert.assertTrue(banned);
        } finally {
            BanLogic.deleteBan(id);
        }
    }

    @Test
    public void isUserBannedTestExpired() throws LogicException {
        int id = 0;
        try {
            BanLogic.addBan(USER_ID_2, TYPE, REASON, EXPIRATION_ENDED);
            id = BanLogic.takeLastId();
            boolean banned = BanLogic.isUserBanned(USER_ID_2);
            Assert.assertTrue(banned);
        } finally {
            BanLogic.deleteBan(id);
        }
    }

    @Test
    public void isUserBannedTestFalse() throws LogicException {
        boolean banned = BanLogic.isUserBanned(USER_ID_2);
        Assert.assertFalse(banned);
    }

    @Test
    public void deleteBanTest() throws LogicException {
        BanLogic.addBan(USER_ID, TYPE, REASON, EXPIRATION);
        int id = BanLogic.takeLastId();
        boolean deleted = BanLogic.deleteBan(id);
        Assert.assertTrue(deleted);
    }
}
