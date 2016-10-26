package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.validation.ValidationResult;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class UserLogicTest {
    private static final String LOGIN = "user";
    private static final String PASSWORD = "user";
    private static final String PASSWORD_NOT_EXISTS = "user_not_exists";
    private static final String PASSWORD_NOT_MATCHES = "user_not_matches";
    private static final int USER_ID = 1;
    private static final int USER_ID_NOT_EXISTING = 10_000_000;
    private static final String LOGIN_NEW = "login";
    private static final String LOGIN_INCORRECT = "login!!!";
    private static final String EMAIL = "email@email.email";
    private static final String EMAIL_NEW = "email_new@email.email";
    private static final String NAME = "Name";
    private static final String NAME_INCORRECT = "name!!!";
    private static final String SURNAME = "Surname";
    private static final String PHOTO = "img/movie/default.png";
    private static final int ID_NOT_EXISTING = 10_000_000;

    @Test
    public void checkLoginTest() throws LogicException {
        User user = UserLogic.checkLogin(LOGIN, PASSWORD);
        Assert.assertNotNull(user);

        user = UserLogic.checkLogin(LOGIN, PASSWORD_NOT_EXISTS);
        Assert.assertNull(user);
    }

    @Test
    public void checkRepeatPasswordTest() throws LogicException {
        boolean matches = UserLogic.checkRepeatPassword(PASSWORD, PASSWORD);
        Assert.assertTrue(matches);

        matches = UserLogic.checkRepeatPassword(PASSWORD, PASSWORD_NOT_MATCHES);
        Assert.assertFalse(matches);
    }

    @Test
    public void findUserTest() throws LogicException {
        Optional<User> optUser = UserLogic.findUser(USER_ID);
        Assert.assertTrue(optUser.isPresent());

        optUser = UserLogic.findUser(USER_ID_NOT_EXISTING);
        Assert.assertFalse(optUser.isPresent());
    }

    @Test
    public void registerTest() throws LogicException {
        User user = new User();
        ValidationResult result;
        try {
            result = UserLogic.register(LOGIN_NEW, PASSWORD, PASSWORD, EMAIL, user);
            Assert.assertEquals(ValidationResult.ALL_RIGHT, result);

            result = UserLogic.register(LOGIN_NEW, PASSWORD, PASSWORD, EMAIL, user);
            Assert.assertEquals(ValidationResult.LOGIN_NOT_UNIQUE, result);

            result = UserLogic.register(LOGIN_INCORRECT, PASSWORD, PASSWORD, EMAIL, user);
            Assert.assertEquals(ValidationResult.LOGIN_PASS_INCORRECT, result);

            result = UserLogic.register(LOGIN_NEW, PASSWORD, PASSWORD_NOT_MATCHES, EMAIL, user);
            Assert.assertEquals(ValidationResult.PASS_NOT_MATCH, result);
        } finally {
            UserLogic.deleteUser(user.getId());
        }
    }

    @Test
    public void editUserTest() throws LogicException {
        User user = new User();
        ValidationResult result;
        try {
            UserLogic.register(LOGIN_NEW, PASSWORD, PASSWORD, EMAIL, user);
            User newUser = new User(
                    user.getId(),
                    user.getLogin(),
                    EMAIL_NEW,
                    user.getRegDate(),
                    user.getRoleID(),
                    NAME,
                    SURNAME,
                    user.getStatus(),
                    PHOTO,
                    user.getNumRated());
            result = UserLogic.editUser(newUser);
            Assert.assertEquals(ValidationResult.ALL_RIGHT, result);

            newUser = new User(
                    user.getId(),
                    user.getLogin(),
                    EMAIL_NEW,
                    user.getRegDate(),
                    user.getRoleID(),
                    NAME_INCORRECT,
                    SURNAME,
                    user.getStatus(),
                    PHOTO,
                    user.getNumRated());
            result = UserLogic.editUser(newUser);
            Assert.assertEquals(ValidationResult.NAME_INCORRECT, result);
        } finally {
            UserLogic.deleteUser(user.getId());
        }
    }

    @Test
    public void deleteUserTest() throws LogicException {
        User user = new User();
        UserLogic.register(LOGIN_NEW, PASSWORD, PASSWORD, EMAIL, user);
        boolean result = UserLogic.deleteUser(user.getId());
        Assert.assertTrue(result);

        result = UserLogic.deleteUser(ID_NOT_EXISTING);
        Assert.assertFalse(result);
    }

    @Test
    public void takeAllUsersTest() throws LogicException {
        List<User> list = UserLogic.takeAllUsers();
        Assert.assertNotNull(list);
        Assert.assertFalse(list.isEmpty());
    }
}
