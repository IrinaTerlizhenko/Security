package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.dao.BanDAO;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Ban;
import by.bsu.cinemarating.entity.BanType;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.exception.LogicException;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

/**
 * A service layer class implementing all the logic concerning bans.
 */
public class BanLogic {
    /**
     * Bans the user with a specific id.
     *
     * @param userId     id of user to ban
     * @param type       type of the ban
     * @param reason     reason of the ban
     * @param expiration time of ban expiration
     * @return true if ban was added, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean addBan(int userId, BanType type, String reason, Timestamp expiration) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            BanDAO banDAO = new BanDAO(connection);
            Ban ban = new Ban(0, userId, type, expiration, reason);
            result = banDAO.create(ban);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    /**
     * Checks if the user with a specific id is banned.
     *
     * @param userId id of the user
     * @return true if the user is banned, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean isUserBanned(int userId) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            BanDAO banDAO = new BanDAO(connection);
            List<Timestamp> expirations = banDAO.selectBanExpirations(userId);
            Timestamp now = new Timestamp(new java.util.Date().getTime());
            Timestamp latestExpiration = expirations
                    .stream()
                    .max((o1, o2) -> o1.compareTo(o2))
                    .orElse(now);
            result = now.compareTo(latestExpiration) < 0;
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    /**
     * Deletes the ban with a specific id.
     *
     * @param banId id of the ban to delete
     * @return true if the ban was deleted, false otherwise
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static boolean deleteBan(int banId) throws LogicException {
        boolean result = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            BanDAO banDAO = new BanDAO(connection);
            result = banDAO.delete(banId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return result;
    }

    /**
     * Retrieves id of the last added ban.
     *
     * @return id of the last added ban
     * @throws LogicException if any exceptions occurred on the DAO or SQL layer
     */
    public static int takeLastId() throws LogicException {
        int id = 0;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            BanDAO banDAO = new BanDAO(connection);
            id = banDAO.selectLastId();
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return id;
    }
}
