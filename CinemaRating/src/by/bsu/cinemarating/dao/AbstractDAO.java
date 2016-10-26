package by.bsu.cinemarating.dao;

import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.entity.Entity;
import by.bsu.cinemarating.exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Abstract root class for classes that provide access to the database and deal with entities with two ids.
 */
public abstract class AbstractDAO<T extends Entity> implements IDAO<T> {
    protected WrapperConnection connection;
    protected static final String LAST_ID = "SELECT LAST_INSERT_ID() AS id";
    protected static final String ID = "id";

    public AbstractDAO(WrapperConnection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves all entities in the system.
     *
     * @return list of all entities
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public abstract List<T> findAll() throws DAOException;

    /**
     * Retrieves an entity with a specific id.
     *
     * @param id id of the entity to find
     * @return an entity with the given id wrapped in Optional if exists, Optional.empty() otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public abstract Optional<T> findEntityById(int id) throws DAOException;

    /**
     * Deletes the entity with a specific id from the system.
     *
     * @param id id of the entity to delete
     * @return true if the entity was deleted, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public abstract boolean delete(int id) throws DAOException;

    /**
     * Updates the entity with a specific id.
     *
     * @param entity new entity
     * @return previous entity with the given id
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public abstract T update(T entity) throws DAOException;

    /**
     * Sets the actual id of the entity.
     *
     * @param entity entity to update id
     * @throws SQLException if any exceptions occurred on the SQL layer
     */
    public void updateId(Entity entity) throws SQLException {
        try (PreparedStatement stId = connection.prepareStatement(LAST_ID)) {
            ResultSet rs = stId.executeQuery();
            if (rs.next()) {
                entity.setId(rs.getInt(ID));
            }
        }
    }

    /**
     * Retrieves id of the latest added entity of type T.
     *
     * @param sqlQuery query to select id
     * @return id of the latest added entity of type T
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public int selectLastId(String sqlQuery) throws DAOException {
        int id = 0;
        try (PreparedStatement st = connection.prepareStatement(sqlQuery)) {
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt(ID);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return id;
    }
}
