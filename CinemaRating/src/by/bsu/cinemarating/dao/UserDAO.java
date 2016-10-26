package by.bsu.cinemarating.dao;

import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.User;
import by.bsu.cinemarating.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 12.04.16
 * Time: 5:09
 * To change this template use File | Settings | File Templates.
 */
public class UserDAO extends AbstractDAO<User> {
    private static Logger log = LogManager.getLogger(UserDAO.class);

    private static final String SELECT_ALL = "SELECT user_id,login,email,reg_date,role_id,name,surname,status,photo,num_rated FROM users";
    private static final String SELECT_BY_ID = "SELECT login,email,reg_date,role_id,name,surname,status,photo,num_rated FROM users WHERE user_id=?";
    private static final String SELECT_USER_BY_LOGIN = "SELECT user_id,email,reg_date,role_id,name,surname,status,photo,num_rated FROM users WHERE login=?";
    private static final String SELECT_PASSWORD_BY_LOGIN = "SELECT password FROM users WHERE login=?";
    private static final String SELECT_PIN_BY_LOGIN = "SELECT PIN FROM users WHERE login=?";
    private static final String SELECT_RATING = "SELECT rating FROM ratings WHERE mid=? AND uid=?";
    private static final String SELECT_BY_LOGIN = "SELECT user_id FROM users WHERE login=?";
    private static final String SELECT_BY_EMAIL = "SELECT user_id FROM users WHERE email=?";

    private static final String INSERT_USER = "INSERT INTO users(login,password,email,reg_date,role_id) VALUES(?,?,?,?,1)";
    private static final String INSERT_USER_PIN = "INSERT INTO users(login,password,email,reg_date,role_id,PIN) VALUES(?,?,?,?,1,?)";
    private static final String INSERT_TOKEN = "REPLACE INTO tokens(id,token) VALUES(?,?)";

    private static final String DELETE_BY_ID = "DELETE FROM users WHERE user_id=?";

    private static final String UPDATE_USER = "UPDATE users SET login=?,email=?,reg_date=?,role_id=?,name=?,surname=?,status=?,photo=?,num_rated=? WHERE user_id=?";
    private static final String UPDATE_USER_WITH_PASSWORD =
            "UPDATE users SET login=?,email=?,reg_date=?,role_id=?,name=?,surname=?,status=?,photo=?,num_rated=?,password=? WHERE user_id=?";
    private static final String UPDATE_STATUS = "UPDATE users SET status=? WHERE user_id=?";
    private static final String UPDATE_NUM_RATED = "UPDATE users SET num_rated=? WHERE user_id=?";

    private static final String SELECT_CAN_EDIT =
            "SELECT COUNT(1) AS VALUE FROM shared JOIN movies WHERE movies.movie_id=? AND (added_by=? OR (shared.movie_id = movies.movie_id AND shared_id=? AND access_type='READ_WRITE'))";
    private static final String SELECT_TOKEN = "SELECT token FROM tokens WHERE id=?";
    private static final String SELECT_TOKEN_TIME = "SELECT generated, valid_for_minutes FROM tokens WHERE id=?";

    public UserDAO(WrapperConnection connection) {
        super(connection);
    }

