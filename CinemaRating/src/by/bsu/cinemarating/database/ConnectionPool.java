package by.bsu.cinemarating.database;

import com.mysql.jdbc.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread safe connection pool.
 */
public class ConnectionPool {
    private static Logger log = LogManager.getLogger(ConnectionPool.class);
    private BlockingQueue<WrapperConnection> connections;
    private static ReentrantLock lock = new ReentrantLock();

    private static final String RESOURCE = "resources.database";
    private static final String URL = "url";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String AUTORECONNECT = "autoReconnect";
    private static final String CHARENCODING = "characterEncoding";
    private static final String USEUNICODE = "useUnicode";
    private static final String POOLSIZE = "poolsize";
    private static final String USESSL = "useSSL";

    private Properties properties;
    private String url;

    /**
     * Nested class for lazy pool initialization.
     */
    private static class PoolHolder {
        private static ConnectionPool pool;
        private static AtomicBoolean init = new AtomicBoolean(false);

        static {
            try {
                lock.lock();
                if (!init.get()) {
                    pool = new ConnectionPool();
                    init.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private ConnectionPool() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            ResourceBundle resource = ResourceBundle.getBundle(RESOURCE);
            url = resource.getString(URL);
            String user = resource.getString(USER);
            String pass = resource.getString(PASSWORD);
            String autoReconnect = resource.getString(AUTORECONNECT);
            String charEncoding = resource.getString(CHARENCODING);
            String useUnicode = resource.getString(USEUNICODE);
            String useSSL = resource.getString(USESSL);
            int poolSize = Integer.parseInt(resource.getString(POOLSIZE));
            properties = new Properties();
            properties.put(USER, user);
            properties.put(PASSWORD, pass);
            properties.put(AUTORECONNECT, autoReconnect);
            properties.put(CHARENCODING, charEncoding);
            properties.put(USEUNICODE, useUnicode);
            properties.put(USESSL, useSSL);
            connections = new ArrayBlockingQueue<>(poolSize);
            for (int i = 0; i < poolSize; i++) {
                connections.add(new WrapperConnection(properties, url));
            }
        } catch (SQLException e) {
            log.fatal("Register Driver error:", e);
            throw new RuntimeException(e);
        }
    }

    public static ConnectionPool getInstance() {
        return PoolHolder.pool;
    }

    /**
     * @return WrapperConnection wrapped in Optional if the pool is not empty, Optional.empty() otherwise
     */
    public Optional<WrapperConnection> takeConnection() {
        WrapperConnection connection = null;
        try {
            connection = connections.poll(5, TimeUnit.SECONDS);
            log.debug("Connection taken.");
        } catch (InterruptedException e) {
            log.error(e);
        }
        return Optional.ofNullable(connection);
    }

    public boolean returnConnection(WrapperConnection connection) {
        log.debug("Connection returned.");
        return connections.add(connection);
    }

    public void closePool() {
        while (!connections.isEmpty()) {
            WrapperConnection connection = connections.poll();
            connection.closeConnection();
        }

        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = (com.mysql.jdbc.Driver) drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log.debug("Driver deregistered.", driver);
            } catch (SQLException e) {
                log.error("Error while deregistering driver.", e);
            }
        }

        log.debug("Pool closed.");
    }
}
