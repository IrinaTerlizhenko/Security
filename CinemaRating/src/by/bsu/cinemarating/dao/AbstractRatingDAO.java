package by.bsu.cinemarating.dao;

import by.bsu.cinemarating.database.WrapperConnection;
import by.bsu.cinemarating.exception.DAOException;

import java.util.Optional;

/**
 * Created by User on 06.06.2016.
 */
public abstract class AbstractRatingDAO<T> implements IDAO<T> {
    protected WrapperConnection connection;

    public AbstractRatingDAO(WrapperConnection connection) {
        this.connection = connection;
    }

    /**
     * Deletes the entity with specific ids from the system.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return true if the entity was deleted, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public abstract boolean delete(int movieId, int userId) throws DAOException;

    /**
     * Retrieves an entity with specific ids.
     *
     * @param movieId id of the movie
     * @param userId  id of the user
     * @return an entity with the given ids wrapped in Optional if exists, Optional.empty() otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public abstract Optional<T> findEntityById(int movieId, int userId) throws DAOException;

    /**
     * Replaces an entity with the new one.
     *
     * @param entity new entity
     * @return true if the entity was replaced, false otherwise
     * @throws DAOException if any exceptions occurred on the SQL layer
     */
    public abstract boolean replace(T entity) throws DAOException;
}