    @Override
    public boolean create(User entity) throws DAOException {
        boolean created = false;
        try (PreparedStatement st = connection.prepareStatement(INSERT_USER)) {
            st.setString(1, entity.getLogin());
            st.setString(2, entity.getPassword());
            st.setString(3, entity.getEmail());
            st.setDate(4, entity.getRegDate());
            int res = st.executeUpdate();
            if (res > 0) {
                created = true;
                updateId(entity);
                log.info("User " + entity + " created");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return created;
    }

    public boolean create(User entity, String pin) throws DAOException {
        boolean created = false;
        try (PreparedStatement st = connection.prepareStatement(INSERT_USER_PIN)) {
            st.setString(1, entity.getLogin());
            st.setString(2, entity.getPassword());
            st.setString(3, entity.getEmail());
            st.setDate(4, entity.getRegDate());
            st.setString(5, pin);
            int res = st.executeUpdate();
            if (res > 0) {
                created = true;
                updateId(entity);
                log.info("User " + entity + " created");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return created;
    }

    @Override
    public List<User> findAll() throws DAOException {
        List<User> allUsers = new ArrayList<>();
        try (PreparedStatement st = connection.prepareStatement(SELECT_ALL)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(USER_ID);
                String login = rs.getString(LOGIN);
                String email = rs.getString(EMAIL);
                Date reg_date = rs.getDate(REG_DATE);
                byte role_id = rs.getByte(ROLE_ID);
                String name = rs.getString(NAME);
                String surname = rs.getString(SURNAME);
                double status = rs.getDouble(STATUS);
                String photo = rs.getString(PHOTO);
                int numRated = rs.getInt(NUM_RATED);
                User user = new User(id, login, email, reg_date, role_id, name, surname, status, photo, numRated);
                allUsers.add(user);
            }
            log.info("All users retrieved");
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return allUsers;
    }

    @Override
    public Optional<User> findEntityById(int id) throws DAOException {
        User user = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_BY_ID)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String login = rs.getString(LOGIN);
                String email = rs.getString(EMAIL);
                Date reg_date = rs.getDate(REG_DATE);
                byte role_id = rs.getByte(ROLE_ID);
                String name = rs.getString(NAME);
                String surname = rs.getString(SURNAME);
                double status = rs.getDouble(STATUS);
                String photo = rs.getString(PHOTO);
                int numRated = rs.getInt(NUM_RATED);
                user = new User(id, login, email, reg_date, role_id, name, surname, status, photo, numRated);
                log.info("User [id = " + id + "] found");
            } else {
                log.info("User [id = " + id + "] not found");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public boolean delete(int id) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(DELETE_BY_ID)) {
            st.setInt(1, id);
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("User [id = " + id + "] deleted");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    @Override
    public User update(User entity) throws DAOException {
        return update(entity, false);
    }

    /**
     * Updates the entity with a specific id with password.
     *
     * @param entity new entity
     * @return previous entity with the given id
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public User updateWithPassword(User entity) throws DAOException {
        return update(entity, true);
    }

    /**
     * Updates the entity with a specific id.
     *
     * @param entity       new entity
     * @param withPassword true if password needs to be updated, false otherwise
     * @return previous entity with the given id
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    private User update(User entity, boolean withPassword) throws DAOException {
        User user = findEntityById(entity.getId()).get();
        String sqlQuery = withPassword ? UPDATE_USER_WITH_PASSWORD : UPDATE_USER;
        try (PreparedStatement st = connection.prepareStatement(sqlQuery)) {
            st.setString(1, entity.getLogin());
            st.setString(2, entity.getEmail());
            st.setDate(3, entity.getRegDate());
            st.setInt(4, entity.getRoleID());
            st.setString(5, entity.getName());
            st.setString(6, entity.getSurname());
            st.setDouble(7, entity.getStatus());
            st.setString(8, entity.getPhoto());
            st.setInt(9, entity.getNumRated());
            if (withPassword) {
                st.setString(10, entity.getPassword());
                st.setInt(11, entity.getId());
            } else {
                st.setInt(10, entity.getId());
            }
            int updated = st.executeUpdate();
            if (updated > 0) {
                log.info("Movie " + entity + " updated");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return user;
    }

    /**
     * Selects the user with the specific login
     *
     * @param login login of the user
     * @return the user with the specific login wrapped in Optional if exists, Optional.empty() otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public Optional<User> findEntityByLogin(String login) throws DAOException {
        User user = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_USER_BY_LOGIN)) {
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(USER_ID);
                String email = rs.getString(EMAIL);
                Date reg_date = rs.getDate(REG_DATE);
                byte role_id = rs.getByte(ROLE_ID);
                String name = rs.getString(NAME);
                String surname = rs.getString(SURNAME);
                double status = rs.getDouble(STATUS);
                String photo = rs.getString(PHOTO);
                int numRated = rs.getInt(NUM_RATED);
                user = new User(id, login, email, reg_date, role_id, name, surname, status, photo, numRated);
                log.info("User [login = " + login + "] found");
            } else {
                log.info("User [login = " + login + "] not found");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(user);
    }

    /**
     * Selects the password of the user with the specific login
     *
     * @param login login of the user
     * @return the password of the user with the specific login wrapped in Optional if exists, Optional.empty() otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public Optional<String> findPasswordByLogin(String login) throws DAOException {
        String password = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_PASSWORD_BY_LOGIN)) {
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                password = rs.getString(PASSWORD);
                log.info("Password by login = " + login + " retrieved");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(password);
    }

    /**
     * Selects the PIN code of the user with the specific login
     *
     * @param login login of the user
     * @return the PIN code of the user with the specific login wrapped in Optional if exists, Optional.empty() otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public Optional<String> findPinByLogin(String login) throws DAOException {
        String pin = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_PIN_BY_LOGIN)) {
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                pin = rs.getString("pin");
                log.info("Pin by login = " + login + " retrieved");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return Optional.ofNullable(pin);
    }

    /**
     * Updates the status of the user with a specific id.
     *
     * @param userId id of the user
     * @param status new status
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public void updateStatus(int userId, double status) throws DAOException {
        try (PreparedStatement st = connection.prepareStatement(UPDATE_STATUS)) {
            st.setDouble(1, status);
            st.setInt(2, userId);
            if (st.executeUpdate() > 0) {
                log.info("User [id = " + userId + "] status updated to " + status);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    /**
     * Checks if there is no user with the given login in the system.
     *
     * @param login login of the user
     * @return true if there is no user with the given login in the system, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public boolean isLoginFree(String login) throws DAOException {
        boolean isFree;
        try (PreparedStatement st = connection.prepareStatement(SELECT_BY_LOGIN)) {
            st.setString(1, login);
            ResultSet rs = st.executeQuery();
            isFree = !rs.next();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return isFree;
    }

    /**
     * Checks if there is no user with the given e-mail in the system.
     *
     * @param email e-mail of the user
     * @return true if there is no user with the given e-mail in the system, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public boolean isEmailFree(String email) throws DAOException {
        boolean isFree;
        try (PreparedStatement st = connection.prepareStatement(SELECT_BY_EMAIL)) {
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            isFree = !rs.next();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return isFree;
    }

    /**
     * Updates number of rated by the user movies.
     *
     * @param userId   id of the user
     * @param numRated new number of rated movies
     * @return true if the number was updated, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public boolean updateNumRated(int userId, int numRated) throws DAOException {
        int rows;
        try (PreparedStatement st = connection.prepareStatement(UPDATE_NUM_RATED)) {
            st.setInt(1, numRated);
            st.setInt(2, userId);
            rows = st.executeUpdate();
            if (rows > 0) {
                log.info("User [id = " + userId + " numRated updated to " + numRated);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return rows > 0;
    }

    /**
     * Checks if the user has rated the movie.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return true if the user has rated the movie, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public boolean ratedMovie(int movieId, int userId) throws DAOException {
        try (PreparedStatement st = connection.prepareStatement(SELECT_RATING)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    public boolean canEdit(int userId, int movieId) throws DAOException {
        boolean canEdit = false;
        try (PreparedStatement st = connection.prepareStatement(SELECT_CAN_EDIT)) {
            st.setInt(1, movieId);
            st.setInt(2, userId);
            st.setInt(3, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                canEdit = (rs.getInt("VALUE") > 0);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return canEdit;
    }

    public boolean createToken(int userId, String token) throws DAOException {
        if (token.length() > 50) {
            throw new DAOException("Token is too long.");
        }
        boolean added;
        try (PreparedStatement st = connection.prepareStatement(INSERT_TOKEN)) {
            st.setInt(1, userId);
            st.setString(2, token);
            added = st.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return added;
    }

    public String findToken(int userId) throws DAOException {
        String token = null;
        try (PreparedStatement st = connection.prepareStatement(SELECT_TOKEN)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                token = rs.getString("token");
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return token;
    }

    public boolean expiredToken(int userId) throws DAOException {
        boolean expired = true;
        try (PreparedStatement st = connection.prepareStatement(SELECT_TOKEN_TIME)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                Timestamp generated = rs.getTimestamp("generated");
                int minutes = rs.getInt("valid_for_minutes");

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(generated.getTime());
                cal.add(Calendar.MINUTE, minutes);
                Timestamp expires = new Timestamp(cal.getTime().getTime());

                expired = expires.before(new Timestamp(Calendar.getInstance().getTime().getTime()));
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return expired;
    }
}
