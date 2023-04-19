package com.epam.restaurant.service.util;

import com.epam.restaurant.service.ServiceException;

/**
 * The ${@code TransactionService} interface provides methods for working with a transaction at the service level
 */
public interface TransactionService {
    /**
     * Gets a connection from the connection pool and puts it in ThreadLocal
     * @throws ServiceException if an error occurred in the method
     */
    void startTransaction() throws ServiceException;

    /**
     * Makes a commit in a connection and removes it from ThreadLocal
     * @throws ServiceException if an error occurred in the method
     */
    void commit() throws ServiceException;

    /**
     * Makes a rollback in a connection and removes it from ThreadLocal
     * @throws ServiceException if an error occurred in the method
     */
    void rollback() throws ServiceException;
}
