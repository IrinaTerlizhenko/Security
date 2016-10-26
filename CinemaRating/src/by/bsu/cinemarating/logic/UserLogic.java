package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.dao.UserDAO;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Role;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.loader.PictureLoader;
import by.bsu.cinemarating.logic.encrypt.MD5;
import by.bsu.cinemarating.validation.ValidationResult;
import by.bsu.cinemarating.validation.Validator;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A service layer class implementing all the logic concerning users.
 */
public class UserLogic {
    /**
     * Folder where all profile photos are saved.
     */
    private static final String PICTURE_FOLDER = "img" + File.separator + "photo";
    private static final String DEFAULT_PICTURE = "default.png";

    private static final String REGEXP_LOGIN = "(\\w|\\d){1,30}";
    private static final String REGEXP_PASSWORD = "(\\w|\\d){3,20}";
    private static final String REGEXP_EMAIL_FORMAT = "(\\w|\\d)+@\\w+\\.\\w+";
    private static final String REGEXP_EMAIL_LENGTH = ".{5,40}";
    private static final String REGEXP_NAME = "([A-Z][A-Za-z]*([ -][A-Z][A-Za-z]*)*)|([А-Я][А-Яа-я]*([ -][А-Я][А-Яа-я]*)*)";
    private static final String REGEXP_NAME_LENGTH = ".{1,30}";
    private static final String REGEXP_SURNAME = "([A-Za-z]+([ -'][A-Za-z]+)*)|([А-Яа-я]+([ -'][А-Яа-я]+)*)";
    private static final String REGEXP_SURNAME_LENGTH = ".{1,30}";
    private static final String REGEXP_SURNAME_CAPITAL = "([A-ZА-Я].*)|(.+[ -'][A-ZА-Я].*)";

    /**
     * Checks user's login and password.
     *
     * @param enterLogin entered login
     * @param enterPass  entered password
     * @return user if login and password are correct, null otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static User checkLogin(String enterLogin, String enterPass) throws LogicException {
        User user = null;
        if (!validateLogin(enterLogin) || !validatePassword(enterPass)) {
            return null;
        }
        String md5password = MD5.encrypt(enterPass);
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            Optional<String> optPassword = userDAO.findPasswordByLogin(enterLogin);
            if (optPassword.isPresent()) {
                String pass = optPassword.get();
                if (pass.equals(md5password)) {
                    user = userDAO.findEntityByLogin(enterLogin).get();
                }
            }
        } catch (SQLException e) {
            throw new LogicException("DB connection error: ", e);
        } catch (DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return user;
    }

    /**
     * Checks user's login, password and PIN code.
     *
     * @param enterLogin entered login
     * @param enterPass  entered password
     * @param enterPin entered PIN code
     * @return user if login and password are correct, null otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static User checkLogin(String enterLogin, String enterPass, String enterPin) throws LogicException {
        User user = null;
        if (!validateLogin(enterLogin) || !validatePassword(enterPass)) {
            return null;
        }
        String md5password = MD5.encrypt(enterPass);
        String md5pin = MD5.encrypt(enterPin);
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            Optional<String> optPassword = userDAO.findPasswordByLogin(enterLogin);
            Optional<String> optPin = userDAO.findPinByLogin(enterLogin);
            if (optPassword.isPresent() && optPin.isPresent()) {
                String pass = optPassword.get();
                String pin = optPin.get();
                if (pass.equals(md5password) && pin.equals(md5pin)) {
                    user = userDAO.findEntityByLogin(enterLogin).get();
                }
            }
        } catch (SQLException e) {
            throw new LogicException("DB connection error: ", e);
        } catch (DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return user;
    }

    /**
     * Checks if passwords match.
     *
     * @param password       string with a password
     * @param repeatPassword string with repeated password
     * @return true if both passwords are not empty and password matches repeated password, false otherwise
     */
    public static boolean checkRepeatPassword(String password, String repeatPassword) {
        return password != null && !password.isEmpty() && repeatPassword != null && !repeatPassword.isEmpty() &&
                validatePassword(password) && validatePassword(repeatPassword) && password.equals(repeatPassword);
    }

