package com.epam.restaurant.dao.util;

import com.epam.restaurant.dao.DAOException;

import java.sql.Connection;

/**
 * The ${@code TransactionDAO} interface provides methods for working with a transaction at the DAO level
 */
public interface TransactionDAO {
    /**
     * Gets a connection from the connection pool and puts it in ThreadLocal
     * @throws DAOException if an error occurred in the method
     */
    void startTransaction() throws DAOException;

    /**
     * Makes a commit in a connection and removes it from ThreadLocal
     * @throws DAOException if an error occurred in the method
     */
    void commit() throws DAOException;

    /**
     * Makes a rollback in a connection and removes it from ThreadLocal
     * @throws DAOException if an error occurred in the method
     */
    void rollback() throws DAOException;

    /**
     * Returns the ThreadLocal that stores the connection
     * @return the ThreadLocal that stores the connection
     */
    ThreadLocal<Connection> getConnectionHolder();
}
