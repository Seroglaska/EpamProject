package com.epam.restaurant.dao;

/**
 * The ${@code DAOException} occurs when there is an error in the methods of the DAO level
 */
public class DAOException extends Exception{
    private static final long serialVersionUID = -6436531393447944334L;

    public DAOException() {
        super();
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
