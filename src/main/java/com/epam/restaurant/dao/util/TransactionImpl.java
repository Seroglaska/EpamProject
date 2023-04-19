package com.epam.restaurant.dao.util;

import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionImpl implements TransactionDAO{
    private final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    @Override
    public void startTransaction() throws DAOException {
        try {
            Connection connection = ConnectionPool.getInstance().takeConnection();
            connection.setAutoCommit(false);

            connectionHolder.set(connection);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection..", e);
        } catch (SQLException e) {
            throw new DAOException("Error to set Auto commit for the connection..");
        }
    }

    @Override
    public void commit() throws DAOException {
        try {
            Connection connection = connectionHolder.get();
            connection.commit();

            connectionHolder.remove();
            connection.close();
        } catch (SQLException e) {
            throw new DAOException("Error to commit transaction..", e);
        }
    }

    @Override
    public void rollback() throws DAOException {
        try {
            Connection connection = connectionHolder.get();
            connection.rollback();

            connectionHolder.remove();
            connection.close();
        } catch (SQLException e) {
            throw new DAOException("Error to rollback transaction..", e);
        }
    }

    @Override
    public ThreadLocal<Connection> getConnectionHolder() {
        return connectionHolder;
    }
}
