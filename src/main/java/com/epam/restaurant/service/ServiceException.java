package com.epam.restaurant.service;

/**
 * The ${@code ServiceException} occurs when there is an error in the methods of the service level
 */
public class ServiceException extends Exception{
    private static final long serialVersionUID = 2585357278908575546L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