    /**
     * Finds the user with a specified id.
     *
     * @param userId user id
     * @return user, wrapped in Optional if exists, Optional.empty() otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static Optional<User> findUser(int userId) throws LogicException {
        Optional<User> user = null;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            user = userDAO.findEntityById(userId);
        } catch (SQLException e) {
            throw new LogicException("DB connection error: ", e);
        } catch (DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return user;
    }

    /**
     * Edits the user.
     *
     * @param user user to edit
     * @return result of user validation and editing
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static ValidationResult editUser(User user) throws LogicException {
        return editUser(user, false);
    }

    /**
     * Edits the user with password.
     *
     * @param user user to edit
     * @return result of user validation and editing
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static ValidationResult editUserWithPassword(User user) throws LogicException {
        return editUser(user, true);
    }

    /**
     * Edits the user.
     *
     * @param user         user to edit
     * @param withPassword if needed to edit password
     * @return result of user validation and editing
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    private static ValidationResult editUser(User user, boolean withPassword) throws LogicException {
        ValidationResult result = ValidationResult.UNKNOWN_ERROR;
        if (!validateName(user.getName())) {
            result = ValidationResult.NAME_INCORRECT;
        } else if (!validateSurname(user.getSurname())) {
            result = ValidationResult.SURNAME_INCORRECT;
        } else if (!validateEmail(user.getEmail())) {
            result = ValidationResult.EMAIL_INCORRECT;
        } else if (withPassword && !validatePassword(user.getPassword())) {
            result = ValidationResult.PASS_INCORRECT;
        } else {
            Optional<WrapperConnection> optConnection = Optional.empty();
            try {
                optConnection = ConnectionPool.getInstance().takeConnection();
                WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
                UserDAO userDAO = new UserDAO(connection);
                if (!withPassword) {
                    userDAO.update(user);
                } else {
                    String md5password = MD5.encrypt(user.getPassword());
                    user.setPassword(md5password);
                    userDAO.updateWithPassword(user);
                }
                result = ValidationResult.ALL_RIGHT;
            } catch (SQLException e) {
                throw new LogicException("DB connection error: ", e);
            } catch (DAOException e) {
                throw new LogicException(e);
            } finally {
                optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
            }
        }
        return result;
    }

    /**
     * Deletes the user.
     *
     * @param userId user id
     * @return true if the user was deleted, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean deleteUser(int userId) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            result = userDAO.delete(userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    /**
     * Updates user's photo.
     *
     * @param user     user to update photo
     * @param filePart picture to set
     * @param path     path to the root folder
     * @throws LogicException if any exceptions occurred with input/output
     */
    public static void updatePhoto(User user, Part filePart, String path) throws LogicException {
        try {
            String filename = PictureLoader.loadPicture(filePart, user.getId(), path, PICTURE_FOLDER, DEFAULT_PICTURE);
            user.setPhoto(filename);
        } catch (IOException e) {
            throw new LogicException(e);
        }
    }

