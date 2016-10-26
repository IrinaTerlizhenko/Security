package by.bsu.cinemarating.logic;

import by.bsu.cinemarating.dao.BanDAO;
import by.bsu.cinemarating.dao.UserDAO;
import by.bsu.cinemarating.database.ConnectionPool;
import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.exception.DAOException;
import by.bsu.cinemarating.exception.LogicException;
import by.bsu.cinemarating.logic.token.TokenGenerator;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by User on 16.10.2016.
 */
public class TokenLogic {
    private static final int TOKEN_LENGTH = 50;
    public static String generateToken(int userId) throws LogicException {
        String token = new TokenGenerator(TOKEN_LENGTH).nextString();
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            userDAO.createToken(userId, token);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return token;
    }

    public static boolean checkToken(int userId, String token) throws LogicException {
        boolean equals = false;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            equals = token.equals(userDAO.findToken(userId));
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return equals;
    }

    public static boolean expiredToken(int userId) throws LogicException {
        boolean expired = true;
        Optional<WrapperConnection> optConnection = Optional.empty();
        try {
            optConnection = ConnectionPool.getInstance().takeConnection();
            WrapperConnection connection = optConnection.orElseThrow(SQLException::new);
            UserDAO userDAO = new UserDAO(connection);
            expired = userDAO.expiredToken(userId);
        } catch (SQLException | DAOException e) {
            throw new LogicException(e);
        } finally {
            optConnection.ifPresent(ConnectionPool.getInstance()::returnConnection);
        }
        return expired;
    }
}
