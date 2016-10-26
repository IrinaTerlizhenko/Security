package by.bsu.cinemarating.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;

/**
 * Wrapper on java.sql.Connection.
 */
public class WrapperConnection {
    private static Logger log = LogManager.getLogger(WrapperConnection.class);
    private Connection connection;

    /**
     * @param prop a list of arbitrary string tag/value pairs as connection arguments; normally at least a "user" and "password" property should be included
     * @param url  a database url of the form jdbc:subprotocol:subname
     */
    WrapperConnection(Properties prop, String url) {
        try {
            connection = DriverManager.getConnection(url, prop);
        } catch (SQLException e) {
            log.error("Connection not obtained.", e);
        }
    }

    /**
     * Releases Statement object's database and JDBC resources immediately instead of waiting for this to happen when it is automatically closed.
     *
     * @param statement Statement object related to this WrapperConnection to release resources
     */
    public void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("Statement is null.", e);
            }
        }
    }

    /**
     * Releases this Connection object's database and JDBC resources immediately instead of waiting for them to be automatically released.
     */
    void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("Wrong connection.", e);
            }
        }
    }

    /**
     * Creates a PreparedStatement object for sending parameterized SQL statements to the database.
     *
     * @param sql an SQL statement that may contain one or more '?' IN parameter placeholders
     * @return a new default PreparedStatement object containing the pre-compiled SQL statement
     * @throws SQLException if connection or statement is null
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (connection != null) {
            PreparedStatement statement = connection.prepareStatement(sql);
            if (statement != null) {
                return statement;
            }
        }
        throw new SQLException("Connection or statement is null.");
    }

    /**
     * Sets this connection's auto-commit mode to the given state. If a connection is in auto-commit mode, then all its
     * SQL statements will be executed and committed as individual transactions. Otherwise, its SQL statements are
     * grouped into transactions that are terminated by a call to either the method commit or the method rollback. By
     * default, new connections are in auto-commit mode.
     *
     * @param autoCommit true to enable auto-commit mode; false to disable it
     * @throws SQLException if a database access error occurs, setAutoCommit(true) is called while participating in a
     *                      distributed transaction, or this method is called on a closed connection
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * Undoes all changes made in the current transaction and releases any database locks currently held by this
     * WrapperConnection object. This method should be used only when auto-commit mode has been disabled.
     *
     * @throws SQLException if a database access error occurs, this method is called while participating in a
     *                      distributed transaction, this method is called on a closed connection or this WrapperConnection object is in
     *                      auto-commit mode
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Makes all changes made since the previous commit/rollback permanent and releases any database locks currently
     * held by this Connection object. This method should be used only when auto-commit mode has been disabled.
     *
     * @throws SQLException if a database access error occurs, this method is called while participating in a
     *                      distributed transaction, if this method is called on a closed conection or this WrapperConnection object is in
     *                      auto-commit mode
     */
    public void commit() throws SQLException {
        connection.commit();
    }
}