    /**
     * Retrieves all users registered in the system.
     *
     * @return list of all users registered in the system
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static List<User> takeAllUsers() throws LogicException {
        List<User> userList = new ArrayList<>();
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            userList = userDAO.findAll();
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return userList;
    }

    /**
     * Resgisters the user in the system.
     *
     * @param login          login of the user
     * @param password       password of the user
     * @param repeatPassword repeated password
     * @param email          email of the user
     * @param user           returned object User, should be empty on call
     * @return result of user validation and registration
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static ValidationResult register(String login, String password, String repeatPassword, String email, User user) throws LogicException {
        ValidationResult result;
        if (!validateLogin(login) || !validatePassword(password)) {
            result = ValidationResult.LOGIN_PASS_INCORRECT;
        } else if (!validateEmail(email)) {
            result = ValidationResult.EMAIL_INCORRECT;
        } else if (!password.equals(repeatPassword)) {
            result = ValidationResult.PASS_NOT_MATCH;
        } else {
            Optional<WrapperConnection> optConnection = Optional.empty();
            try {
                optConnection = ConnectionPool.getInstance().takeConnection();
                WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
                UserDAO userDAO = new UserDAO(connection);
                if (userDAO.isLoginFree(login)) {
                    if (userDAO.isEmailFree(email)) {
                        String md5password = MD5.encrypt(password);
                        user.setLogin(login);
                        user.setPassword(md5password);
                        user.setEmail(email);
                        user.setRegDate(java.sql.Date.valueOf(java.time.LocalDate.now()));
                        user.setRoleID((byte) Role.USER.ordinal());
                        boolean registered = userDAO.create(user);
                        result = registered ? ValidationResult.ALL_RIGHT : ValidationResult.UNKNOWN_ERROR;
                    } else {
                        result = ValidationResult.EMAIL_NOT_UNIQUE;
                    }
                } else {
                    result = ValidationResult.LOGIN_NOT_UNIQUE;
                }
            } catch (SQLException e) {
                throw new LogicException("DB connection error: ", e);
            } catch (DAOException e) {
                throw new LogicException(e);
            } finally {
                optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
            }
        }
        return result;
    }

    public static ValidationResult register(String login, String password, String repeatPassword, String pin, String email, User user) throws LogicException {
        ValidationResult result;
        if (!validateLogin(login) || !validatePassword(password)) {
            result = ValidationResult.LOGIN_PASS_INCORRECT;
        } else if (!validateEmail(email)) {
            result = ValidationResult.EMAIL_INCORRECT;
        } else if (!password.equals(repeatPassword)) {
            result = ValidationResult.PASS_NOT_MATCH;
        } else {
            Optional<WrapperConnection> optConnection = Optional.empty();
            try {
                optConnection = ConnectionPool.getInstance().takeConnection();
                WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
                UserDAO userDAO = new UserDAO(connection);
                if (userDAO.isLoginFree(login)) {
                    if (userDAO.isEmailFree(email)) {
                        String md5password = MD5.encrypt(password);
                        user.setLogin(login);
                        user.setPassword(md5password);
                        user.setEmail(email);
                        user.setRegDate(java.sql.Date.valueOf(java.time.LocalDate.now()));
                        user.setRoleID((byte) Role.USER.ordinal());
                        boolean registered = userDAO.create(user, MD5.encrypt(String.valueOf(pin)));
                        result = registered ? ValidationResult.ALL_RIGHT : ValidationResult.UNKNOWN_ERROR;
                    } else {
                        result = ValidationResult.EMAIL_NOT_UNIQUE;
                    }
                } else {
                    result = ValidationResult.LOGIN_NOT_UNIQUE;
                }
            } catch (SQLException e) {
                throw new LogicException("DB connection error: ", e);
            } catch (DAOException e) {
                throw new LogicException(e);
            } finally {
                optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
            }
        }
        return result;
    }

    /**
     * Updates status of the user with a specific id
     *
     * @param userId id of the user
     * @param status new user status
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static void updateStatus(int userId, double status) throws LogicException {
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            userDAO.updateStatus(userId, status);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
    }

    public static boolean canEdit(int userId, int movieId) throws LogicException {
        boolean canEdit = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            canEdit = userDAO.canEdit(userId, movieId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return canEdit;
    }

    /**
     * Validates login. A login consists of from 1 to 30 english letters of digits.
     *
     * @param login String containing login
     * @return true if login is valid, false otherwise
     */
    private static boolean validateLogin(String login) {
        return Validator.validate(login, REGEXP_LOGIN);
    }

    /**
     * Validates password. A password consists of from 3 to 20 english letters of digits.
     *
     * @param password String containing password
     * @return true if password is valid, false otherwise
     */
    private static boolean validatePassword(String password) {
        return Validator.validate(password, REGEXP_PASSWORD);
    }

    /**
     * Validates e-mail. An e-mail has format aaa@bbb.ccc Parts bbb, ccc consist of at least 1 english
     * letter. Part aaa consists of at least 1 english letter or digit. E-mail contains not more than 40 symbols.
     *
     * @param email String containing e-mail
     * @return true if e-mail is valid, false otherwise
     */
    private static boolean validateEmail(String email) {
        return Validator.validate(email, REGEXP_EMAIL_FORMAT, REGEXP_EMAIL_LENGTH);
    }

    /**
     * Validates name. A name contains either english or russian letters, spaces and hyphens. Every word after a
     * space or a hyphen begins with a capital letter. A name contains from 1 to 30 symbols.
     *
     * @param name String containing user name
     * @return true if name is valid, false otherwise
     */
    private static boolean validateName(String name) {
        return Validator.validate(name, REGEXP_NAME, REGEXP_NAME_LENGTH);
    }

    /**
     * Validates surname. A surname contains either english or russian letters, spaces, hyphens and apostrophes.
     * There must be at least one word starting with a capital letter. A surname contains from 1 to 30 symbols.
     *
     * @param surname String containing user surname
     * @return true if surname is valid, false otherwise
     */
    private static boolean validateSurname(String surname) {
        return Validator.validate(surname, REGEXP_SURNAME, REGEXP_SURNAME_CAPITAL, REGEXP_SURNAME_LENGTH);
    }
}
